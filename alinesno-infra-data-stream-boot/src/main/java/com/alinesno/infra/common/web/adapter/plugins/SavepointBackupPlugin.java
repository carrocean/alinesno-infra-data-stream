package com.alinesno.infra.common.web.adapter.plugins;

import com.alinesno.infra.data.stream.entity.JobConfigEntity;
import com.alinesno.infra.data.stream.service.IJobConfigService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 保存点转换插件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */
@Component("SavepointBackupPlugin")
public class SavepointBackupPlugin implements TranslatePlugin {
    @Autowired
    private IJobConfigService jobConfigService;

    private final String jOB_CONFIG_ID   = "jobConfigId";
    private final String jOB_CONFIG_NAME = "jobConfigName";

    @Override
    public void translate(ArrayNode node, TranslateCode convertCode) {

        if (!node.isEmpty()) {
            //获取任务列表
            List<String> jobConfigIds = this.extractIds(node, jOB_CONFIG_ID);
            List<JobConfigEntity> jobConfiglist = this.jobConfigService.findByIds(jobConfigIds);
            Map<String, JobConfigEntity> jobConfigMap = this.toEntityMap(jobConfiglist);


            //转换逻辑
            node.forEach(jsonObject -> {
                //从返回的列表中获取任务id
                String jobConfigId = jsonObject.get(jOB_CONFIG_ID).asText();
                ObjectNode rootNode = (ObjectNode) jsonObject;

                //从查找的列表里拿到对应的任务
                JobConfigEntity jobConfig = jobConfigMap.get(jobConfigId);
                if (jobConfig != null) {
                    //设置返回值
                    rootNode.put(jOB_CONFIG_NAME + LABEL_SUFFER, jobConfig.getJobName());
                }

            });
        }
    }
}
