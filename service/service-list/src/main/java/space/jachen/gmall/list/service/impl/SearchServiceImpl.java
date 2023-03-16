package space.jachen.gmall.list.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.list.*;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.list.repository.GoodsRepository;
import space.jachen.gmall.list.service.SearchService;
import space.jachen.gmall.product.client.ProductFeignClient;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/3/14 11:48
 */
@Service
@SuppressWarnings("all")
public class SearchServiceImpl implements SearchService {

    @Resource
    private ProductFeignClient productFeignClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void upperGoods(Long skuId) {
        Goods goods = new Goods();
        // 封装 Attrs 属性
        List<BaseAttrInfo> baseAttrInfos = productFeignClient.getAttrListBySkuId(skuId);
        List<SearchAttr> searchAttrList = baseAttrInfos.stream().map(baseAttrInfo -> {
            SearchAttr searchAttr = new SearchAttr();
            searchAttr.setAttrId(baseAttrInfo.getId());
            searchAttr.setAttrName(baseAttrInfo.getAttrName());
            searchAttr.setAttrValue(baseAttrInfo.getAttrValueList().get(0).getValueName());
            return searchAttr;
        }).collect(Collectors.toList());
        goods.setAttrs(searchAttrList);
        SkuInfo skuInfo = productFeignClient.findSkuInfoBySkuId(skuId);
        // 封装 skuInfo 属性
        if (null != skuInfo) {
            goods.setTitle(skuInfo.getSkuName());
            goods.setId(skuId);
            goods.setDefaultImg(skuInfo.getSkuDefaultImg());
            goods.setPrice(skuInfo.getPrice().doubleValue());
            goods.setCreateTime(new Date());
        }
        BaseTrademark trademark = productFeignClient.getTrademark(skuInfo.getTmId());
        // 封装 trademark 属性
        if (null != trademark) {
            goods.setTmId(trademark.getId());
            goods.setTmLogoUrl(trademark.getLogoUrl());
            goods.setTmName(trademark.getTmName());
        }
        // 封装 categoryView 信息
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        if (null != categoryView) {
            goods.setCategory1Id(categoryView.getCategory1Id());
            goods.setCategory1Name(categoryView.getCategory1Name());
            goods.setCategory2Id(categoryView.getCategory2Id());
            goods.setCategory2Name(categoryView.getCategory2Name());
            goods.setCategory3Id(categoryView.getCategory3Id());
            goods.setCategory3Name(categoryView.getCategory3Name());
        }
        // 存入 es
        goodsRepository.save(goods);
    }

    @Override
    public void lowerGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public void incrHotScore(Long skuId) {
        String hotKey = "hotScore";
        Double hotScore = redisTemplate.opsForZSet().incrementScore(hotKey, RedisConst.SKUKEY_PREFIX + skuId, 10);
        // 每点击十次更新一次es
        if (hotScore % 100 == 0) {
            // 查询原始hotScore值
            Optional<Goods> optional = goodsRepository.findById(skuId);
            if (optional.isPresent()) {
                Goods goods = optional.get();
                goods.setHotScore(hotScore.longValue());
                goodsRepository.save(goods);
            }
        }
    }

