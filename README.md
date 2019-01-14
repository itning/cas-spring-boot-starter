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
    <version>1.0.0-RELEASE</version>
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
	implementation 'com.github.itning:cas-spring-boot-starter:1.0.0-RELEASE'
}
```



## 使用

### 配置

#### 在yml或properties中配置

[哈尔滨信息工程学院的同学点我](https://github.com/itning/cas-spring-boot-starter/tree/master/pic)

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

#### 实现登陆状态回调

**回调有默认实现类: [CasCallBackDefaultImpl](https://github.com/itning/cas-spring-boot-starter/blob/master/src/main/java/top/itning/cas/CasCallBackDefaultImpl.java)**

写一个类实现 [ICasCallback](https://github.com/itning/cas-spring-boot-starter/blob/master/src/main/java/top/itning/cas/ICasCallback.java) 接口

```java
import org.springframework.stereotype.Component;
import top.itning.cas.ICasCallback;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 自定义回调
 *
 * @author itning
 */
@Component//必须要有该注解 目的将该类加入IOC容器中
public class MyCasCallBack implements ICasCallback {
    @Override
    public void onLoginSuccess(HttpServletResponse resp, HttpServletRequest req, Map<String, String> attributesMap) throws IOException, ServletException {
        //当登陆成功时自动调用该方法
        //你可以向浏览器写一段话或者写入JSON
        PrintWriter writer = resp.getWriter();
        writer.write("登陆成功");
        writer.flush();
        writer.close();
    }

    @Override
    public void onLoginFailure(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException {
        //当登陆失败的时候回调
    }

    @Override
    public void onNeverLogin(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException {
        //当用户没有登陆时的回调
    }

    @Override
    public void onOptionsHttpMethodRequest(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException {
        //跨域配置
        //注意:需要在配置文件中将allow-cors设置为true 这个方法才会被调用
        //cas.allow-cors=true
        String origin = req.getHeader("Origin");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", origin);
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,DELETE,PUT,PATCH");
        resp.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
    }
}
```



