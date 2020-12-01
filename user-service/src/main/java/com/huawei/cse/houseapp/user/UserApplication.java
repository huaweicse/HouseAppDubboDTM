package com.huawei.cse.houseapp.user;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class UserApplication {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(UserApplication.class, args);
  }
}
