package space.jachen.gmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.jachen.gmall.domain.order.OrderDetail;

/**
 * @author JaChen
 * @date 2023/3/20 16:49
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}