package com.alinesno.infra.data.stream.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.stream.entity.SavepointBackupEntity;
import com.alinesno.infra.data.stream.exchange.dto.SavepointBackupDTO;
import com.alinesno.infra.data.stream.mapper.SavepointBackupMapper;
import com.alinesno.infra.data.stream.service.ISavepointBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 保存点备份Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class SavepointBackupServiceImpl extends IBaseServiceImpl<SavepointBackupEntity, SavepointBackupMapper> implements ISavepointBackupService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SavepointBackupServiceImpl.class);

    @Autowired
    private SavepointBackupMapper savepointBackupMapper;

    @Override
    public void insertSavepoint(Long jobConfigId, String savepointPath, Date date, long operatorId) {
        SavepointBackupEntity savepointBackup = new SavepointBackupEntity();
        savepointBackup.setBackupTime(date);
        savepointBackup.setSavepointPath(savepointPath);
        savepointBackup.setJobConfigId(jobConfigId);
        savepointBackup.setOperatorId(operatorId);
        savepointBackupMapper.insert(savepointBackup);
    }

    @Override
    public String getSavepointPathById(long jobConfigId, long savepointId) {
        SavepointBackupEntity savepointBackup = savepointBackupMapper.getSavepointBackupById(jobConfigId, savepointId);
        if (savepointBackup != null) {
            return savepointBackup.getSavepointPath();
        }
        return null;
    }

    @Override
    public List<SavepointBackupDTO> lasterHistory10(Long jobConfigId) {
        return SavepointBackupDTO.toDTOList(savepointBackupMapper.selectByLimt10(jobConfigId));
    }

    @Override
    public Integer statisticsByJobConfigId(Long jobConfigId){
        return savepointBackupMapper.statisticsByJobConfigId(jobConfigId) ;
    };


}
