package space.jachen.gmall.list.service;

/**
 * @author JaChen
 * @date 2023/3/14 11:47
 */
public interface SearchService {

    /**
     * 商品上架
     * @param skuId skuId
     */
    void upperGoods(Long skuId);

    /**
     * 商品下架
     * @param skuId   skuId
     */
    void lowerGoods(Long skuId);
}
