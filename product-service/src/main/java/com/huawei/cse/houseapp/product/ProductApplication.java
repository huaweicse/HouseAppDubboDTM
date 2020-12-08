package com.huawei.cse.houseapp.product;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml", "classpath*:META-INF/spring/*.xml"})
public class ProductApplication {
  public static void main(String[] args) throws Exception {
    try {
      new SpringApplicationBuilder(ProductApplication.class).web(WebApplicationType.NONE).run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
