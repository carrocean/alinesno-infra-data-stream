package com.alinesno.infra.data.stream.exchange.to;



import java.util.List;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-06
 * @time 02:04
 */

public class AppListTO {

  private List<AppTO> app;

  public List<AppTO> getApp() {
    return app;
  }

  public void setApp(List<AppTO> app) {
    this.app = app;
  }
}
