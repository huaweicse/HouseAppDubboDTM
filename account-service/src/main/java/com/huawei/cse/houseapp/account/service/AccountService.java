package com.huawei.cse.houseapp.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huawei.cse.houseapp.BizException;
import com.huawei.cse.houseapp.account.dao.AccountInfo;
import com.huawei.cse.houseapp.account.dao.AccountMapper;

@Service
public class AccountService {
  @Autowired
  private AccountMapper accountMapper;

  public boolean payWithTransactionSaga(long userid, double amount) {
    AccountInfo info = accountMapper.getAccountInfo(userid);
    if (info == null) {
      throw new BizException(400, "account id not valid");
    }
    if (info.isReserved()) {
      throw new BizException(400, "account is already in transaction");
    }
    if (info.getTotalBalance() < amount) {
      throw new BizException(400, "account do not have enouph money");
    }
    info.setReserved(true);
    accountMapper.updateAccountInfo(info);
    return true;
  }

  public boolean cancelTransactionSaga(long userid, double amount) {
    return true;
  }

  //  @Participate(confirmMethod = "confirmTransactionTCC", cancelMethod = "cancelTransactionTCC")
  public boolean payWithTransactionTCC(long userid, double amount) {
    AccountInfo info = accountMapper.getAccountInfo(userid);
    if (info == null) {
      throw new BizException(400, "account id not valid");
    }
    if (info.isReserved()) {
      throw new BizException(400, "account is already in transaction");
    }
    if (info.getTotalBalance() < amount) {
      throw new BizException(400, "account do not have enouph money");
    }
    info.setReserved(true);
    accountMapper.updateAccountInfo(info);
    return true;
  }


  void confirmTransactionTCC(long userid, double amount) {
    AccountInfo info = accountMapper.getAccountInfo(userid);
    if (info == null) {
      return;
    }
    if (info.isReserved()) {
      info.setReserved(false);
      info.setTotalBalance(info.getTotalBalance() - amount);
      accountMapper.updateAccountInfo(info);
    }
  }

  void cancelTransactionTCC(long userid, double amount) {
    AccountInfo info = accountMapper.getAccountInfo(userid);
    if (info == null) {
      return;
    }
    if (info.isReserved()) {
      info.setReserved(false);
      accountMapper.updateAccountInfo(info);
    }
  }
}
