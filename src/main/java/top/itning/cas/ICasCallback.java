package top.itning.cas;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Cas 回调接口
 *
 * @author itning
 */
public interface ICasCallback {
    /**
     * 当登陆成功时
     *
     * @param resp          {@link HttpServletResponse}
     * @param req           {@link HttpServletRequest}
     * @param attributesMap 登陆成功时CAS服务器返回的属性信息
     * @throws IOException      可能抛出的异常
     * @throws ServletException 可能抛出的异常
     */
    void onLoginSuccess(HttpServletResponse resp, HttpServletRequest req, Map<String, String> attributesMap) throws IOException, ServletException;

    /**
     * 当登陆失败时
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     * @param e    登陆失败时抛出的异常
     * @throws IOException      可能抛出的异常
     * @throws ServletException 可能抛出的异常
     */
    void onLoginFailure(HttpServletResponse resp, HttpServletRequest req, Exception e) throws IOException, ServletException;

    /**
     * 当用户没有登陆时
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     * @throws IOException      可能抛出的异常
     * @throws ServletException 可能抛出的异常
     */
    void onNeverLogin(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException;

    /**
     * <p>当出现OPTIONS请求时</p>
     * <p>一般来说OPTION请求为浏览器检测是否支持跨域</p>
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     * @throws IOException      可能抛出的异常
     * @throws ServletException 可能抛出的异常
     */
    void onOptionsHttpMethodRequest(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException;
}
