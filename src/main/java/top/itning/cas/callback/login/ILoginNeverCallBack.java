package top.itning.cas.callback.login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当用户没有登陆时回调
 *
 * @author itning
 * @date 2019/4/14 22:38
 */
@FunctionalInterface
public interface ILoginNeverCallBack {
    /**
     * 当用户没有登陆时
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     * @throws IOException      可能抛出的异常
     * @throws ServletException 可能抛出的异常
     */
    void onNeverLogin(HttpServletResponse resp, HttpServletRequest req) throws IOException, ServletException;
}
