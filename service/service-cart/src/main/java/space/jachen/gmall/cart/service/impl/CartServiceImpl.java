package space.jachen.gmall.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import space.jachen.gmall.cart.service.CartService;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.util.DateUtil;
import space.jachen.gmall.domain.cart.CartInfo;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.product.client.ProductFeignClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/3/17 22:44
 */
@Service
@SuppressWarnings("all")
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {

        String cartKey = getCartKey(userId);
        List<CartInfo> cartInfoList = null;
        if (!StringUtils.isEmpty(cartKey)) {
            cartInfoList = redisTemplate.opsForHash().values(cartKey);
            if (!CollectionUtils.isEmpty(cartInfoList)) {
                cartInfoList = cartInfoList.stream().map(cartInfo -> {
                    // 找出选中的商品返回
                    if (cartInfo.getIsChecked().intValue() == 1) {
                        return cartInfo;
                    }
                    return null;
                }).collect(Collectors.toList());
            }
        }
        return cartInfoList;
    }

    @Override
    public void deleteCart(Long skuId, String userId) {
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(this.getCartKey(userId));
        //  判断购物车中是否有该商品
        if (boundHashOps.hasKey(String.valueOf(skuId))) {
            boundHashOps.delete(String.valueOf(skuId));
        }
    }

    @Override
    public void checkCart(String userId, Integer isChecked, Long skuId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = boundHashOps.get(String.valueOf(skuId));
        if (null != cartInfo) {
            cartInfo.setIsChecked(isChecked);
            boundHashOps.put(String.valueOf(skuId), cartInfo);
        }
    }

    @Override
    public void addToCart(Long skuId, String userId, Integer skuNum) {
        String cartKey = getCartKey(userId);
        // 尝试获取redis中的cartKey  自动创建大key  即cartKey
        BoundHashOperations<String, String, CartInfo> operations = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = null;
        // 如果缓存中存在购物车信息
        if (operations.hasKey(String.valueOf(skuId))) {
            cartInfo = operations.get(String.valueOf(skuId));
            // 更新商品价格
            cartInfo.setCartPrice(productFeignClient.getSkuPrice(skuId));
            // 更新商品数量
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            // 更新选中状态
            cartInfo.setIsChecked(1);
            // 更新更新日期
            cartInfo.setUpdateTime(new Date());
        } else {
            // 如果缓存中没有购物车信息 远程获取商品信息
            SkuInfo skuInfo = productFeignClient.findSkuInfoBySkuId(skuId);
            // 创建新的购物车信息
            cartInfo = new CartInfo();
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setSkuPrice(skuInfo.getPrice());
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());

        }
        operations.put(String.valueOf(skuId), cartInfo);

    }

    @Override
    public List<CartInfo> getCartList(String userId, String userTempId) {

        List<CartInfo> noLoginCartInfoList = null;
        // 存在临时userTempId
        if (!StringUtils.isEmpty(userTempId)) {
            BoundHashOperations<String, String, CartInfo> operations = this.redisTemplate.boundHashOps(getCartKey(userTempId));
            noLoginCartInfoList = operations.values();
        }
        if (StringUtils.isEmpty(userId)) {
            if (!CollectionUtils.isEmpty(noLoginCartInfoList)) {
                // 对 cartInfoList 进行排序
                noLoginCartInfoList.stream().sorted(new Comparator<CartInfo>() {
                    @Override
                    public int compare(CartInfo o1, CartInfo o2) {
                        return DateUtil.truncatedCompareTo(o2.getUpdateTime(), o1.getUpdateTime(), Calendar.SECOND);
                    }
                });
            }
            // 返回未登录数据
            return noLoginCartInfoList;
        }

        // 如果登录  合并登录前的购物车
        List<CartInfo> loginCartInfoList = null;
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> operations = this.redisTemplate.boundHashOps(cartKey);
        if (!CollectionUtils.isEmpty(noLoginCartInfoList)) {
            noLoginCartInfoList.forEach(noLoginCartInfo -> {
                // 如果未登录的购物车的商品中有登录后的商品 进行合并
                if (operations.hasKey(String.valueOf(noLoginCartInfo.getSkuId()))) {
                    CartInfo loginCartInfo = operations.get(String.valueOf(noLoginCartInfo.getSkuId()));
                    loginCartInfo.setSkuNum(loginCartInfo.getSkuNum() + noLoginCartInfo.getSkuNum());
                    loginCartInfo.setUpdateTime(new Date());
                    loginCartInfo.setSkuPrice(productFeignClient.getSkuPrice(noLoginCartInfo.getSkuId()));
                    // 选中状态合并
                    if (noLoginCartInfo.getIsChecked().intValue() == 1) {
                        loginCartInfo.setIsChecked(1);
                    }
                    // 添加缓存
                    operations.put(String.valueOf(noLoginCartInfo.getSkuId()), loginCartInfo);
                } else {
                    // 如果没有 则不需要合并
                    noLoginCartInfo.setUserId(userId);
                    noLoginCartInfo.setCreateTime(new Date());
                    noLoginCartInfo.setUpdateTime(new Date());
                    // 添加缓存
                    operations.put(String.valueOf(noLoginCartInfo.getSkuId()), noLoginCartInfo);
                }
            });
            // 删除未登录购物车数据
            this.redisTemplate.delete(this.getCartKey(userTempId));
        }

        //  获取到合并之后的数据：
        loginCartInfoList = this.redisTemplate.boundHashOps(cartKey).values();
        if (!CollectionUtils.isEmpty(loginCartInfoList)) {
            loginCartInfoList.sort(new Comparator<CartInfo>() {
                @Override
                public int compare(CartInfo o1, CartInfo o2) {
                    return DateUtil.truncatedCompareTo(o2.getUpdateTime(), o1.getUpdateTime(), Calendar.SECOND);
                }
            });
            return loginCartInfoList;
        }

        return new ArrayList<>();
    }

    @Override
    public void allCheckCart(String userId, Integer isChecked) {
        //  获取到购物车key
        String cartKey = this.getCartKey(userId);
        List<CartInfo> cartInfoList = this.redisTemplate.opsForHash().values(cartKey);
        //  声明一个Map 集合
        Map<String, Object> hashMap = new HashMap<>();
        //  循环遍历
        for (CartInfo cartInfo : cartInfoList) {
            cartInfo.setIsChecked(isChecked);
            hashMap.put(cartInfo.getSkuId().toString(),cartInfo);
        }
        //  将数据存储到购物车中
        this.redisTemplate.opsForHash().putAll(cartKey,hashMap);
    }

    @Override
    public void clearCart(String userId) {
        //  获取购物车key
        String cartKey = getCartKey(userId);
        //  删除数据
        this.redisTemplate.delete(cartKey);
    }

    /**
     * 获取购物车key
     *
     * @param userId
     */
    private static String getCartKey(String userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }

}
