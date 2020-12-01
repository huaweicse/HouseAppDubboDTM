package com.huawei.cse.houseapp.user.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.huawei.cse.houseapp.BizException;
import com.huawei.cse.houseapp.user.api.UserEndpoint;
import com.huawei.cse.houseapp.user.api.UserInfo;
import com.huawei.cse.houseapp.user.dao.UserMapper;

public class UserEndpointImpl implements UserEndpoint {

  @Autowired
  private UserService userService;

  // private UserMapper userMapper = new MockedUserMapper();
  @Autowired
  private UserMapper userMapper;

  //新增的登陆接口
  @Override
  public long login2(String username,
      String password) {
    // 使用测试账号登陆，登陆成功分配唯一的选房账号. 这里主要是为了并发和性能测试方便，实际业务场景需要按照要求设计。 
    try {
      UserInfo info = userMapper.getUserInfo(Long.parseLong(username.substring("user".length())));
      if (info == null) {
        return -1;
      }
      if (password.equals("test")) {
        return info.getUserId();
      }
      return -1;
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  //实际是重置数据接口，不改名字了。 
  @Override
  public long login(String username,
      String password) {
    // 使用测试账号登陆，登陆成功分配唯一的选房账号. 这里主要是为了并发和性能测试方便，实际业务场景需要按照要求设计。 
    if ("test".equals(username) && "test".equals(password)) {
      userMapper.clear();

      for (int i = 1; i <= 100; i++) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(i);
        userInfo.setUserName("user" + i);
        userInfo.setReserved(false);
        userInfo.setTotalBalance(10000000d);
        userMapper.createUser(userInfo);
      }
      return 1L;
    } else {
      return -1;
    }
  }

  @Override
  public boolean buyWithTransactionSaga(long userId,
      double price) {
    return userService.buyWithTransactionSaga(userId, price);
  }

  @Override
  public boolean buyWithTransactionTCC(long userId,
      double price) {
    return userService.buyWithTransactionTCC(userId, price);
  }


  @Override
  public boolean buyWithoutTransaction(long userId,
      double price) {
    UserInfo info = userMapper.getUserInfo(userId);
    if (info == null) {
      throw new BizException(400, "user id not valid");
    }
    if (info.getTotalBalance() < price) {
      throw new BizException(400, "user do not got so mush money");
    }
    info.setTotalBalance(info.getTotalBalance() - price);
    userMapper.updateUserInfo(info);
    return true;
  }

  @Override
  public double queryReduced() {
//    boolean isThrow =
//        DynamicPropertyFactory.getInstance().getBooleanProperty("cse.test.throwexception", false).get();
//    if (isThrow) {
//      throw new IllegalStateException("user controlled exception");
//    }

//    long sleep = DynamicPropertyFactory.getInstance().getLongProperty("cse.test.wait", -1).get();
//    String myid = RegistryUtils.getMicroserviceInstance().getInstanceId();
//    String instanceId = DynamicPropertyFactory.getInstance().getStringProperty("cse.test.myinstanceid", "").get();
//    if (sleep > 0 && myid.equals(instanceId)) {
//      try {
//        Thread.sleep(sleep);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }

    Double reduced = userMapper.queryReduced();
    if (reduced == null) {
      reduced = 0D;
    }
    return 100 * 10000000 - reduced;
  }

  @Override
  public UserInfo getUserInfo(String userName) {
    return userMapper.getUserInfo(Long.parseLong(userName.substring("user".length())));
  }
}
