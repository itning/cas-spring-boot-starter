package top.itning.cas.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 是否登陆判断配置
 *
 * @author itning
 * @date 2019/4/14 22:41
 */
@FunctionalInterface
public interface ICheckIsLoginConfig {
    /**
     * 是否登陆判断
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     * @return 已经登陆返回<code>true</code>
     */
    boolean isLogin(HttpServletResponse resp, HttpServletRequest req);
}
