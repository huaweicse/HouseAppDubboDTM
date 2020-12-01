package com.huawei.cse.houseapp.account.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.huawei.cse.houseapp.BizException;
import com.huawei.cse.houseapp.account.api.AccountEndpoint;
import com.huawei.cse.houseapp.account.dao.AccountInfo;
import com.huawei.cse.houseapp.account.dao.AccountMapper;

public class AccountEndpointImpl implements AccountEndpoint {
  //内存测试
  //private AccountMapper accountMapper = new MockedAccountMapper();
  @Autowired
  private AccountMapper accountMapper;

  @Autowired
  AccountService accountService;

  @Override
  public long login(String username,
      String password) {
    // 使用测试账号登陆，登陆成功分配唯一的选房账号. 这里主要是为了并发和性能测试方便，实际业务场景需要按照要求设计。 
    if ("test".equals(username) && "test".equals(password)) {
      accountMapper.clear();

      for (int i = 1; i <= 100; i++) {
        AccountInfo info = new AccountInfo();
        info.setUserId(i);
        info.setReserved(false);
        info.setTotalBalance(8000000);
        accountMapper.createAccountInfo(info);
      }
      return 1L;
    } else {
      return -1;
    }
  }

  @Override
  public boolean payWithoutTransaction(long userId, double amount) {
    AccountInfo info = accountMapper.getAccountInfo(userId);
    if (info == null) {
      throw new BizException(400, "account id not valid");
    }
    if (info.getTotalBalance() < amount) {
      throw new BizException(400, "account do not have enough money");
    }
    info.setTotalBalance(info.getTotalBalance() - amount);
    accountMapper.updateAccountInfo(info);
    return true;
  }

  @Override
  public boolean payWithTransactionSaga(long userid, double amount) {
    return accountService.payWithTransactionSaga(userid, amount);
  }

  @Override
  public boolean payWithTransactionTCC(long userid, double amount) {
    return accountService.payWithTransactionTCC(userid, amount);
  }

  @Override
  public double queryReduced() {
    Double reduced = accountMapper.queryReduced();
    if (reduced == null) {
      reduced = 0D;
    }
    return 100 * 8000000 - reduced;
  }
}
