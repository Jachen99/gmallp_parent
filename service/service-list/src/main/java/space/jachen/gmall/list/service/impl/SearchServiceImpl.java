package space.jachen.gmall.list.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.list.Goods;
import space.jachen.gmall.domain.list.SearchAttr;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.list.repository.GoodsRepository;
import space.jachen.gmall.list.service.SearchService;
import space.jachen.gmall.product.client.ProductFeignClient;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/3/14 11:48
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    private ProductFeignClient productFeignClient;
    @Autowired
    private GoodsRepository goodsRepository;

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
}
