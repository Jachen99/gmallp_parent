package space.jachen.gmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import space.jachen.gmall.domain.order.OrderInfo;

/**
 * @author JaChen
 * @date 2023/3/20 16:48
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    IPage<OrderInfo> selectPageByUserId(Page<OrderInfo> pageParam, @Param("userId")String userId);
}
