package com.alinesno.infra.data.flink.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.flink.entity.UserEntity;
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