    @Override
    @SneakyThrows
    public SearchResponseVo list(SearchParam searchParam) {

        // 1、构建DSL语句
        SearchRequest searchRequest = new SearchRequest("goods");
        // 查询器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 搜索条件之 关键字
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(
                    QueryBuilders.matchQuery(
                            "title", searchParam.getKeyword()
                    ).operator(Operator.AND)
            );
        }
        // 搜索条件之 品牌
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = StringUtils.split(trademark, ":");
            if (split != null && split.length == 2) {
                boolQueryBuilder.filter(
                        QueryBuilders.termQuery("tmId",split[0])
                );
            }
        }
        // 搜素条件之 三级分类id
        if ( null != searchParam.getCategory1Id() ){
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery("category1Id",searchParam.getCategory1Id())
            );
        }
        if ( null != searchParam.getCategory2Id() ){
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery("category2Id",searchParam.getCategory2Id())
            );
        }
        if ( null != searchParam.getCategory3Id() ){
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery("category3Id",searchParam.getCategory3Id())
            );
        }
        // 搜素条件之 平台属性信息  props=30:8G:运行内存
        String[] props = searchParam.getProps();
        if ( null != props && props.length>0 ){
            for (String prop : props) {
                String[] split = prop.split(":");
                if ( null != split && split.length == 3 ){
                    BoolQueryBuilder builder = new BoolQueryBuilder();
                    BoolQueryBuilder subBuilder = new BoolQueryBuilder();
                    subBuilder.must(
                            QueryBuilders.termQuery("attrs.attrId",split[0])
                    );
                    subBuilder.must(
                            QueryBuilders.termQuery("attrs.attrValue",split[1])
                    );
                    builder.must(
                            QueryBuilders.nestedQuery(
                                    "attrs",subBuilder, ScoreMode.None
                            )
                    );
                    boolQueryBuilder.filter(builder);
                }
            }
        }
        // 执行查询
        searchSourceBuilder.query(boolQueryBuilder);
        // 排序  1:hotScore 2:price  //order=2:desc
        String order = searchParam.getOrder();
        if (!StringUtils.isEmpty(order)){
            String[] split = StringUtils.split(order, ":");
            if (null!=split&&split.length==2){
                String field = "";
                switch (split[0]){
                    case "1":
                        field = "hotScore";
                        break;
                    case "2":
                        field = "price";
                        break;
                    default:
                        throw new IllegalArgumentException("排序字段order参数异常");
                }
                searchSourceBuilder.sort(field,"desc".equals(split[1])?SortOrder.DESC:SortOrder.ASC);
            }else {
                searchSourceBuilder.sort("hotScore", SortOrder.DESC);
            }
        }
        // 分页
        searchSourceBuilder.from(
                (searchParam.getPageNo()-1)*searchParam.getPageSize()
        );
        searchSourceBuilder.size(searchParam.getPageSize());
        // 高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.postTags("</span>");
        highlightBuilder.preTags("<span style=color:red>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 聚合之 品牌
        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("tmIdAggs").field("tmId").size(10)
                        .subAggregation(AggregationBuilders.terms("tmNameAggs").field("tmName").size(10))
                        .subAggregation(AggregationBuilders.terms("tmLogoUrlAggs").field("tmLogoUrl").size(10))
        );
        // 聚合之 平台属性
        searchSourceBuilder.aggregation(
                AggregationBuilders.nested("attrAgg", "attrs")
                    .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(10)
                    .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName")).size(10)
                    .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue")).size(10)));
        // 处理结果集 _source {"id","defaultImg","title","price" }
        searchSourceBuilder.fetchSource(new String[]{"id","defaultImg","title","price"},null);
        System.out.println("searchSourceBuilder = " + searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);

        // 2、查询数据
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 3、封装查询结果
        SearchResponseVo responseVo = new SearchResponseVo();
        // 获取聚合信息
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        // 获取品牌聚合信息
        ParsedLongTerms tmIdAggs = (ParsedLongTerms) aggregationMap.get("tmIdAggs");
        // 封装品牌list
        List<SearchResponseTmVo> tmVoList = tmIdAggs.getBuckets().stream().map(bucket -> {
            SearchResponseTmVo searchResponseTmVo = new SearchResponseTmVo();
            // 品牌id
            Long id = (Long) bucket.getKey();
            // 品牌name
            ParsedStringTerms tmNameAggs = bucket.getAggregations().get("tmNameAggs");
            String name = (String) tmNameAggs.getBuckets().get(0).getKey();
            // 品牌url
            ParsedStringTerms tmLogoUrlAggs = bucket.getAggregations().get("tmLogoUrlAggs");
            String url = (String) tmLogoUrlAggs.getBuckets().get(0).getKey();
            searchResponseTmVo.setTmId(id);
            searchResponseTmVo.setTmName(name);
            searchResponseTmVo.setTmLogoUrl(url);
            return searchResponseTmVo;
        }).collect(Collectors.toList());
        // 获取平台属性集合
        ParsedNested attrAgg = (ParsedNested) aggregationMap.get("attrAgg");
        //获取平台属性id聚合对象
        ParsedLongTerms attrIdAgg = (ParsedLongTerms) attrAgg.getAggregations().getAsMap().get("attrIdAgg");
        // 封装平台属性list
        List<SearchResponseAttrVo> responseAttrVoList = attrIdAgg.getBuckets().stream().map(bucket -> {
            SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();
            // 平台属性id
            Long id = (Long) bucket.getKey();
            // 平台属性名称
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String name = (String) attrNameAgg.getBuckets().get(0).getKey();
            // 平台属性值list
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
            List<String> attrValueList = attrValueAgg.getBuckets().stream().map(attrValue -> {
                return (String) attrValue.getKey();
            }).collect(Collectors.toList());
            searchResponseAttrVo.setAttrId(id);
            searchResponseAttrVo.setAttrName(name);
            searchResponseAttrVo.setAttrValueList(attrValueList);
            return searchResponseAttrVo;
        }).collect(Collectors.toList());
        // 封装商品信息list
        // 创建集合封装数据
        List<Goods> goodsList = new ArrayList<>();
        // 获取hit信息
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchResponse.getHits().getHits();
        if (null!=hits&&hits.length>0){
            for (SearchHit hit : hits) {
                // 处理 _source
                Goods goods = JSONObject.parseObject(hit.getSourceAsString(), Goods.class);
                // 处理 高亮信息
                if(hit.getHighlightFields().get("title")!=null){
                    goods.setTitle(hit.getHighlightFields().get("title").getFragments()[0].toString());
                }
                goodsList.add(goods);
            }
        }
        // 封装结果集
        responseVo.setTrademarkList(tmVoList);
        responseVo.setGoodsList(goodsList);
        responseVo.setAttrsList(responseAttrVoList);
        // 总记录数
        long total = searchHits.getTotalHits().value;
        responseVo.setTotal(total);
        // 每页记录数
        responseVo.setPageSize(searchParam.getPageSize());
        // 当前页
        responseVo.setPageNo(searchParam.getPageNo());
        // 总页数
        if (total%searchParam.getPageSize()==0){
            responseVo.setTotalPages(total/searchParam.getPageSize());
        }else {
            responseVo.setTotalPages(total/searchParam.getPageSize()+1);
        }

        // 4、响应
        return responseVo;
    }
}
