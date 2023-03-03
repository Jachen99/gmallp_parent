package space.jachen.gmall.product.service;

import space.jachen.gmall.product.entity.BaseCategory3;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 三级分类表 服务类
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
public interface BaseCategory3Service extends IService<BaseCategory3> {


    /**
     * 根据三级分类Id集合查询
     *
     * @return  List<BaseCategory3>
     */
    List<BaseCategory3> findBaseCategory3ByCategory3IdList();
}
