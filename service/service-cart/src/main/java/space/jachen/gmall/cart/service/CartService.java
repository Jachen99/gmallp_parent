package space.jachen.gmall.cart.service;

import space.jachen.gmall.domain.cart.CartInfo;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/17 21:39
 */
public interface CartService {


    /**
     * 根据用户Id 查询购物车列表
     *
     * @param userId
     * @return
     */
    List<CartInfo> getCartCheckedList(String userId);

    void deleteCart(Long skuId, String userId);

    /**
     * 更新选中状态
     *
     * @param userId  userId
     * @param isChecked  isChecked
     * @param skuId  skuId
     */
    void checkCart(String userId, Integer isChecked, Long skuId);


    /**
     * 添加购物车 用户Id，商品Id，商品数量。
     * @param skuId  skuId
     * @param userId  userId
     * @param skuNum  skuNum
     */
    void addToCart(Long skuId, String userId, Integer skuNum);

    /**
     * 通过用户Id 查询购物车列表
     * @param userId
     * @param userTempId
     * @return
     */
    List<CartInfo> getCartList(String userId, String userTempId);

    /**
     * 勾选所有购物车
     * @param userId
     * @param isChecked
     */
    void allCheckCart(String userId, Integer isChecked);

    /**
     * 清空购物车
     * @param userId
     */
    void clearCart(String userId);
}
