package space.jachen.gmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.jachen.gmall.domain.payment.PaymentInfo;

/**
 * @author JaChen
 * @date 2023/3/22 18:45
 */
@Mapper
public interface PaymentInfoMapper extends BaseMapper<PaymentInfo> {
}
