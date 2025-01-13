package com.alinesno.infra.data.stream.config;

import com.alinesno.infra.common.facade.enable.EnableActable;
import com.alinesno.infra.common.web.adapter.sso.enable.EnableInfraSsoApi;
import com.dtflys.forest.springboot.annotation.ForestScan;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 */
@Slf4j
@EnableActable
@EnableInfraSsoApi
@MapperScan("com.alinesno.infra.data.stream.mapper")
@ForestScan({
        "com.alinesno.infra.common.web.adapter.base.consumer"
})
@Configuration
public class AppConfiguration implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.debug("AppConfiguration.run");
    }
}
