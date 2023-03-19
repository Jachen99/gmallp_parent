package space.jachen.gmall.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.jachen.gmall.cart.client.CartFeignClient;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.product.client.ProductFeignClient;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JaChen
 * @date 2023/3/19 22:00
 */
@Controller
@SuppressWarnings("all")
public class CartController {

    @Autowired
    private CartFeignClient cartFeignClient;
    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 查看购物车
     * @return
     */
    @RequestMapping("cart.html")
    public String index(){

        return "cart/index";
    }

    /**
     * 添加购物车
     * @param skuId  skuId
     * @param skuNum  skuNum
     * @param request  request
     * @return  String
     */
    @RequestMapping("addCart.html")
    public String addCart(@RequestParam(name = "skuId") Long skuId, @RequestParam(name = "skuNum") Integer skuNum, HttpServletRequest request){
        SkuInfo skuInfo = productFeignClient.findSkuInfoBySkuId(skuId);
        request.setAttribute("skuInfo",skuInfo);
        request.setAttribute("skuNum",skuNum);

        return "cart/addCart";
    }
}
