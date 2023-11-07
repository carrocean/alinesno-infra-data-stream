package com.alinesno.infra.data.flink.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.flink.entity.SystemConfigEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统配置Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface SystemConfigMapper extends IBaseMapper<SystemConfigEntity> {


    List<SystemConfigEntity> selectAllConfig(String sysType);


    SystemConfigEntity selectConfigByKey(String sysKey);

    int updateByKey(SystemConfigEntity systemConfig);
}
