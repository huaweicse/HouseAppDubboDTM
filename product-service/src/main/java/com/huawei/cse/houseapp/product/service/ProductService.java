package com.huawei.cse.houseapp.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.huawei.cse.houseapp.BizException;
import com.huawei.cse.houseapp.product.api.ProductInfo;
import com.huawei.cse.houseapp.product.dao.ProductMapper;

@Service
public class ProductService {
  @Autowired
  private ProductMapper productMapper;

  @Autowired
  PlatformTransactionManager txManager;

  public boolean buyWithTransactionSaga(long productId, long userId, double price) {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    TransactionStatus status = txManager.getTransaction(def);

    try {
      // 使用锁机制，防止多线程并发对于product的同时抢购。 getProductInfo使用了for
      // update，事务会加锁，不会并发。这里使用了spring事务。
      ProductInfo info = productMapper.getProductInfo(productId);
      if (info == null) {
        throw new BizException(400, "product id not valid");
      }
      if (price != info.getPrice()) {
        txManager.commit(status);
        throw new BizException(400, "product price not valid");
      }
      if (info.isReserved() || info.isSold()) {
        txManager.commit(status);
        return false;
      }
      info.setReserved(true);
      info.setReservedUserId(userId);
      productMapper.updateProductInfo(info);
      txManager.commit(status);
      return true;
    } catch (Exception e) {
      txManager.rollback(status);
      throw e;
    }
  }

  public boolean cancelTransactionSaga(long productId, long userId, double price) {
    return true;
  }

  public boolean buyWithTransactionTCC(long productId, long userId, double price) {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    TransactionStatus status = txManager.getTransaction(def);

    try {
      // 使用锁机制，防止多线程并发对于product的同时抢购。 getProductInfo使用了for
      // update，事务会加锁，不会并发。这里使用了spring事务。
      ProductInfo info = productMapper.getProductInfo(productId);
      if (info == null) {
        throw new BizException(400, "product id not valid");
      }
      if (price != info.getPrice()) {
        txManager.commit(status);
        throw new BizException(400, "product price not valid");
      }
      if (info.isReserved() || info.isSold()) {
        txManager.commit(status);
        return false;
      }
      info.setReserved(true);
      info.setReservedUserId(userId);
      productMapper.updateProductInfo(info);
      txManager.commit(status);
      return true;
    } catch (Exception e) {
      txManager.rollback(status);
      throw e;
    }
  }

  void confirmTransactionTCC(long productId, long userId, double price) {
    ProductInfo info = productMapper.getProductInfo(productId);
    if (info == null) {
      return;
    }
    if (info.isReserved()) {
      info.setReserved(false);
      info.setSold(true);
      productMapper.updateProductInfo(info);
    }
  }

  void cancelTransactionTCC(long productId, long userId, double price) {
    ProductInfo info = productMapper.getProductInfo(productId);
    if (info == null) {
      return;
    }
    if (info.isReserved() && info.getReservedUserId() == userId) {
      info.setReserved(false);
      productMapper.updateProductInfo(info);
    }
  }
}
