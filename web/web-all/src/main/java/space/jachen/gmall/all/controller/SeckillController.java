package space.jachen.gmall.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import space.jachen.gmall.activity.client.ActivityFeignClient;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.activity.SeckillGoods;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/24 20:12
 */
@Controller
@SuppressWarnings("all")
public class SeckillController {

    @Autowired
    private ActivityFeignClient activityFeignClient;


    /**
     * 开始秒杀，排队页面
     * @param skuId
     * @param skuIdStr
     * @param request
     * @return
     */
    @GetMapping("seckill/queue.html")
    public String queue(@RequestParam Long skuId, @RequestParam String skuIdStr, HttpServletRequest request){
        request.setAttribute("skuId", skuId);
        request.setAttribute("skuIdStr", skuIdStr);

        return "seckill/queue";
    }





    /**
     * 展示秒杀页面
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("seckill/{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model){
        // 通过skuId 查询skuInfo
        Result<SeckillGoods> seckillGoods = activityFeignClient.getSeckillGoods(skuId);
        model.addAttribute("item", seckillGoods.getData());
        return "seckill/item";
    }



    /**
     * 秒杀列表
     * @param model
     * @return
     */
    @GetMapping("seckill.html")
    public String index(Model model) {
        Result<List<SeckillGoods>> listResult = activityFeignClient.findAll();
        model.addAttribute("list", listResult.getData());
        return "seckill/index";
    }
}
