package space.jachen.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.common.cache.GmallCache;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.product.BaseCategory1;
import space.jachen.gmall.domain.product.BaseCategory2;
import space.jachen.gmall.domain.product.BaseCategory3;
import space.jachen.gmall.product.mapper.BaseCategoryMapper;
import space.jachen.gmall.product.mapper.BaseCategoryViewMapper;
import space.jachen.gmall.product.service.BaseCategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 一级分类表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
@Service
public class BaseCategoryServiceImpl implements BaseCategoryService {

    @Autowired
    private BaseCategoryMapper baseCategoryMapper;
    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;

    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategoryMapper.getCategory1();
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        return baseCategoryMapper.getCategory2(category1Id);
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        return baseCategoryMapper.getCategory3(category2Id);
    }

    @Override
    @GmallCache(front = "baseCategoryView:")
    public BaseCategoryView getCategoryView(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    @Override
    public List<JSONObject> getBaseCategoryList() {

        // 获取所有分类数据list
        List<BaseCategoryView> categoryViewList = baseCategoryViewMapper.selectList(null);
        // 创建结果集
        List<JSONObject> resultList = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);
        // 根据 category1Id 获取分组集合数据
        Map<Long, List<BaseCategoryView>> category1Map = categoryViewList.stream()
                .collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        category1Map.forEach((category1Key, category1Value) -> {
            JSONObject category1 = new JSONObject();
            category1.put("index",index.getAndIncrement());
            category1.put("categoryId",category1Key);
            category1.put("categoryName",category1Value.get(0).getCategory1Name());
            // 根据 category2Id 获取分组集合数据
            Map<Long, List<BaseCategoryView>> category2Map = category1Value.stream()
                    .collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            category2Map.forEach((category2Key,category2Value)->{
                JSONObject category2 = new JSONObject();
                category2.put("categoryId",category2Key);
                category2.put("categoryName",category2Value.get(0).getCategory2Name());
                JSONObject category3 = new JSONObject();
                category3.put("categoryId",category2Value.get(0).getCategory3Id());
                category3.put("categoryName",category2Value.get(0).getCategory3Name());
                // 存入category3
                category2.put("categoryChild",category3);
                // 存入category2
                category1.put("categoryChild",category2);
            });
            // 存入结果集
            resultList.add(category1);
        });
        return resultList;
    }
}
