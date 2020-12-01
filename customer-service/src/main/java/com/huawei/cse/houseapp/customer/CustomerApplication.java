package com.huawei.cse.houseapp.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml", "classpath*:META-INF/spring/*.xml"})
@EnableConfigurationProperties
public class CustomerApplication {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(CustomerApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
