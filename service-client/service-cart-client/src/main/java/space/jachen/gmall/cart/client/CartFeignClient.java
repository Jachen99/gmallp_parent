package space.jachen.gmall.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.gmall.domain.cart.CartInfo;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 21:54
 */
@FeignClient(value ="service-cart", fallback = CartDegradeFeignClient.class)
public interface CartFeignClient {

    public static final String BaseURL = "api/cart";

    /**
     * 根据用户Id 查询购物车列表
     *
     * @param userId userId
     * @return  List<CartInfo>
     */
    @GetMapping(BaseURL+"/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable String userId);



//    /**
//     * 删除
//     *
//     * @param skuId  skuId
//     * @param request  HttpServletRequest
//     * @return  Result<String>
//     */
//    @DeleteMapping(BaseURL+"deleteCart/{skuId}")
//    public Result<String> deleteCart(@PathVariable("skuId") Long skuId, HttpServletRequest request);
//
//    /**
//     * 选中状态
//     * @param skuId  skuId
//     * @param isChecked  isChecked
//     * @param request  HttpServletRequest
//     * @return  Result<String>
//     */
//    @GetMapping(BaseURL+"checkCart/{skuId}/{isChecked}")
//    public Result<String> checkCart(@PathVariable Long skuId, @PathVariable Integer isChecked, HttpServletRequest request);
//
//
//    /**
//     * 查询购物车
//     * @param request  HttpServletRequest
//     * @return  List<CartInfo>
//     */
//    @GetMapping(BaseURL+"/cartList")
//    public Result<List<CartInfo>> cartList(HttpServletRequest request);
//
//
//    /**
//     * 添加购物车
//     * @param skuId  skuId
//     * @param skuNum  skuNum
//     * @param request  HttpServletRequest
//     * @return  Result<String>
//     */
//    @GetMapping(BaseURL+"addToCart/{skuId}/{skuNum}")
//    public Result<String> addToCart(@PathVariable("skuId") Long skuId, @PathVariable("skuNum") Integer skuNum, HttpServletRequest request);
//

}
