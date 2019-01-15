package top.itning.cas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Cas 高级配置
 *
 * @author itning
 */
public interface ICasConfig {
    /**
     * 解析响应体到Map
     *
     * @param body 响应体
     * @return Map
     */
    Map<String, String> analysisBody2Map(String body);

    /**
     * 是否登陆判断
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     * @return 已经登陆返回<code>true</code>
     */
    boolean isLogin(HttpServletResponse resp, HttpServletRequest req);

    /**
     * 是否需要将属性放到Session中
     *
     * @return 需要返回<code>true</code>
     */
    default boolean needSetMapSession() {
        return true;
    }
}
