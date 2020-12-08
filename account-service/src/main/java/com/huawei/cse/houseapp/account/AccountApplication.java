package com.huawei.cse.houseapp.account;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml", "classpath*:META-INF/spring/*.xml"})
public class AccountApplication {
  public static void main(String[] args) throws Exception {
    try {
      new SpringApplicationBuilder(AccountApplication.class).web(WebApplicationType.NONE).run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
