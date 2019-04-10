package top.itning.cas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.springframework.http.HttpHeaders.*;

/**
 * Cas 回调默认实现
 *
 * @author itning
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractCasCallBackImpl implements ICasCallback {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCasCallBackImpl.class);
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    protected final CasProperties casProperties;

    protected AbstractCasCallBackImpl(CasProperties casProperties) {
        this.casProperties = casProperties;
    }

    @Override
    public void onLoginSuccess(HttpServletResponse resp, HttpServletRequest req, Map<String, String> attributesMap) throws IOException {
        debug("Now send redirect to " + casProperties.getLoginSuccessUrl().toString());
        resp.sendRedirect(casProperties.getLoginSuccessUrl().toString());
    }

    @Override
    public void onLoginFailure(HttpServletResponse resp, HttpServletRequest req, Exception e) throws IOException {
        allowCors(resp, req);
        resp.setHeader(RETRY_AFTER, "10");
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        RestModel<Void> restModel = new RestModel<>();
        restModel.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        restModel.setMsg("认证失败，请重试");
        writeRestModel2Response(resp, restModel);
    }

    @Override
    public void onNeverLogin(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        allowCors(resp, req);
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        RestModel<Void> restModel = new RestModel<>();
        restModel.setCode(HttpStatus.UNAUTHORIZED.value());
        restModel.setMsg("请先登陆");
        writeRestModel2Response(resp, restModel);
    }

    @Override
    public void onOptionsHttpMethodRequest(HttpServletResponse resp, HttpServletRequest req) {
        allowCors(resp, req);
    }

    /**
     * 将RestModel写入Response
     *
     * @param resp      {@link HttpServletResponse}
     * @param restModel {@link RestModel}
     * @throws IOException see {@link HttpServletResponse#getWriter()}
     */
    private void writeRestModel2Response(HttpServletResponse resp, RestModel<Void> restModel) throws IOException {
        String json = MAPPER.writeValueAsString(restModel);
        PrintWriter writer = resp.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }


    /**
     * 允许跨域(不管客户端地址是什么，全部允许)
     *
     * @param resp {@link HttpServletResponse}
     * @param req  {@link HttpServletRequest}
     */
    protected void allowCors(HttpServletResponse resp, HttpServletRequest req) {
        String origin = req.getHeader(ORIGIN);
        resp.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        resp.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        resp.setHeader(ACCESS_CONTROL_ALLOW_METHODS, "POST,GET,OPTIONS,DELETE,PUT,PATCH");
        resp.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, req.getHeader(ACCESS_CONTROL_REQUEST_HEADERS));
        resp.setIntHeader(ACCESS_CONTROL_MAX_AGE, 2592000);
    }

    /**
     * DEBUG 日志输出
     *
     * @param msg 日志消息
     */
    private void debug(String msg) {
        if (casProperties.isDebug()) {
            logger.debug(msg);
        }
    }
}
