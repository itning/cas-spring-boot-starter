<h3 align="center">CAS 单点登陆 spring boot starter</h3>
<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/itning/cas-spring-boot-starter.svg?style=social&label=Stars)](https://github.com/itning/cas-spring-boot-starter/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/itning/cas-spring-boot-starter.svg?style=social&label=Fork)](https://github.com/itning/cas-spring-boot-starter/network/members)
[![GitHub watchers](https://img.shields.io/github/watchers/itning/cas-spring-boot-starter.svg?style=social&label=Watch)](https://github.com/itning/cas-spring-boot-starter/watchers)
[![GitHub followers](https://img.shields.io/github/followers/itning.svg?style=social&label=Follow)](https://github.com/itning?tab=followers)

</div>

<div align="center">
	
[![GitHub issues](https://img.shields.io/github/issues/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/issues)
[![GitHub license](https://img.shields.io/github/license/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/blob/master/LICENSE)
[![GitHub last commit](https://img.shields.io/github/last-commit/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/commits)
[![GitHub release](https://img.shields.io/github/release/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter/releases)
[![GitHub repo size in bytes](https://img.shields.io/github/repo-size/itning/cas-spring-boot-starter.svg)](https://github.com/itning/cas-spring-boot-starter)
[![HitCount](http://hits.dwyl.io/itning/cas-spring-boot-starter.svg)](http://hits.dwyl.io/itning/cas-spring-boot-starter)
[![language](https://img.shields.io/badge/language-JAVA-green.svg)](https://github.com/itning/cas-spring-boot-starter)
[![](https://jitpack.io/v/itning/cas-spring-boot-starter.svg)](https://jitpack.io/#itning/cas-spring-boot-starter)
[![Build Status](https://travis-ci.org/itning/cas-spring-boot-starter.svg?branch=master)](https://travis-ci.org/itning/cas-spring-boot-starter)

</div>

---

## 安装

[![](https://jitpack.io/v/itning/cas-spring-boot-starter.svg)](https://jitpack.io/#itning/cas-spring-boot-starter)

## maven

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
    <version>1.2.0-RELEASE</version>
</dependency>
```

## gradle

1.Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2.Add the dependency

```groovy
dependencies {
	implementation 'com.github.itning:cas-spring-boot-starter:1.2.0-RELEASE'
}
```

## 使用

### 配置

#### 在yml或properties中配置

[哈尔滨信息工程学院的同学点我](https://github.com/itning/cas-spring-boot-starter/tree/master/pic/config)

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
| cas.exclude | 排除过滤地址(会排除以其开头的请求) |  |
| cas.enabled | 是否开启 | true |

**注意：默认为null的必须配置**

#### 实现登陆状态回调

**回调有默认实现类: [CasAutoConfigure](https://github.com/itning/cas-spring-boot-starter/blob/master/src/main/java/top/itning/cas/CasAutoConfigure.java)**，即**如果你不自己实现接口则会使用默认的实现**

各个接口的作用请看接口注释。

写两个类实现 [callback和config](https://github.com/itning/cas-spring-boot-starter/tree/master/src/main/java/top/itning/cas) 包下的接口

```java
import top.itning.cas.callback.login.ILoginFailureCallBack;
import top.itning.cas.callback.login.ILoginNeverCallBack;
import top.itning.cas.callback.login.ILoginSuccessCallBack;
import top.itning.cas.callback.option.IOptionsHttpMethodCallBack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author itning
 * @date 2019/6/23 10:26
 */
public class CallBackImpl implements ILoginFailureCallBack, ILoginNeverCallBack, ILoginSuccessCallBack, IOptionsHttpMethodCallBack {
    @Override
    public void onLoginFailure(HttpServletResponse resp, HttpServletRequest req, Exception e) throws IOException, ServletException {
        
    }

    @Override
    public void onNeverLogin(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException {

    }

    @Override
    public void onLoginSuccess(HttpServletResponse resp, HttpServletRequest req, Map<String, String> attributesMap) throws IOException, ServletException {

    }

    @Override
    public void onOptionsHttpMethodRequest(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException {

    }
}

```
```java
import top.itning.cas.config.IAnalysisResponseBody;
import top.itning.cas.config.ICheckIsLoginConfig;
import top.itning.cas.config.INeedSetMap2SessionConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author itning
 * @date 2019/6/23 10:27
 */
public class ConfigImpl implements IAnalysisResponseBody, ICheckIsLoginConfig, INeedSetMap2SessionConfig {
    @Override
    public Map<String, String> analysisBody2Map(String body) {
        return null;
    }

    @Override
    public boolean isLogin(HttpServletResponse resp, HttpServletRequest req) {
        return false;
    }

    @Override
    public boolean needSetMapSession() {
        return false;
    }
}

```

## 如何

1. 如何获取CAS服务器登陆后传过来的属性？

   我们将它放在session中了，你可以使用 ```HttpSession#getAttribute``` 方法来获取，参数默认为```_cas_attributes_```，当然你可以在配置中更改```cas.session-attribute-name```的值
   
2. 如何配置只过滤某些URL路径？

   只需在**过滤器初始化之前**调用该静态方法：

   ```java
   top.itning.cas.CasAutoConfigure.setUrlPatterns(String... urlPatterns);
   ```

   源码：

   ```java
   /**
   * 设置拦截路径
   *
   * @param urlPatterns 路径
   */
   public static void setUrlPatterns(String... urlPatterns) {
   	up = urlPatterns;
   }
   ```

## 流程

![流程图](https://raw.githubusercontent.com/itning/cas-spring-boot-starter/master/pic/1.png)
