package top.itning.cas.callback.login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 当登陆成功时回调
 *
 * @author itning
 * @date 2019/4/14 22:36
 */
@FunctionalInterface
public interface ILoginSuccessCallBack {
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
}
