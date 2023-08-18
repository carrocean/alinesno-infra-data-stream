package com.alinesno.infra.data.flink.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;

/**
 * 功能名：上传文件管理
 * 数据表：upload_file
 * 表备注：上传文件管理
 * 
 * 此类表示上传文件管理实体。
 * 
 * 字段说明：
 * - fileName: 文件名字
 * - filePath: 文件路径
 * - type: 类型，1表示jar
 * 
 * 该实体类继承自InfraBaseEntity，使用MyBatis-Plus提供的注解进行字段映射。
 * 
 * 注意：由于没有提供字段的中文注释信息，以下字段注释仅供参考。
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("upload_file")
public class UploadFileEntity extends InfraBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件名字
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 类型，1表示jar
     */
    @TableField("type")
    private Integer type;

    // getter and setter methods

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
