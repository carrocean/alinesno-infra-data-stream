package com.alinesno.infra.data.flink.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.flink.entity.SavepointBackupEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 保存点备份Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface SavepointBackupMapper extends IBaseMapper<SavepointBackupEntity> {

    SavepointBackupEntity getSavepointBackupById(@Param("jobConfigId") long jobConfigId, @Param("id") long savepointId);

    List<SavepointBackupEntity> selectByLimt10(@Param("jobConfigId") long jobConfigId);

    Integer statisticsByJobConfigId(@Param("jobConfigId") long jobConfigId);
}
