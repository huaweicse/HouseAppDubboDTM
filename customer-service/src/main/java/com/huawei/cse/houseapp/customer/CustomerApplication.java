package com.huawei.cse.houseapp.customer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml", "classpath*:META-INF/spring/*.xml"})
@EnableConfigurationProperties
public class CustomerApplication {
  public static void main(String[] args) throws Exception {
    try {
      new SpringApplicationBuilder(CustomerApplication.class).web(WebApplicationType.NONE).run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
