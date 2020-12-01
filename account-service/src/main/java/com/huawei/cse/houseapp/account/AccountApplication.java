package com.huawei.cse.houseapp.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml", "classpath*:META-INF/spring/*.xml"})
public class AccountApplication {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(AccountApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
