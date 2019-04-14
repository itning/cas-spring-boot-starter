package top.itning.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import top.itning.cas.callback.option.IOptionsHttpMethodCallBack;
import top.itning.cas.callback.login.ILoginFailureCallBack;
import top.itning.cas.callback.login.ILoginNeverCallBack;
import top.itning.cas.callback.login.ILoginSuccessCallBack;
import top.itning.cas.config.IAnalysisResponseBody;
import top.itning.cas.config.ICheckIsLoginConfig;
import top.itning.cas.config.INeedSetMap2SessionConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;


/**
 * Cas Filter
 *
 * @author itning
 */
public class CasFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CasFilter.class);
    private RestTemplate restTemplate;
    private CasProperties casProperties;
    private ILoginFailureCallBack loginFailureCallBack;
    private ILoginNeverCallBack loginNeverCallBack;
    private ILoginSuccessCallBack loginSuccessCallBack;
    private IOptionsHttpMethodCallBack optionsHttpMethodCallBack;
    private IAnalysisResponseBody analysisResponseBody;
    private ICheckIsLoginConfig checkIsLoginConfig;
    private INeedSetMap2SessionConfig needSetMap2SessionConfig;

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("Cas filter init...");
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        casProperties = ctx.getBean(CasProperties.class);
        loginFailureCallBack = ctx.getBean(ILoginFailureCallBack.class);
        loginNeverCallBack = ctx.getBean(ILoginNeverCallBack.class);
        loginSuccessCallBack = ctx.getBean(ILoginSuccessCallBack.class);
        optionsHttpMethodCallBack = ctx.getBean(IOptionsHttpMethodCallBack.class);
        analysisResponseBody = ctx.getBean(IAnalysisResponseBody.class);
        checkIsLoginConfig = ctx.getBean(ICheckIsLoginConfig.class);
        needSetMap2SessionConfig = ctx.getBean(INeedSetMap2SessionConfig.class);
        logger.info("Use login path: " + casProperties.getClientLoginPath());
        logger.info("Use logout path: " + casProperties.getClientLogoutPath());
        logger.info("Use ILoginFailureCallBack Implements: " + loginFailureCallBack.getClass().getName());
        logger.info("Use ILoginNeverCallBack Implements: " + loginNeverCallBack.getClass().getName());
        logger.info("Use ILoginSuccessCallBack Implements: " + loginSuccessCallBack.getClass().getName());
        logger.info("Use IOptionsHttpMethodCallBack Implements: " + optionsHttpMethodCallBack.getClass().getName());
        logger.info("Use IAnalysisResponseBody Implements: " + analysisResponseBody.getClass().getName());
        logger.info("Use ICheckIsLoginConfig Implements: " + checkIsLoginConfig.getClass().getName());
        logger.info("Use INeedSetMap2SessionConfig Implements: " + needSetMap2SessionConfig.getClass().getName());
        debug(casProperties.toString());

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //ms
        factory.setReadTimeout(casProperties.getRequestReadTimeout());
        //ms
        factory.setConnectTimeout(casProperties.getRequestConnectTimeout());
        restTemplate = new RestTemplate(factory);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        debug("Get Servlet Path: " + req.getServletPath());
        if (isExcludePath(req, resp, chain)) {
            return;
        }
        if (casProperties.isAllowCors() && HttpMethod.OPTIONS.matches(req.getMethod())) {
            optionsHttpMethodCallBack.onOptionsHttpMethodRequest(resp, req);
            return;
        }
        //登陆登出
        if (HttpMethod.GET.matches(req.getMethod())) {
            //login
            if (casProperties.getClientLoginPath().equals(req.getServletPath())) {
                //重定向到登陆地址
                doRedirectLoginPath(resp);
                return;
            }
            //logout
            if (casProperties.getClientLogoutPath().equals(req.getServletPath())) {
                removeCurrentLoginUserSessionAttribute(req.getSession());
                //重定向到登出地址
                doRedirectLogoutPath(resp);
                return;
            }
        }
        //CAS Start
        String ticket = req.getParameter("ticket");
        if (ticket != null) {
            doLoginWithTicket(resp, req, ticket);
            return;
        }
        if (checkIsLoginConfig.isLogin(resp, req)) {
            chain.doFilter(request, response);
            return;
        }
        loginNeverCallBack.onNeverLogin(resp, req);
    }

    @Override
    public void destroy() {

    }

    private void doRedirectLoginPath(HttpServletResponse resp) throws IOException {
        String location = getRedirectLocation();
        debug("Match login path...");
        debug("Now send redirect to " + location);
        resp.sendRedirect(location);
    }

    private void doRedirectLogoutPath(HttpServletResponse resp) throws IOException {
        String location = casProperties.getLogoutUrl().toString();
        debug("Match logout path...");
        debug("Now send redirect to " + location);
        resp.sendRedirect(location);
    }

    private void doLoginWithTicket(HttpServletResponse resp, HttpServletRequest req, String ticket) throws IOException, ServletException {
        debug("Get Ticket: " + ticket);
        try {
            Optional<String> bodyOptional = sendRequestAndGetResponseBody(ticket);
            if (bodyOptional.isPresent()) {
                String body = bodyOptional.get();
                debug("Get response body: ");
                debug(body);
                Map<String, String> map = analysisResponseBody.analysisBody2Map(body);
                //解析成功,用户成功登陆
                setMap2Session(req.getSession(), map);
                loginSuccessCallBack.onLoginSuccess(resp, req, map);
            } else {
                logger.error("AUTHENTICATION failed : Body is Null");
                loginFailureCallBack.onLoginFailure(resp, req, new RuntimeException("AUTHENTICATION failed : Body is Null"));
            }
        } catch (Exception e) {
            debug(e.getMessage());
            loginFailureCallBack.onLoginFailure(resp, req, e);
        }
    }

    private String getRedirectLocation() throws UnsupportedEncodingException {
        return casProperties.getLoginUrl() + "?service=" + URLEncoder.encode(casProperties.getLocalServerUrl().toString(), "UTF-8");
    }


    private void setMap2Session(HttpSession session, Map<String, String> map) {
        if (needSetMap2SessionConfig.needSetMapSession()) {
            session.setAttribute(casProperties.getSessionAttributeName(), map);
            debug("Set attribute " + casProperties.getSessionAttributeName() + " success");
        }
    }

    private Optional<String> sendRequestAndGetResponseBody(String ticket) {
        debug("Send request to " + casProperties.getServerUrl() + "/serviceValidate?ticket=" + ticket + "&service=" + casProperties.getLocalServerUrl());
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(casProperties.getServerUrl() + "/serviceValidate?ticket={ticket}&service={local_server_url}", String.class, ticket, casProperties.getLocalServerUrl());
        debug("Get response status code: " + responseEntity.getStatusCode().value());
        return Optional.ofNullable(responseEntity.getBody());
    }

    private void removeCurrentLoginUserSessionAttribute(HttpSession session) {
        if (needSetMap2SessionConfig.needSetMapSession()) {
            session.removeAttribute(casProperties.getSessionAttributeName());
            session.invalidate();
        }
    }

    private boolean isExcludePath(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (casProperties.getExclude() != null) {
            for (String path : casProperties.getExclude()) {
                if (req.getServletPath().startsWith(path)) {
                    chain.doFilter(req, resp);
                    return true;
                }
            }
        }
        return false;
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
