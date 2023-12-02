package com.alinesno.infra.data.stream.core;


import com.alinesno.infra.data.stream.commom.constant.SystemConstant;
import com.alinesno.infra.data.stream.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.stream.commom.sql.SqlFileParser;
import com.alinesno.infra.data.stream.commom.utils.UrlUtils;
import com.alinesno.infra.data.stream.core.checkpoint.CheckPointParams;
import com.alinesno.infra.data.stream.core.checkpoint.FsCheckPoint;
import com.alinesno.infra.data.stream.core.execute.ExecuteSql;
import com.alinesno.infra.data.stream.core.model.JobRunParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhp
 * @Description:
 * @date 2020-06-23
 * @time 00:33
 */
public class JobApplication {

  private static final Logger LOG = LoggerFactory.getLogger(JobApplication.class);

  public static void main(String[] args) {

    try {
      Arrays.stream(args).forEach(arg -> LOG.info("{}", arg));

      JobRunParam jobRunParam = buildParam(args);

      List<String> fileList = null;

      if (UrlUtils.isHttpsOrHttp(jobRunParam.getSqlPath())) {
        fileList = UrlUtils.getSqlList(jobRunParam.getSqlPath());
      } else {
        fileList = Files.readAllLines(Paths.get(jobRunParam.getSqlPath()));
      }

      List<String> sqlList = SqlFileParser.parserSql(fileList);

      //20230628
      LOG.info("待执行的sqlList:{}",sqlList);

      EnvironmentSettings settings = null;

      TableEnvironment tEnv = null;

      if (jobRunParam.getJobTypeEnum() != null
          && JobTypeEnum.SQL_BATCH.equals(jobRunParam.getJobTypeEnum())) {
        LOG.info("[SQL_BATCH] job start......");
        //批处理
        settings = EnvironmentSettings.newInstance()
            .inBatchMode()
            .build();
        tEnv = TableEnvironment.create(settings);
      } else {
        LOG.info("[SQL_STREAMING] job start......");
        //默认是流 流处理 目的是兼容之前版本
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        settings = EnvironmentSettings.newInstance()
            .inStreamingMode()
            .build();
        tEnv = StreamTableEnvironment.create(env, settings);

        //设置checkPoint
        FsCheckPoint.setCheckpoint(env, jobRunParam.getCheckPointParam());

      }

      JobID jobID = ExecuteSql.exeSql(sqlList, tEnv);

      LOG.info(SystemConstant.QUERY_JOBID_KEY_WORD + "{}", jobID);

    } catch (Exception e) {
      LOG.error("job fail:", e);
    }


  }


  private static JobRunParam buildParam(String[] args) throws Exception {
    ParameterTool parameterTool = ParameterTool.fromArgs(args);
    String sqlPath = parameterTool.get("sql");
    if (StringUtils.isEmpty(sqlPath)) {
      throw new NullPointerException("-sql参数 不能为空");
    }
    JobRunParam jobRunParam = new JobRunParam();
    jobRunParam.setSqlPath(sqlPath);
    jobRunParam.setCheckPointParam(CheckPointParams.buildCheckPointParam(parameterTool));
    String type = parameterTool.get("type");
    if (StringUtils.isNotEmpty(type)) {
      jobRunParam.setJobTypeEnum(JobTypeEnum.getJobTypeEnum(Integer.valueOf(type)));
    }
    return jobRunParam;
  }

}
