package space.jachen.gmall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.jachen.gmall.domain.user.UserInfo;

/**
 * @author JaChen
 * @date 2023/3/17 16:20
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
