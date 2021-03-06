# 秒杀抢房系统

***接入服务***
* 网关服务(edge-service) : 转发浏览器请求到选房服务和页面服务

***前端服务***
* 页面服务(customer-website): 采用 spring boot 的静态页面服务
* 选房服务(customer-service) : 前端服务，通过 Dubbo REST 对外提供服务

***后端服务***
* 用户中心(user-service): Dubbo 协议内部服务
* 房源服务(product-service): Dubbo 协议内部服务
* 支付服务(account-service): Dubbo 协议内部服务

***抢购业务流程***
1. 登录校验: 选房服务 -> 用户中心
2. 展示选房场次列表： 选房服务
3. 进入一个选房场次，展示选房详情和可选房间: 选房服务 -> 房源服务
4. 选定一个房间点击秒杀抢房（选房服务），**开启分布式事务**
5. 检查用户可用余额并冻结房间价格对应金额（用户中心）
6. 锁定房源（房源服务）**5,6两步可以异步发起**
7. 等5,6两步都成功后，扣减房间金额（支付中心）
8. 更新房源（房源服务）
9. 更新用户可用余额（用户中心）
10. 更新选房结果（选房服务）
11. 提交事务
12. 刷新选房详情页面（查询选房服务）




