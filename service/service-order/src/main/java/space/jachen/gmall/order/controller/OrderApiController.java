package space.jachen.gmall.order.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.cart.client.CartFeignClient;
import space.jachen.gmall.common.config.ThreadPoolConfig;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.util.AuthContextHolder;
import space.jachen.gmall.domain.cart.CartInfo;
import space.jachen.gmall.domain.order.OrderDetail;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.domain.user.UserAddress;
import space.jachen.gmall.order.service.OrderService;
import space.jachen.gmall.product.client.ProductFeignClient;
import space.jachen.gmall.user.client.UserFeignClient;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author JaChen
 * @date 2023/3/19 23:18
 */
@RestController
@RequestMapping("api/order")
@SuppressWarnings("all")
public class OrderApiController {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private CartFeignClient cartFeignClient;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThreadPoolConfig threadPoolConfig;

    /**
     * 提交订单
     * @param orderInfo
     * @param request
     * @return
     */
    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderInfo orderInfo, HttpServletRequest request) {
        // 1、获取用户Id
        String userId = AuthContextHolder.getUserId(request);
        orderInfo.setUserId(Long.parseLong(userId));
        // 2、校验
        // 收集错误信息
        List<String> errorList = new ArrayList<>();
        // 2.1、比对流水号
        // 异步编排对象
        List<CompletableFuture> futureList = new ArrayList<>();
        ThreadPoolExecutor executor = threadPoolConfig.executor();
        // 获取前台页面的流水号
        String tradeNo = request.getParameter("tradeNo");
        // 调用服务层的比较方法
        CompletableFuture<Void> tradeFuture = CompletableFuture.runAsync(() -> {
            boolean flag = orderService.checkTradeCode(userId, tradeNo);
            if (!flag) {
                // 比较失败！
                errorList.add("不能重复提交订单");
            }
        }, executor);
        futureList.add(tradeFuture);
        // 2.2、比对库存
        orderInfo.getOrderDetailList().forEach(orderDetail -> {
            CompletableFuture<Void> stockFuture = CompletableFuture.runAsync(() -> {
                boolean checkStock = orderService.checkStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
                if (!checkStock)
                    errorList.add(orderDetail.getSkuName() + ":库存不足");
            }, executor);
            futureList.add(stockFuture);
            // 2.3、校验价格
            CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
                BigDecimal skuPrice = productFeignClient.getSkuPrice(orderDetail.getSkuId());
                if (orderDetail.getOrderPrice().compareTo(skuPrice) != 0) {
                    // 价格不同 更新购物车缓存
                    cartFeignClient.getCartCheckedList(userId).forEach(cartInfo -> {
                        redisTemplate.opsForHash().put(
                                RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX,
                                orderDetail.getSkuId(), cartInfo);
                    });
                    errorList.add(orderDetail.getSkuName() + ":价格有变动");
                }
            }, executor);
            futureList.add(priceFuture);
        });
        // 合并线程
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
        if (!CollectionUtils.isEmpty(errorList))
            return Result.fail().message(StringUtils.join(errorList,";\n"));
        // 验证通过，保存订单
        Long orderId = orderService.saveOrderInfo(orderInfo);
        //  删除流水号
        orderService.deleteTradeNo(userId);
        return Result.ok(orderId);
    }


    /**
     * 确认订单
     * @param request  HttpServletRequest
     * @return
     */
    @GetMapping("auth/trade")
    public Result<Map<String, Object>> trade(HttpServletRequest request) {
        // 获取到用户Id
        String userId = AuthContextHolder.getUserId(request);
        //获取用户地址
        List<UserAddress> userAddressList = userFeignClient.findUserAddressListByUserId(userId);
        // 渲染送货清单
        // 先得到用户想要购买的商品
        List<CartInfo> cartInfoList = cartFeignClient.getCartCheckedList(userId);
        // 声明一个集合来存储订单明细
        ArrayList<OrderDetail> detailArrayList = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());
            orderDetail.setImgUrl(cartInfo.getImgUrl());
            orderDetail.setSkuNum(cartInfo.getSkuNum());
            orderDetail.setOrderPrice(cartInfo.getSkuPrice());
            // 添加到集合
            detailArrayList.add(orderDetail);
        }
        // 计算总金额
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderDetailList(detailArrayList);
        orderInfo.sumTotalAmount();
        Map<String, Object> result = new HashMap<>();
        result.put("userAddressList", userAddressList);
        result.put("detailArrayList", detailArrayList);
        // 保存总金额
        result.put("totalNum", detailArrayList.size());
        result.put("totalAmount", orderInfo.getTotalAmount());
        // 增加流水号
        String tradeNo = orderService.getTradeNo(userId);
        result.put("tradeNo", tradeNo);

        return Result.ok(result);
    }

}
