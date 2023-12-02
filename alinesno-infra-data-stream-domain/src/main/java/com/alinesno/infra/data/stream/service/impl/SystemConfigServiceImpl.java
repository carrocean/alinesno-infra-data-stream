package com.alinesno.infra.data.stream.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.stream.commom.constant.SystemConstant;
import com.alinesno.infra.data.stream.entity.SystemConfigEntity;
import com.alinesno.infra.data.stream.exchange.common.FlinkYarnRestUriConstants;
import com.alinesno.infra.data.stream.exchange.common.SystemConstants;
import com.alinesno.infra.data.stream.exchange.common.util.FileUtils;
import com.alinesno.infra.data.stream.exchange.common.util.HttpServiceCheckerUtil;
import com.alinesno.infra.data.stream.exchange.common.util.HttpUtil;
import com.alinesno.infra.data.stream.exchange.config.LocalCache;
import com.alinesno.infra.data.stream.exchange.dto.SystemConfigDTO;
import com.alinesno.infra.data.stream.exchange.enums.DeployModeEnum;
import com.alinesno.infra.data.stream.exchange.enums.SysConfigEnum;
import com.alinesno.infra.data.stream.exchange.enums.SysConfigEnumType;
import com.alinesno.infra.data.stream.exchange.enums.SysErrorEnum;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.mapper.SystemConfigMapper;
import com.alinesno.infra.data.stream.service.ISystemConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class SystemConfigServiceImpl extends IBaseServiceImpl<SystemConfigEntity, SystemConfigMapper> implements ISystemConfigService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SystemConfigServiceImpl.class);
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private LocalCache localCache;

    /**
     * 初始化代码仓库
     */
    @Override
    public void initAccountSystemConfig(Long operatorId) {

        List<SystemConfigEntity> sysConfList = new ArrayList<SystemConfigEntity>() ;

        //Flink客户端目录
        SystemConfigEntity clientDir = new SystemConfigEntity();
        clientDir.setSysDesc("flink安装目录");
        clientDir.setSysKey("flink_home");
        clientDir.setSysType("SYS");
        clientDir.setOperatorId(operatorId);
        clientDir.setOrderId(1);
        sysConfList.add(clientDir) ;

        //Flink管理平台目录
        SystemConfigEntity manageDir = new SystemConfigEntity();
        manageDir.setSysDesc("数据实时计算安装目录");
        manageDir.setSysKey("flink_pipeline_home");
        manageDir.setSysType("SYS");
        manageDir.setOperatorId(operatorId);
        manageDir.setOrderId(2);
        sysConfList.add(manageDir) ;


        //自动开启savepoint
        SystemConfigEntity savePoint = new SystemConfigEntity();
        savePoint.setSysDesc("是否开启保存点");
        savePoint.setSysKey("auto_savepoint");
        savePoint.setSysType("SYS");
        savePoint.setSysVal("是");
        savePoint.setOperatorId(operatorId);
        savePoint.setOrderId(3);
        sysConfList.add(savePoint) ;

        //Yarn RM Http地址
        SystemConfigEntity Yarn = new SystemConfigEntity();
        Yarn.setSysDesc("Yarn RM Http地址");
        Yarn.setSysKey("yarn_rm_http_address");
        Yarn.setSysType("SYS");
        Yarn.setOperatorId(operatorId);
        Yarn.setOrderId(4);
        sysConfList.add(Yarn) ;

        //Flink服务HTTP地址
        SystemConfigEntity flinkHttp = new SystemConfigEntity();
        flinkHttp.setSysDesc("Flink服务HTTP地址");
        flinkHttp.setSysKey("flink_rest_http_address");
        flinkHttp.setSysType("SYS");
        flinkHttp.setOperatorId(operatorId);
        flinkHttp.setOrderId(5);
        sysConfList.add(flinkHttp) ;

        //Flink HA服务HTTP地址
        SystemConfigEntity flinkHAHttp = new SystemConfigEntity();
        flinkHAHttp.setSysDesc("Flink HA服务HTTP地址");
        flinkHAHttp.setSysKey("flink_rest_ha_http_address");
        flinkHAHttp.setSysType("SYS");
        flinkHAHttp.setOperatorId(operatorId);
        flinkHAHttp.setOrderId(6);
        sysConfList.add(flinkHAHttp) ;

        //钉钉告警
        SystemConfigEntity dingTalk = new SystemConfigEntity();
        dingTalk.setSysDesc("钉钉告警");
        dingTalk.setSysKey("dingding_alart_url");
        dingTalk.setSysType("ALART");
        dingTalk.setOperatorId(operatorId);
        dingTalk.setOrderId(7);
        sysConfList.add(dingTalk) ;

        //自定义回调
        SystemConfigEntity callback = new SystemConfigEntity();
        callback.setSysDesc("自定义回调");
        callback.setSysKey("callback_alart_url");
        callback.setSysType("ALART");
        callback.setOperatorId(operatorId);
        callback.setOrderId(8);
        sysConfList.add(callback) ;

        this.saveOrUpdateBatch(sysConfList);
    }

    @Override
    public List<SystemConfigDTO> getSystemConfig(SysConfigEnumType sysConfigEnumType) {
        return SystemConfigDTO.toListDTO(systemConfigMapper.selectAllConfig( sysConfigEnumType != null ? sysConfigEnumType.name() : null));
    }

    @Override
    public String getSystemConfigByKey(String key) {
        List<SystemConfigDTO> list = this.getSystemConfig(null);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return SystemConfigDTO.toMap(list).get(key);
    }

    @Override
    public String getYarnRmHttpAddress() {
        String urlHa = this.getSystemConfigByKey(SysConfigEnum.YARN_RM_HTTP_ADDRESS.getKey());
        if (StringUtils.isEmpty(urlHa)) {
            throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL_YARN_RM_HTTP_ADDRESS);
        }

        return getActiveYarnUrl(urlHa);


    }

    @Override
    public String getFlinkAddress(DeployModeEnum deployModeEnum) {
        try {
            String url = this.getFlinkHttpAddress(deployModeEnum);
            URL address = new URL(url);
            String host = address.getHost();
            Integer port = address.getPort() == -1 ? 80 : address.getPort();
            return host + ":" + port;
        } catch (Exception e) {
            log.error("getFlinkAddress is error", e);
        }
        return null;
    }

    @Override
    public String getFlinkHttpAddress(DeployModeEnum deployModeEnum) {

        switch (deployModeEnum) {
            case LOCAL:
                String urlLocal = this.getSystemConfigByKey(SysConfigEnum.FLINK_REST_HTTP_ADDRESS.getKey());
                if (StringUtils.isEmpty(urlLocal)) {
                    throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL_FLINK_REST_HTTP_ADDRESS);
                }
                if (HttpServiceCheckerUtil.checkUrlConnect(urlLocal)) {
                    return urlLocal.trim();
                }
                throw new BizException("网络异常 url=" + urlLocal);
            case STANDALONE:
                String urlHA = this.getSystemConfigByKey(SysConfigEnum.FLINK_REST_HA_HTTP_ADDRESS.getKey());
                if (StringUtils.isEmpty(urlHA)) {
                    throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL_FLINK_REST_HA_HTTP_ADDRESS);
                }
                String[] urls = urlHA.split(SystemConstant.SEMICOLON);
                for (String http : urls) {
                    if (HttpServiceCheckerUtil.checkUrlConnect(http)) {
                        return http.trim();
                    }
                }
                throw new BizException("网络异常 url=" + urlHA);
            default:
                throw new BizException("不支持该模式");
        }


    }

    @Override
    public String getFlinkUrl(DeployModeEnum deployModeEnum) {
        String url = localCache.get(deployModeEnum.name());
        if (StringUtils.isNotEmpty(url)) {
            return url;
        }
        try {
            switch (deployModeEnum) {
                case LOCAL:
                case STANDALONE:
                    url = getFlinkHttpAddress(deployModeEnum);
                    break;
                case YARN_APPLICATION:
                case YARN_PER:
                    url = getYarnRmHttpAddress();
                    break;
                default:
                    throw new BizException("不支持该模式=" + deployModeEnum.name());
            }
            localCache.put(deployModeEnum.name(), url);
        } catch (Exception e) {
            log.error("getFlinkUrl is error", e);
            localCache.put(deployModeEnum.name(), "null");
        }
        return url;
    }

    @Override
    public String getUploadJarsPath() {
        String path = this.getSystemConfigByKey(SysConfigEnum.FLINK_STREAMING_PLATFORM_WEB_HOME.getKey());
        if (StringUtils.isEmpty(path)) {
            throw new BizException("请先去系统设置界面设置Flink管理平台目录(即flink_streaming_platform_web)");
        }
        return path + SystemConstant.VIRGULE + SystemConstant.JAR_ROOT_PATH;
    }

    @Override
    public boolean isExist(String key) {
        String value = this.getSystemConfigByKey(key);
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean autoSavepoint() {
        String value = this.getSystemConfigByKey(SysConfigEnum.AUTO_SAVEPOINT.getKey());
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        log.info("autoSavepoint ={}", value);
        return Boolean.parseBoolean(value);
    }


    private String getActiveYarnUrl(String urlHa) {
        String[] urls = urlHa.split(SystemConstant.SEMICOLON);
        for (String http : urls) {
            try {
                String url = HttpUtil.buildUrl(http, FlinkYarnRestUriConstants.URI_YARN_INFO);
                String request = cn.hutool.http.HttpUtil.get(url, HttpServiceCheckerUtil.TIMEOUTMILLSECONDS);
                if (StringUtils.isNotEmpty(request)) {
                    JSONObject jsonObject = (JSONObject) JSON.parse(request);
                    String haState = jsonObject.getJSONObject("clusterInfo").get("haState").toString();
                    if ("ACTIVE".equalsIgnoreCase(haState)) {
                        return http;
                    }
                }
            } catch (Exception e) {
                log.error("单个http异常={}", http, e);
            }
        }
        throw new BizException("网络异常 url=" + urlHa);
    }

    @Override
    public void addOrUpdateConfigByKey(String key, String value) {

        this.checkParam(key, value);
        if (SysConfigEnum.FLINK_HOME.equals(SysConfigEnum.getSysConfigEnum(key))) {
            FileUtils.createSqlHome(value);
        }
        SystemConfigEntity systemConfig = systemConfigMapper.selectConfigByKey(key);
        if (systemConfig == null) {
            systemConfigMapper.insert(new SystemConfigEntity(key, value.trim()));
        } else {
            systemConfigMapper.updateByKey(new SystemConfigEntity(key, value.trim()));
        }

    }

    private void checkParam(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            throw new BizException(SysErrorEnum.PARAM_IS_NULL);
        }
        SysConfigEnum sysConfigEnum = SysConfigEnum.getSysConfigEnum(key);

        if (SysConfigEnum.YARN_RM_HTTP_ADDRESS.equals(sysConfigEnum)
                || SysConfigEnum.FLINK_REST_HTTP_ADDRESS.equals(sysConfigEnum)
                || SysConfigEnum.FLINK_REST_HA_HTTP_ADDRESS.equals(sysConfigEnum)) {
            if (!StrUtil.endWith(value, SystemConstants.SLASH)) {
                throw new BizException("必须以/结尾");
            }
            if (!StrUtil.startWith(value, SystemConstants.HTTP_KEY)) {
                throw new BizException("必须以http或者https开头");
            }
        }
        if (SysConfigEnum.DINGDING_ALARM_URL.equals(sysConfigEnum)) {
            if (!StrUtil.startWith(value, SystemConstants.HTTP_KEY)) {
                throw new BizException("必须以http或者https开头");
            }
        }

        this.checkUrlValid(sysConfigEnum, value);

        if (SysConfigEnum.FLINK_HOME.equals(sysConfigEnum)) {
            if (!StrUtil.endWith(value, SystemConstants.SLASH)) {
                throw new BizException("必须以/结尾");
            }
            if (!StrUtil.startWith(value, SystemConstants.SLASH)) {
                throw new BizException("必须以/开头");
            }
        }
        if (SysConfigEnum.FLINK_STREAMING_PLATFORM_WEB_HOME.equals(sysConfigEnum)) {
            if (!StrUtil.startWith(value, SystemConstants.SLASH)) {
                throw new BizException("必须以/开头");
            }
            if (!StrUtil.endWith(value, SystemConstants.SLASH)) {
                throw new BizException("必须以/结尾");
            }

        }
    }


    private void checkUrlValid(SysConfigEnum sysConfigEnum, String url) {
        switch (sysConfigEnum) {
            case FLINK_REST_HTTP_ADDRESS:
            case DINGDING_ALARM_URL:
                if (!HttpServiceCheckerUtil.checkUrlConnect(url)) {
                    throw new BizException("网络异常 url=" + url);
                }
                break;
            case YARN_RM_HTTP_ADDRESS:
            case FLINK_REST_HA_HTTP_ADDRESS:
                String[] urls = url.split(SystemConstant.SEMICOLON);
                for (String http : urls) {
                    if (!HttpServiceCheckerUtil.checkUrlConnect(http)) {
                        throw new BizException("网络异常 url=" + http);
                    }
                }
                break;
            default:
                break;
        }

    }
}
