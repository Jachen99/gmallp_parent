package space.jachen.gmall.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.cart.service.CartService;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.util.AuthContextHolder;
import space.jachen.gmall.domain.cart.CartInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 20:02
 */
@RestController
@RequestMapping("api/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;


    /**
     * 删除
     *
     * @param skuId  skuId
     * @param request  HttpServletRequest
     * @return  Result<String>
     */
    @DeleteMapping("deleteCart/{skuId}")
    public Result<String> deleteCart(@PathVariable("skuId") Long skuId, HttpServletRequest request) {
        // 如何获取userId
        String userId = AuthContextHolder.getUserId(request);
        if (StringUtils.isEmpty(userId)) {
            // 获取临时用户Id
            userId = AuthContextHolder.getUserTempId(request);
        }
        cartService.deleteCart(skuId, userId);
        return Result.ok();
    }



    /**
     * 选中状态
     * @param skuId  skuId
     * @param isChecked  isChecked
     * @param request  HttpServletRequest
     * @return  Result<String>
     */
    @GetMapping("checkCart/{skuId}/{isChecked}")
    public Result<String> checkCart(@PathVariable Long skuId, @PathVariable Integer isChecked, HttpServletRequest request){

        String userId = AuthContextHolder.getUserId(request);
        if (StringUtils.isEmpty(userId)){
            userId = AuthContextHolder.getUserTempId(request);
        }
        cartService.checkCart(userId,isChecked,skuId);
        return Result.ok();
    }



    /**
     * 查询购物车
     * @param request  HttpServletRequest
     * @return  List<CartInfo>
     */
    @GetMapping("/cartList")
    public Result<List<CartInfo>> cartList(HttpServletRequest request){
        String userId = AuthContextHolder.getUserId(request);
        String userTempId = AuthContextHolder.getUserTempId(request);
        List<CartInfo> cartList = cartService.getCartList(userId, userTempId);

        return Result.ok(cartList);
    }



    /**
     * 添加购物车
     * @param skuId  skuId
     * @param skuNum  skuNum
     * @param request  HttpServletRequest
     * @return  Result<String>
     */
    @GetMapping("addToCart/{skuId}/{skuNum}")
    public Result<String> addToCart(@PathVariable("skuId") Long skuId, @PathVariable("skuNum") Integer skuNum, HttpServletRequest request) {
        // 如何获取userId
        String userId = AuthContextHolder.getUserId(request);
        if (StringUtils.isEmpty(userId)) {
            // 获取临时用户Id
            userId = AuthContextHolder.getUserTempId(request);
        }
        cartService.addToCart(skuId, userId, skuNum);
        return Result.ok();
    }

}
