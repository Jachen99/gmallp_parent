package space.jachen.gmall.all.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.list.SearchParam;
import space.jachen.gmall.domain.list.SearchResponseVo;
import space.jachen.gmall.list.client.ListFeignClient;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/16 15:27
 */
@Controller
@SuppressWarnings("all")
public class listController {

    @Resource
    private ListFeignClient listFeignClient;

    @GetMapping("list.html")
    public String search(SearchParam searchParam, Model model) {
        Result<SearchResponseVo> responseVoResult = listFeignClient.list(searchParam);
        Map map = JSONObject.parseObject(JSON.toJSONString(responseVoResult.getData()), Map.class);
        // 记录拼接url
        String urlParam = makeUrlParam(searchParam);
        model.addAttribute("searchParam", searchParam);
        model.addAttribute("urlParam", urlParam);
        model.addAllAttributes(map);

        return "list/index";
    }


    /**
     * 拼接searchParam
     *
     * @param searchParam searchParam
     * @return String
     */
    private String makeUrlParam(SearchParam searchParam) {
        StringBuilder urlParam = new StringBuilder();
        // 判断关键字
        if (searchParam.getKeyword() != null) {
            urlParam.append("keyword=").append(searchParam.getKeyword());
        }
        // 判断一级分类
        if (searchParam.getCategory1Id() != null) {
            urlParam.append("category1Id=").append(searchParam.getCategory1Id());
        }
        // 判断二级分类
        if (searchParam.getCategory2Id() != null) {
            urlParam.append("category2Id=").append(searchParam.getCategory2Id());
        }
        // 判断三级分类
        if (searchParam.getCategory3Id() != null) {
            urlParam.append("category3Id=").append(searchParam.getCategory3Id());
        }
        // 处理品牌
        if (searchParam.getTrademark() != null) {
            if (urlParam.length() > 0) {
                urlParam.append("&trademark=").append(searchParam.getTrademark());
            }
        }
        // 判断平台属性值
        if (null != searchParam.getProps()) {
            for (String prop : searchParam.getProps()) {
                if (urlParam.length() > 0) {
                    urlParam.append("&props=").append(prop);
                }
            }
        }
        return "list.html?" + urlParam;
    }
}