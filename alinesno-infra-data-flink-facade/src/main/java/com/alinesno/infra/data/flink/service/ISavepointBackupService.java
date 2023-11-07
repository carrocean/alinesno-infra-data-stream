package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.entity.SavepointBackupEntity;
import com.alinesno.infra.data.flink.exchange.dto.SavepointBackupDTO;

import java.util.Date;
import java.util.List;

/**
 * 保存点备份Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface ISavepointBackupService extends IBaseService<SavepointBackupEntity> {

    /**
     * 新增
     *
     * @author zhp
     * @date 2020-09-17
     * @time 20:34
     */
    void insertSavepoint(Long jobConfigId, String savepointPath, Date date, long operatorId);

    /**
     * 获取SavepointPath详细地址
     *
     * @author zhp
     * @date 2020-09-21
     * @time 00:44
     */
    String getSavepointPathById(long jobConfigId, long savepointId);

    /**
     * 最近10条
     *
     * @author zhp
     * @date 2020-09-17
     * @time 20:34
     */
    List<SavepointBackupDTO> lasterHistory10(Long jobConfigId);

    /**
     * 根据任务ID统计保存点数量
     */
    Integer statisticsByJobConfigId(Long jobConfigId);
}
