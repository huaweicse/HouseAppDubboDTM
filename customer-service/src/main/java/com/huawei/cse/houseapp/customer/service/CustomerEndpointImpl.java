package com.huawei.cse.houseapp.customer.service;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.huawei.cse.houseapp.BizException;
import com.huawei.cse.houseapp.account.api.AccountEndpoint;
import com.huawei.cse.houseapp.customer.api.CustomerEndpoint;
import com.huawei.cse.houseapp.product.api.ProductEndpoint;
import com.huawei.cse.houseapp.product.api.ProductInfo;
import com.huawei.cse.houseapp.user.api.UserEndpoint;
import com.huawei.cse.houseapp.user.api.UserInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerEndpointImpl implements CustomerEndpoint {
  @Autowired
  private ConfigurationPropertiesModel model;

  @Autowired
  public CustomerService customerService;

  @Autowired
  private UserEndpoint userService;

  @Autowired
  private ProductEndpoint productService;

  @Autowired
  private AccountEndpoint accountService;

  @Override
  @POST
  @Path("buyWithTransactionSaga")
  @ApiResponse(code = 400, response = String.class, message = "buy failed")
  public boolean buyWithTransactionSaga(@HeaderParam("userId") long userId,
      @FormParam("productId") long productId, @FormParam("price") double price) {
    return customerService.buyWithTransactionSaga(userId, productId, price);
  }


  @Override
  @POST
  @Path("buyWithTransactionTCC")
  public boolean buyWithTransactionTCC(@HeaderParam("userId") long userId,
      @FormParam("productId") long productId, @FormParam("price") double price) {
    return customerService.buyWithTransactionTCC(userId, productId, price);
  }

  @Override
  @POST
  @Path("buyWithoutTransaction")
  @ApiResponse(code = 400, response = String.class, message = "buy failed")
  public boolean buyWithoutTransaction(@HeaderParam("userId") long userId,
      @FormParam("productId") long productId, @FormParam("price") double price) {
    // product will lock, put it in front
    if (!productService.buyWithoutTransaction(productId, userId, price)) {
      throw new BizException(400, "product already sold");
    }
    if (!userService.buyWithoutTransaction(userId, price)) {
      throw new BizException(400, "user do not got so much money");
    }
    if (!accountService.payWithoutTransaction(userId, price)) {
      throw new BizException(400, "pay failed");
    }
    return true;
  }

  @ApiOperation(hidden = true, value = "")
  public void cancelBuy(long userId, long productId, double price) {
    //不做事情。生产代码可以记录审计日志。
  }

  @ApiOperation(hidden = true, value = "")
  public void confirmBuy(long userId, long productId, double price) {
    //不做事情。生产代码可以记录审计日志。 
  }

  //实际是重置数据接口，不改名字了。 
  @Override
  @POST
  @Path("login")
  public long login(@FormParam("username") String username,
      @FormParam("password") String password) {
    productService.login(username, password);
    accountService.login(username, password);
    return userService.login(username, password);
  }

  @Override
  @GET
  @Path("getUserInfo")
  @Produces(MediaType.APPLICATION_JSON)
  public UserInfo getUserInfo(@QueryParam("userName") String userName) {
    return userService.getUserInfo(userName);
  }

  @Override
  @POST
  @Path("login2")
  public long login2(@FormParam("username") String username,
      @FormParam("password") String password) {
    return userService.login2(username, password);
  }

  @Override
  @GET
  @Path("searchAllProducts")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ProductInfo> searchAllProducts() {
    return productService.searchAllForCustomer();
  }

  @Override
  @GET
  @Path("balance")
  @Produces(MediaType.APPLICATION_JSON)
  public String balance() {
    double user = userService.queryReduced();
    double acct = accountService.queryReduced();
    double prod = productService.queryReduced();
    return String.format("user:%s;acct:%s;prod:%s", user, acct, prod);
  }

  @Override
  @GET
  @Path("searchAll")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ProductInfo> searchAll(@QueryParam("userId") int userId) {
    return productService.searchAll(userId);
  }
}
