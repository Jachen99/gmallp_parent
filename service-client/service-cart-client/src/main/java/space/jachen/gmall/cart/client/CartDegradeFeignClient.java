package space.jachen.gmall.cart.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.cart.CartInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 21:55
 */
@Component
public class CartDegradeFeignClient implements CartFeignClient{

    @Override
    public Result<String> deleteCart(Long skuId, HttpServletRequest request) {
        return null;
    }

    @Override
    public Result<String> checkCart(Long skuId, Integer isChecked, HttpServletRequest request) {
        return null;
    }

    @Override
    public Result<List<CartInfo>> cartList(HttpServletRequest request) {
        return null;
    }

    @Override
    public Result<String> addToCart(Long skuId, Integer skuNum, HttpServletRequest request) {
        return null;
    }
}
