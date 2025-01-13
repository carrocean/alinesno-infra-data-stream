package com.alinesno.infra.data.stream.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能名：上传文件
 * 数据表：upload_file
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("upload_file")
public class UploadFileEntity extends InfraBaseEntity {

    // 文件名字
    @TableField("file_name")
	@ColumnType(length=128)
	@ColumnComment("文件名字")
    private String fileName;

    // 文件路径
    @TableField("file_path")
	@ColumnType(length=512)
	@ColumnComment("文件路径")
    private String filePath;

    // 1:jar
    @TableField("`type`")
	@ColumnType(MySqlTypeConstant.INT)
	@ColumnComment("1:jar")
    private Integer type;
}
