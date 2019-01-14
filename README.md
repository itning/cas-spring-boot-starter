# CAS 单点登陆 spring boot starter

[![GitHub stars](https://img.shields.io/github/stars/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/network)
[![GitHub watchers](https://img.shields.io/github/watchers/itning/cas-spring-boot-starter.svg?style=social&label=Watch)]()
[![GitHub followers](https://img.shields.io/github/followers/itning.svg?style=social&label=Follow)]()
[![GitHub issues](https://img.shields.io/github/issues/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/issues)

[![GitHub license](https://img.shields.io/github/license/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/blob/master/LICENSE)
[![GitHub last commit](https://img.shields.io/github/last-commit/itning/cas-spring-boot-starter.svg)]()
[![GitHub release](https://img.shields.io/github/release/itning/cas-spring-boot-starter.svg)]()
[![GitHub repo size in bytes](https://img.shields.io/github/repo-size/itning/cas-spring-boot-starter.svg)]()
[![language](https://img.shields.io/badge/language-JAVA-orange.svg)]()
[![](https://jitpack.io/v/itning/cas-spring-boot-starter.svg)](https://jitpack.io/#itning/cas-spring-boot-starter)

## 安装

[![](https://jitpack.io/v/itning/cas-spring-boot-starter.svg)](https://jitpack.io/#itning/cas-spring-boot-starter)

1.添加JitPack仓库到pom.xml

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

2.添加依赖

```xml
<dependency>
    <groupId>com.github.itning</groupId>
    <artifactId>cas-spring-boot-starter</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

## 使用
### 配置

在yml或properties中配置

[哈尔滨信息工程等学院的同学点我](https://github.com/itning/cas-spring-boot-starter/tree/master/pic)

|            属性             |                 说明                 |        默认        |
| :-------------------------: | :----------------------------------: | :----------------: |
|       cas.allow-cors        |             是否允许跨域             |        true        |
|          cas.debug          |               调试模式               |       false        |
|    cas.client-login-path    | 登陆地址(访问该地址会跳转到loginUrl) |       /login       |
|   cas.client-logout-path    | 登出地址(访问该地址会跳转到登出地址) |      /logout       |
| cas.session-attribute-name  | 存储获取到的Attributes的Session Name | \_cas_attributes_ |
| cas.request-connect-timeout |           请求连接超时(ms)           |       15000        |
|  cas.request-read-timeout   |            请求读超时(ms)            |        5000        |
|    cas.local-server-url     |      本地服务端地址(该项目地址)      |        null        |
|    cas.login-success-url    |         登陆成功后跳转的网址         |        null        |
|        cas.login-url        |       登陆地址(CAS服务端地址)        |        null        |
|       cas.logout-url        |       登出网址(CAS服务端地址)        |        null        |
|       cas.server-url        |            CAS服务端地址             |        null        |

**注意：默认为null的必须配置**

