package com.alinesno.infra.data.stream.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhp
 * @Description:
 * @date 2022/10/29
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchJob {
    @ColumnType(length = 11)
    @ColumnComment("")
    @TableField("id")
    private Long id;

    /**
     * 任务名称
     */
    @ColumnType(length = 50)
    @ColumnComment("任务名称")
    @TableField("job_name")
    private String jobName;

    /**
     * cron表达式
     */
    @ColumnType(length = 100)
    @ColumnComment("cron表达式")
    @TableField("cron")
    private String cron;
}
