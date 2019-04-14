package top.itning.cas.callback.login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当登陆失败时回调
 *
 * @author itning
 * @date 2019/4/14 22:37
 */
@FunctionalInterface
public interface ILoginFailureCallBack {
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
}
