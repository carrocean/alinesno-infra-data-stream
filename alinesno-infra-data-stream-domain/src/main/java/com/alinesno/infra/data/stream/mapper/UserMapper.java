package com.alinesno.infra.data.stream.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.stream.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * 用户Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface UserMapper extends IBaseMapper<UserEntity> {
}
