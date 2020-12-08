package com.huawei.cse.houseapp.user.api;

import java.io.Serializable;

public class UserInfo implements Serializable {
  private static final long serialVersionUID = 0L;

  private long userId;

  private String userName;

  private double totalBalance;

  private boolean reserved;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public double getTotalBalance() {
    return totalBalance;
  }

  public void setTotalBalance(double totalBalance) {
    this.totalBalance = totalBalance;
  }

  public boolean isReserved() {
    return reserved;
  }

  public void setReserved(boolean reserved) {
    this.reserved = reserved;
  }
}
