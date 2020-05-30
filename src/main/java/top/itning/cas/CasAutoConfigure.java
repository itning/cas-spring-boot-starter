package top.itning.cas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import top.itning.cas.callback.login.ILoginFailureCallBack;
import top.itning.cas.callback.login.ILoginNeverCallBack;
import top.itning.cas.callback.login.ILoginSuccessCallBack;
import top.itning.cas.callback.option.IOptionsHttpMethodCallBack;
import top.itning.cas.config.IAnalysisResponseBody;
import top.itning.cas.config.ICheckIsLoginConfig;
import top.itning.cas.config.INeedSetMap2SessionConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.*;

/**
 * @author itning
 */
@Configuration
@EnableConfigurationProperties(CasProperties.class)
public class CasAutoConfigure {
    private static final Logger logger = LoggerFactory.getLogger(CasAutoConfigure.class);

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    private final CasProperties casProperties;

    private static String[] up = {"*"};

    public CasAutoConfigure(CasProperties casProperties) {
        this.casProperties = casProperties;
    }

    @Bean
    public FilterRegistrationBean<CasFilter> topItningCasFilterRegistration(CasProperties casProperties) {
        debug("urlPatterns：" + Arrays.toString(up));
        FilterRegistrationBean<CasFilter> registration = new FilterRegistrationBean<>();
        registration.setEnabled(casProperties.isEnabled());
        registration.setFilter(new CasFilter());
        registration.addUrlPatterns(up);
        registration.setName("top.itning.cas.filter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginFailureCallBack iLoginFailureCallBack() {
        return (resp, req, e) -> {
            allowCors(resp, req);
            resp.setHeader(RETRY_AFTER, "10");
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            RestModel<Void> restModel = new RestModel<>();
            restModel.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            restModel.setMsg("认证失败，请重试");
            writeRestModel2Response(resp, restModel);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginNeverCallBack iLoginNeverCallBack() {
        return (resp, req) -> {
            allowCors(resp, req);
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            RestModel<Void> restModel = new RestModel<>();
            restModel.setCode(HttpStatus.UNAUTHORIZED.value());
            restModel.setMsg("请先登陆");
            writeRestModel2Response(resp, restModel);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginSuccessCallBack iLoginSuccessCallBack() {
        return (resp, req, attributesMap) -> {
            debug("Now send redirect to " + casProperties.getLoginSuccessUrl().toString());
            resp.sendRedirect(casProperties.getLoginSuccessUrl().toString());
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public IOptionsHttpMethodCallBack iOptionsHttpMethodCallBack() {
        return this::allowCors;
    }

    @Bean
    @ConditionalOnMissingBean
    public IAnalysisResponseBody iAnalysisResponseBody() {
        return body -> {
            Map<String, String> map = new HashMap<>(16);
            try {
                Document doc = DocumentHelper.parseText(body);
                Node successNode = doc.selectSingleNode("//cas:authenticationSuccess");
                if (successNode != null) {
                    @SuppressWarnings("unchecked")
                    List<Node> attributesNode = doc.selectNodes("//cas:attributes/*");
                    attributesNode.forEach(defaultElement -> map.put(defaultElement.getName(), defaultElement.getText()));
                    if (casProperties.isDebug()) {
                        logger.debug("Get Map: " + map);
                    }
                } else {
                    //认证失败
                    logger.error("AUTHENTICATION failed : cas:authenticationSuccess Not Found");
                }
            } catch (Exception e) {
                logger.error("AUTHENTICATION failed and Catch Exception: ", e);
            }
            return map;
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ICheckIsLoginConfig iCheckIsLoginConfig() {
        return (resp, req) -> req.getSession().getAttribute(casProperties.getSessionAttributeName()) != null;
    }

    @Bean
    @ConditionalOnMissingBean
    public INeedSetMap2SessionConfig iNeedSetMap2SessionConfig() {
        return () -> true;
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
     * DEBUG 日志输出
     *
     * @param msg 日志消息
     */
    private void debug(String msg) {
        if (casProperties.isDebug()) {
            logger.debug(msg);
        }
    }

    /**
     * 设置拦截路径
     *
     * @param urlPatterns 路径
     */
    public static void setUrlPatterns(String... urlPatterns) {
        up = urlPatterns;
    }
}
