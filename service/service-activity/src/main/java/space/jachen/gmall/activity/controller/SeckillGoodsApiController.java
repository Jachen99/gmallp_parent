package space.jachen.gmall.activity.controller;

/**
 * @author JaChen
 * @date 2023/3/24 20:05
 */

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.activity.service.SeckillGoodsService;
import space.jachen.gmall.activity.utils.CacheHelper;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.result.ResultCodeEnum;
import space.jachen.gmall.common.util.AuthContextHolder;
import space.jachen.gmall.common.util.DateUtil;
import space.jachen.gmall.common.util.MD5;
import space.jachen.gmall.domain.activity.SeckillGoods;
import space.jachen.gmall.domain.activity.UserRecode;
import space.jachen.gmall.product.client.ProductFeignClient;
import space.jachen.gmall.user.client.UserFeignClient;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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



    /**
     * 根据用户和商品ID实现秒杀下单
     * @param skuId
     * @return
     */
    @PostMapping("auth/seckillOrder/{skuId}")
    public Result seckillOrder(@PathVariable Long skuId, HttpServletRequest request){

        // 获取用户id
        String userId = AuthContextHolder.getUserId(request);
        // 获取下单码
        String skuIdStr = request.getParameter("skuIdStr");
        if (StringUtils.isEmpty(userId)||StringUtils.isEmpty(skuIdStr))
            return Result.fail().message("您没有秒杀资格");
        if (MD5.encrypt(userId).equals(skuId.toString()))
            return Result.build("您不是合法用户！", ResultCodeEnum.ILLEGAL_REQUEST);
        // 从本地缓存获取 秒杀状态位
        String status = CacheHelper.get(skuId.toString());
        if (StringUtils.isEmpty(status))
            return Result.build("您不是合法用户！", ResultCodeEnum.ILLEGAL_REQUEST);
        if (!"0".equals(status)&&!"1".equals(status))
            return Result.build("您不是合法用户！", ResultCodeEnum.ILLEGAL_REQUEST);
        if ("0".equals(status))
            return Result.build("秒杀商品已售罄！", ResultCodeEnum.SECKILL_FINISH);
        if ("1".equals(status)){
            //用户记录
            UserRecode userRecode = new UserRecode();
            userRecode.setUserId(userId);
            userRecode.setSkuId(skuId);

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
                String skuIdStr = MD5.encrypt(userId);
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

