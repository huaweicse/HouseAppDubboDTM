package com.huawei.cse.houseapp.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApplication {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(ProductApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
