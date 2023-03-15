package space.jachen.gmall.domain.list;

import lombok.Builder;
import lombok.Data;

/**
 * 查询条件
 */
@Data
@Builder
public class SearchParam {  // ?category3Id=61&trademark=2:华为&props=23:4G:运行内存&order=1:desc

    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    /**
     * 品牌
     */
    private String trademark;
    /**
     * 检索的关键字
     */
    private String keyword;
    /**
     * 排序规则
     */
    private String order = ""; // 1：综合排序/热度  2：价格  || 1:hotScore 2:price
    /**
     * 平台属性信息数组
     */
    private String[] props; //平台属性Id 平台属性值名称 平台属性名  ||  props=23:4G:运行内存
    /**
     * 第几页
     */
    private Integer pageNo = 1;
    /**
     * 每页默认显示的条数
     */
    private Integer pageSize = 3;


}
