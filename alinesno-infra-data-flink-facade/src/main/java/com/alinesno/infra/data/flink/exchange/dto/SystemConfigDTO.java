package com.alinesno.infra.data.flink.exchange.dto;

import com.alinesno.infra.data.flink.entity.SystemConfigEntity;
import com.alinesno.infra.data.flink.exchange.enums.SysConfigEnumType;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhp
 * @date 2020-07-20
 * @time 23:37
 */
public class SystemConfigDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String key;

  private String val;

  private SysConfigEnumType sysConfigEnumType;


  public SystemConfigDTO() {

  }

  public SystemConfigDTO(String key, String val) {
    this.key = key;
    this.val = val;
  }

  public static SystemConfigEntity toEntity(SystemConfigDTO systemConfigDTO) {
    if (systemConfigDTO == null) {
      return null;
    }
    SystemConfigEntity systemConfig = new SystemConfigEntity();
    systemConfig.setId(systemConfigDTO.getId());
    systemConfig.setSysKey(systemConfigDTO.getKey());
    systemConfig.setSysVal(systemConfigDTO.getVal());
    return systemConfig;
  }

  public static SystemConfigDTO toDTO(SystemConfigEntity systemConfig) {
    if (systemConfig == null) {
      return null;
    }
    SystemConfigDTO systemConfigDTO = new SystemConfigDTO();
    systemConfigDTO.setId(systemConfig.getId());
    systemConfigDTO.setKey(systemConfig.getSysKey());
    systemConfigDTO.setVal(systemConfig.getSysVal());
    return systemConfigDTO;
  }

  public static List<SystemConfigDTO> toListDTO(List<SystemConfigEntity> systemConfigList) {
    if (CollectionUtils.isEmpty(systemConfigList)) {
      return Collections.emptyList();
    }
    List<SystemConfigDTO> list = new ArrayList();
    for (SystemConfigEntity systemConfig : systemConfigList) {
      list.add(toDTO(systemConfig));
    }
    return list;
  }

  public static Map<String, String> toMap(List<SystemConfigDTO> systemConfigDTOList) {
    if (CollectionUtils.isEmpty(systemConfigDTOList)) {
      return Collections.EMPTY_MAP;
    }

    return systemConfigDTOList.stream().collect(Collectors.toMap(SystemConfigDTO::getKey,
        SystemConfigDTO::getVal, (key1, key2) -> key2));

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = val;
  }

  public SysConfigEnumType getSysConfigEnumType() {
    return sysConfigEnumType;
  }

  public void setSysConfigEnumType(SysConfigEnumType sysConfigEnumType) {
    this.sysConfigEnumType = sysConfigEnumType;
  }
}
