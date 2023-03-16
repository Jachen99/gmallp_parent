package space.jachen.gmall.all.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.list.SearchParam;
import space.jachen.gmall.domain.list.SearchResponseVo;
import space.jachen.gmall.list.client.ListFeignClient;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        String trademarkParam = makeTmParam(searchParam);
        List<Map<String, String>> propsParamList = this.makeProps(searchParam);
        Map<String,Object> orderMap = this.makeOrder(searchParam);
        model.addAttribute("searchParam", searchParam);
        model.addAttribute("urlParam", urlParam);
        model.addAttribute("trademarkParam",trademarkParam);
        model.addAttribute("propsParamList",propsParamList);
        model.addAttribute("orderMap",orderMap);
        model.addAllAttributes(map);

        return "list/index";
    }

    private Map<String, Object> makeOrder(SearchParam searchParam) {
        Map<String,Object> orderMap = new HashMap<>();
        String order = searchParam.getOrder();
        if(!StringUtils.isEmpty(order)) {
            String[] split = StringUtils.split(order, ":");
            if (split != null && split.length == 2) {
                // 传递的哪个字段
                orderMap.put("type", split[0]);
                // 升序降序
                orderMap.put("sort", split[1]);
            }
        }else {
            orderMap.put("type", "1");
            orderMap.put("sort", "asc");
        }
        return orderMap;

    }

    /**
     * 平台属性信息面包屑
     * @param searchParam
     * @return
     */
    private List<Map<String, String>> makeProps(SearchParam searchParam) {
        List<Map<String, String>> list = new ArrayList<>();
        String[] props = searchParam.getProps();
        if (props!=null && props.length>0){
            for (String prop : props) {
                String[] split = prop.split(":");
                if (split!=null && split.length==3){
                    HashMap<String, String> map = new HashMap<String,String>();
                    map.put("attrId",split[0]);
                    map.put("attrValue",split[1]);
                    map.put("attrName",split[2]);
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 品牌面包屑
     * @param searchParam
     * @return
     */
    private String makeTmParam(SearchParam searchParam) {
        if (!StringUtils.isEmpty(searchParam.getTrademark())) {
            String[] split = searchParam.getTrademark().split(":");
            if (split != null && split.length == 2) {
                return "品牌：" + split[1];
            }
        }
        return "";
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