- [Myseckill](#Myseckill)
  * [项目介绍](#项目介绍)
  * [开发环境](#开发环境)
  * [开发技术点](#开发技术点)
    + [JSR380自定义参数验证](#JSR380自定义参数验证)
    + [两次MD5加密](#两次MD5加密)
    + [注册、登录](#注册、登录)
    + [基于Redis的非粘性Session共享机制](#基于Redis的非粘性Session共享机制)
    + [页面缓存、对象缓存](#页面缓存、对象缓存)
    + [验证码功能](#验证码功能)
    + [验证码功能实现](#验证码功能实现)
    + [接口限流,访问限流](#接口限流,访问限流)
    + [高并发秒杀方案](#高并发秒杀方案)
  * [秒杀流程](#秒杀流程)
  * [压测结果](#压测结果)
  * [存在问题](#存在问题)
  * [参考](#参考)
 




# Myseckill
## 项目介绍
该项目是在学习参考了[秒杀系统优化方案](https://www.cnblogs.com/xiangkejin/p/9351501.html)的前提下，
使用SpringBoot开发的高并发限时抢购秒杀系统，除了实现基本的登录、注册、查看商品列表、秒杀、下单等功能，项目中还针对高并发情况实现了系统缓存、验证码验证和限流。

## 开发环境
前端技术 ：Bootstrap + jQuery + Thymeleaf

后端技术 ：SpringBoot + MyBatis + MySQL

中间件技术 : Druid + Redis + RabbitMQ 

## 开发技术点

### JSR380自定义参数验证
自定义参数 "@isPhone" 验证账号是否为合法手机号:
* 编写一个注解: [@isphone.java](https://github.com/phantommmm/mySeckill/blob/master/src/main/java/com/phantom/seckill/validator/isPhone.java)
* 注解所需要的校验器: [isPhoneValidator.java](https://github.com/phantommmm/mySeckill/blob/master/src/main/java/com/phantom/seckill/validator/isPhoneValidator.java)
* 在需要校验的成员变量上添加注解: [LoginVo.java](https://github.com/phantommmm/mySeckill/blob/master/src/main/java/com/phantom/seckill/vo/LoginVo.java)
* 在参数前加上@Valid注解: [LoginController.java](https://github.com/phantommmm/mySeckill/blob/master/src/main/java/com/phantom/seckill/controller/LoginController.java)


### 两次MD5加密
第一次 MD5 发生在客户端浏览器，使用 js 对用户输入的密码进行加密，目的是防止用户密码在网络中明文传输。

第二次 MD5 发生在服务端，将用户密码和随机字符串 salt 拼接后再进行 MD5 加密，最后才写入数据库，目的是防止数据库泄露后用户密码随之泄露。

由于 MD5 加密是单向的，所以使用这种方式保存隐私数据是很安全的。

### 注册、登录
1. 访问localhost:8080/register进行注册，注册成功后选择跳转到登录页面登录
2. 访问localhost:8080/login 登录或跳转注册页，进行注册

### 基于Redis的非粘性Session共享机制
验证用户账号密码都正确情况下，通过UUID生成唯一id作为token，再将token作为key、用户信息作为value模拟session存储到redis，同时将token存储到cookie，保存登录状态
用户在请求同域名下其他页面时会带上该 token，服务器会根据该 token 从 Redis 服务器中获得相应的用户信息。

### 页面缓存、对象缓存
页面缓存：当某个页面第一次被请求时可以把渲染后的页面放入缓存redis，在后面的请求中可以直接从缓存中取得相应页面。

对象缓存：对用户信息、商品信息、订单信息、验证码和token等数据进行缓存，利用缓存来减少对数据库的访问，大大加快查询速度。

### 验证码功能
* 只有秒杀进行中，才会有验证码生成
* 只有验证码通过，才会执行秒杀功能
* 点击秒杀前要先填写验证码，没填写或验证码错误则提示“验证码错误”，验证码图片刷新
* 刷新页面或点击验证码图片，都会访问后台，使验证码图片更新
* 频繁的刷新页面或点击验证码图片，则会停止访问后台，不会更新验证码图片，并提示“访问频繁,请稍后尝试”

### 验证码功能实现
* 前端通过把商品id作为参数调用服务端创建验证码接口
* 服务端根据前端传过来的商品id和用户id生成验证码，并将商品id+用户id作为key，生成的验证码作为value存入redis，同时将生成的验证码输入图片写入imageIO让前端展示
* 将用户输入的验证码与根据商品id+用户id从redis查询到的验证码对比，相同就返回验证成功，进入秒杀；不同或从redis查询的验证码为空都返回验证失败，刷新验证码重试

### 接口限流,访问限流
* 在需要流量限制的接口上添加注解,例如：
![limit](https://github.com/phantommmm/Myseckill/blob/master/img/limit.png)
* [AccessInterceptor.java](https://github.com/phantommmm/mySeckill/blob/master/src/main/java/com/phantom/seckill/Interceptor/AccessInterceptor.java):对请求拦截，利用缓存记录用户请求的频率，当访问过快时，禁止访问
* 只有在秒杀进行中，页面秒杀按钮才能 “点击” 其他时刻按钮置灰，禁止访问后台

### 高并发秒杀方案
本地标记 + redis预处理 + RabbitMQ异步下单 + 客户端轮询

描述：通过三级缓冲保护，1、本地标记 2、redis预处理 3、RabbitMQ异步下单，最后才会访问数据库，这样做是为了最大力度减少对数据库的访问。

* 在秒杀阶段使用本地标记对用户秒杀过的商品做标记，若被标记过直接返回重复秒杀，未被标记才查询redis，通过本地标记来减少对redis的访问
* 抢购开始前，将商品和库存数据同步到redis中，所有的抢购操作都在redis中进行处理，通过Redis预减少库存减少数据库访问
* 为了保护系统不受高流量的冲击而导致系统崩溃的问题，使用RabbitMQ用异步队列处理下单，实际做了一层缓冲保护，做了一个窗口模型，窗口模型会实时的刷新用户秒杀的状态。
* client端用js轮询一个接口，用来获取处理状态

### 秒杀流程
详情的秒杀流程，请参考：[秒杀系统优化方案](https://www.cnblogs.com/xiangkejin/p/9351501.html)

### 压测结果
![压测效果](https://github.com/phantommmm/Myseckill/blob/master/img/jMeter.png)

### 存在问题
 当页面刷新快时，还是可以看到验证码提示和文本行:
![problem](https://github.com/phantommmm/Myseckill/blob/master/img/problem.png)

### 参考
[秒杀系统优化方案](https://www.cnblogs.com/xiangkejin/p/9351501.html)

[慕课](https://coding.imooc.com/class/168.html)
