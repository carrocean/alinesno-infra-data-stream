package com.alinesno.infra.data.stream.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.stream.entity.IpStatusEntity;
import org.springframework.stereotype.Repository;

/**
 * IP状态Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface IpStatusMapper extends IBaseMapper<IpStatusEntity> {
}
