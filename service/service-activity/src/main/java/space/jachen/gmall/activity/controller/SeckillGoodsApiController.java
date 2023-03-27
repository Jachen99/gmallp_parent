package space.jachen.gmall.activity.controller;

/**
 * @author JaChen
 * @date 2023/3/24 20:05
 */

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.activity.service.SeckillGoodsService;
import space.jachen.gmall.activity.utils.CacheHelper;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.result.ResultCodeEnum;
import space.jachen.gmall.common.util.AuthContextHolder;
import space.jachen.gmall.common.util.DateUtil;
import space.jachen.gmall.common.util.MD5;
import space.jachen.gmall.domain.activity.OrderRecode;
import space.jachen.gmall.domain.activity.SeckillGoods;
import space.jachen.gmall.domain.activity.UserRecode;
import space.jachen.gmall.domain.order.OrderDetail;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.domain.user.UserAddress;
import space.jachen.gmall.order.OrderFeignClient;
import space.jachen.gmall.product.client.ProductFeignClient;
import space.jachen.gmall.user.client.UserFeignClient;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/activity/seckill")
@SuppressWarnings("all")
public class SeckillGoodsApiController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 提交订单
     * @param orderInfo
     * @param request
     * @return
     */
    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderInfo orderInfo, HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        orderInfo.setUserId(Long.parseLong(userId));
        Long orderId = orderFeignClient.submitOrder(orderInfo);
        if (null == orderId) {
            return Result.fail().message("下单失败，请重新操作");
        }
        //删除下单信息
        redisTemplate.boundHashOps(RedisConst.SECKILL_ORDERS).delete(userId);
        //下单记录
        redisTemplate.boundHashOps(RedisConst.SECKILL_ORDERS_USERS).put(userId, orderId.toString());

        return Result.ok(orderId);
    }




    /**
     * 封装秒杀确认订单
     * @param request
     * @return
     */
    @GetMapping("auth/trade")
    public Result<Map<String,Object>> trade(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        // 获取到用户Id
        String userId = AuthContextHolder.getUserId(request);
        // 先得到用户想要购买的商品
        OrderRecode orderRecode = (OrderRecode) redisTemplate.boundHashOps(RedisConst.SECKILL_ORDERS).get(userId);
        if (null == orderRecode) {
            return Result.fail();
        }
        SeckillGoods seckillGoods = orderRecode.getSeckillGoods();
        // 获取商品详情list
        List<OrderDetail> detailArrayList = getDetailArrayList(orderRecode, seckillGoods);
        result.put("detailArrayList", detailArrayList);
        // 计算总金额
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderDetailList(detailArrayList);
        orderInfo.sumTotalAmount();
        result.put("totalAmount", orderInfo.getTotalAmount());
        //获取用户地址
        List<UserAddress> userAddressList = userFeignClient.findUserAddressListByUserId(userId);
        result.put("userAddressList", userAddressList);

        return Result.ok(result);
    }

    /**
     * 封装订单明细数据
     * @param orderRecode
     * @param seckillGoods
     * @return
     */
    private static List<OrderDetail> getDetailArrayList(OrderRecode orderRecode, SeckillGoods seckillGoods) {
        // 声明一个集合来存储订单明细
        List<OrderDetail> detailArrayList = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setSkuId(seckillGoods.getSkuId());
        orderDetail.setSkuName(seckillGoods.getSkuName());
        orderDetail.setImgUrl(seckillGoods.getSkuDefaultImg());
        orderDetail.setSkuNum(orderRecode.getNum()); // 1
        orderDetail.setOrderPrice(seckillGoods.getCostPrice());
        // 添加到集合
        detailArrayList.add(orderDetail);
        return detailArrayList;
    }


    /**
     * 查看订单状态 展示轮询排队页面
     * @param skuId
     * @param request
     * @return
     */
    @GetMapping(value = "auth/checkOrder/{skuId}")
    public Result checkOrder(@PathVariable Long skuId, HttpServletRequest request) {
        //当前登录用户
        String userId = AuthContextHolder.getUserId(request);
        return seckillGoodsService.checkOrder(skuId, userId);
    }




    /**
     * 根据用户和商品ID实现秒杀下单
     * @param skuId
     * @return
     */
    @PostMapping("auth/seckillOrder/{skuId}")
    public Result seckillOrder(@PathVariable String skuId, HttpServletRequest request){

        // 获取用户id
        String userId = AuthContextHolder.getUserId(request);
        // 获取下单码
        String skuIdStr = request.getParameter("skuIdStr");
        if (StringUtils.isEmpty(userId)||StringUtils.isEmpty(skuIdStr))
            return Result.fail().message("您没有秒杀资格");
        if (!MD5.encrypt(userId+skuId).equals(skuIdStr))
            return Result.build("您不是合法用户！", ResultCodeEnum.ILLEGAL_REQUEST);
        // 从本地缓存获取 秒杀状态位  0或1
        String status = CacheHelper.get(skuId);
        if (!"0".equals(status)&&!"1".equals(status))
            return Result.build("您不是合法用户！", ResultCodeEnum.ILLEGAL_REQUEST);
        if ("0".equals(status))
            return Result.build("秒杀商品已售罄！", ResultCodeEnum.SECKILL_FINISH);
        if ("1".equals(status)){
            //用户记录
            UserRecode userRecode = new UserRecode();
            userRecode.setUserId(userId);
            userRecode.setSkuId(Long.parseLong(skuId));

            // 进入消息队列排队 等待秒杀
            rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_SECKILL_USER, MqConst.ROUTING_SECKILL_USER, userRecode);
        }
        // 返回正在排队状态
        return Result.ok(ResultCodeEnum.SECKILL_RUN);
    }



    /**
     * 获取下单码
     * @param skuId
     * @param request
     * @return
     */
    @GetMapping("auth/getSeckillSkuIdStr/{skuId}")
    public Result getSeckillSkuIdStr(@PathVariable Long skuId, HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        SeckillGoods seckillGoods = seckillGoodsService.getSeckillGoods(skuId);
        if (null != seckillGoods) {
            Date curTime = new Date();
            // 比对时间大小
            if (DateUtil.dateCompare(seckillGoods.getStartTime(), curTime) && DateUtil.dateCompare(curTime, seckillGoods.getEndTime())) {
                //可以动态生成，放在redis缓存
                String skuIdStr = MD5.encrypt(userId+skuId);
                return Result.ok(skuIdStr);
            }
        }
        return Result.fail().message("获取下单码失败");
    }



    /**
     * 返回全部列表
     * @return
     */
    @GetMapping("/findAll")
    public Result<List<SeckillGoods>> findAll() {
        List<SeckillGoods> serviceAll = seckillGoodsService.findAll();
        return Result.ok(serviceAll);
    }


    /**
     * 获取实体
     * @param skuId
     * @return
     */
    @GetMapping("/getSeckillGoods/{skuId}")
    public Result<SeckillGoods> getSeckillGoods(@PathVariable Long skuId) {
        return Result.ok(seckillGoodsService.getSeckillGoods(skuId));
    }
}

