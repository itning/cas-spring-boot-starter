package top.itning.cas;

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
