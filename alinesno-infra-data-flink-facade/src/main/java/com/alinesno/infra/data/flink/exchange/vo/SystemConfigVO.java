package com.alinesno.infra.data.flink.exchange.vo;


import com.alinesno.infra.data.flink.exchange.dto.SystemConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.SysConfigEnum;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhp
 * @date 2020-07-20
 * @time 23:37
 */
public class SystemConfigVO {

  private Long id;

  private String desc;

  private String key;

  private String val;


  public static SystemConfigVO toVO(SystemConfigDTO systemConfigDTO) {
    if (systemConfigDTO == null) {
      return null;
    }
    SystemConfigVO systemConfigVO = new SystemConfigVO();
    systemConfigVO.setId(systemConfigDTO.getId());
    if (SysConfigEnum.getSysConfigEnum(systemConfigDTO.getKey()) != null) {
      systemConfigVO.setDesc(SysConfigEnum.getSysConfigEnum(systemConfigDTO.getKey()).getDesc());
    }
    systemConfigVO.setKey(systemConfigDTO.getKey());
    systemConfigVO.setVal(systemConfigDTO.getVal());
    return systemConfigVO;
  }

  public static List<SystemConfigVO> toListVO(List<SystemConfigDTO> systemConfigDTOList) {
    if (CollectionUtils.isEmpty(systemConfigDTOList)) {
      return Collections.emptyList();
    }
    List<SystemConfigVO> list = new ArrayList<>();
    for (SystemConfigDTO systemConfigDTO : systemConfigDTOList) {
      list.add(toVO(systemConfigDTO));
    }
    return list;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
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
}
