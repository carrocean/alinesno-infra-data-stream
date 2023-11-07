package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.exchange.dto.SystemConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.DeployModeEnum;
import com.alinesno.infra.data.flink.exchange.enums.SysConfigEnumType;
import com.alinesno.infra.data.flink.entity.SystemConfigEntity;
import java.util.List;

/**
 * 系统配置Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface ISystemConfigService extends IBaseService<SystemConfigEntity> {
    /**
     * 初始化用户的系统配置
     *
     */
    void initAccountSystemConfig(Long operatorId);

    /**
     * 查询配置
     *
     * @author zhp
     * @date 2020-07-20
     * @time 01:11
     */
    List<SystemConfigDTO> getSystemConfig(SysConfigEnumType sysConfigEnumType);

    /**
     * 根据key获取配置的值
     *
     * @author zhp
     * @date 2020-08-06
     * @time 20:21
     */
    String getSystemConfigByKey(String key);


    /**
     * 获取yarn的rm Http地址
     *
     * @author zhp
     * @date 2020-09-18
     * @time 01:23
     */
    String getYarnRmHttpAddress();


    /*
     *
     * @Param:[deployModeEnum]
     * @return: java.lang.String
     * @Author: zhp
     * @date 2022/10/11
     */
    String getFlinkAddress(DeployModeEnum deployModeEnum);


    /**
     * 获取flink地址
     *
     * @author zhp
     * @since 1.0.0
     * @time 10:52
     */
    String getFlinkHttpAddress(DeployModeEnum deployModeEnum);

    /*
     *获取统一地址
     * @Param:[deployModeEnum]
     * @return: java.lang.String
     * @Author: zhp
     * @since 1.0.0
     */
    String getFlinkUrl(DeployModeEnum deployModeEnum);

    /*
     *上传jar的目录地址
     * @Param:[]
     * @return: java.lang.String
     * @Author: zhp
     * @date 2022/10/8
     */
    String getUploadJarsPath();


    /**
     * 检查配置是否存在
     *
     * @author zhp
     * @date 2020-10-14
     * @time 21:41
     */
    boolean isExist(String key);


    /**
     * 是否自动开启savepoint （默认是true）
     *
     * @return
     */
    boolean autoSavepoint();

    /**
     * 新增或者修改配置
     *
     * @author zhp
     * @date 2020-08-06
     * @time 23:48
     */
    void addOrUpdateConfigByKey(String key, String value);
}
