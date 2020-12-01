package com.huawei.cse.houseapp.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml", "classpath*:META-INF/spring/*.xml"})
public class UserApplication {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(UserApplication.class, args);
  }
}
