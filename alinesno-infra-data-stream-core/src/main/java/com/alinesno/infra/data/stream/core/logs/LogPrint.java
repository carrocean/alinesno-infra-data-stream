package com.alinesno.infra.data.stream.core.logs;

import com.alinesno.infra.data.stream.commom.enums.SqlCommand;
import com.alinesno.infra.data.stream.commom.model.SqlCommandCall;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhp
 * @Description:
 * @date 2021/3/21
 * @time 22:20
 */
public class LogPrint {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(LogPrint.class);

  /**
   * 打印SqlCommandCall 日志信息
   *
   * @author zhp
   * @date 2021/3/21
   * @time 11:25
   */
  public static void logPrint(SqlCommandCall sqlCommandCall) {
    if (sqlCommandCall == null) {
      throw new NullPointerException("sqlCommandCall is null");
    }
    switch (sqlCommandCall.getSqlCommand()) {
      case SET:
        log.info("\n############# {} ############# \nSET{}={}",
            sqlCommandCall.getSqlCommand().name(), sqlCommandCall.getOperands()[0],
            sqlCommandCall.getOperands()[1]);
        break;
      default:
        log.info("\n############# {} ############# \n {}", sqlCommandCall.getSqlCommand().name(),
            sqlCommandCall.getOperands()[0]);
    }
  }

  /**
   * show 语句  select语句结果打印
   *
   * @author zhp
   * @date 2021/3/21
   * @time 11:23
   */
  public static void queryRestPrint(TableEnvironment tEnv, SqlCommandCall sqlCommandCall) {
    if (sqlCommandCall == null) {
      throw new NullPointerException("sqlCommandCall is null");
    }
    LogPrint.logPrint(sqlCommandCall);

    if (sqlCommandCall.getSqlCommand().name().equalsIgnoreCase(SqlCommand.SELECT.name())) {
      throw new RuntimeException("目前不支持select 语法使用");
    } else {
      tEnv.executeSql(sqlCommandCall.getOperands()[0]).print();
    }

//        if (sqlCommandCall.getSqlCommand().name().equalsIgnoreCase(SqlCommand.SELECT.name())) {
//            Iterator<Row> it = tEnv.executeSql(sqlCommandCall.operands[0]).collect();
//            while (it.hasNext()) {
//                String res = String.join(",", PrintUtils.rowToString(it.next()));
//                log.info("数据结果 {}", res);
//            }
//        }
  }

}
