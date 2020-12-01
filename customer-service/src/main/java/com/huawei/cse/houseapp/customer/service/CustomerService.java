package com.huawei.cse.houseapp.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huawei.cse.houseapp.BizException;
import com.huawei.cse.houseapp.account.api.AccountEndpoint;
import com.huawei.cse.houseapp.product.api.ProductEndpoint;
import com.huawei.cse.houseapp.user.api.UserEndpoint;

@Service
public class CustomerService {
  @Autowired
  private UserEndpoint userService;

  @Autowired
  private ProductEndpoint productService;

  @Autowired
  private AccountEndpoint accountService;

  public boolean buyWithTransactionSaga(long userId,
      long productId, double price) {
    if (!userService.buyWithTransactionSaga(userId, price)) {
      throw new BizException(400, "user do not got so much money");
    }
    if (!productService.buyWithTransactionSaga(productId, userId, price)) {
      throw new BizException(400, "product already sold");
    }
    if (!accountService.payWithTransactionSaga(userId, price)) {
      throw new BizException(400, "pay failed");
    }
    return true;
  }

  public boolean buyWithTransactionTCC(long userId,
      long productId, double price) {
    if (!userService.buyWithTransactionTCC(userId, price)) {
      throw new BizException(400, "user do not got so much money");
    }
    if (!productService.buyWithTransactionTCC(productId, userId, price)) {
      throw new BizException(400, "product already sold");
    }
    if (!accountService.payWithTransactionTCC(userId, price)) {
      throw new BizException(400, "pay failed");
    }
    return true;
  }
}
