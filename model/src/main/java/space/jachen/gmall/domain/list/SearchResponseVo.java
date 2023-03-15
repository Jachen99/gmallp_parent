package space.jachen.gmall.domain.list;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchResponseVo
 */
@Data
public class SearchResponseVo implements Serializable {

    /**
     * 品牌
     */
    private List<SearchResponseTmVo> trademarkList;   // 此时vo对象中的id字段保留（不用写） name就是“品牌” value: [{id:100,name:华为,logo:xxx},{id:101,name:小米,log:yyy}]
    /**
     * 所有商品的顶头显示的筛选属性
     */
    private List<SearchResponseAttrVo> attrsList = new ArrayList<>();
    /**
     * 检索出来的商品信息
     */
    private List<Goods> goodsList = new ArrayList<>();
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 每页显示的内容
     */
    private Integer pageSize;
    /**
     * 当前页面
     */
    private Integer pageNo;
    /**
     * 总页数
     */
    private Long totalPages;

}
