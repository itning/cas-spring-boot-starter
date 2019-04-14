package top.itning.cas.callback.option;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>当出现OPTIONS请求时</p>
 * <p>一般来说OPTION请求为浏览器检测是否支持跨域</p>
 *
 * @author itning
 * @date 2019/4/14 22:24
 */
@FunctionalInterface
public interface IOptionsHttpMethodCallBack {
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
