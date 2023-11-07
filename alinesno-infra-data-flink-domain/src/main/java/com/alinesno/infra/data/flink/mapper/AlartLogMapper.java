package com.alinesno.infra.data.flink.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.flink.entity.AlartLogEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 告警日志Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

@Repository
public interface AlartLogMapper extends IBaseMapper<AlartLogEntity> {

    int deleteByJobConfigId(@Param("jobConfigId") Long jobConfigId);
}
