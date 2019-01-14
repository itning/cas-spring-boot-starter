package top.itning.cas;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Cas Filter
 *
 * @author itning
 */
public class CasFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CasFilter.class);
    private RestTemplate restTemplate;
    private CasProperties casProperties;
    private ICasCallback iCasCallback;

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("Cas filter init...");
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        casProperties = ctx.getBean(CasProperties.class);
        iCasCallback = ctx.getBean(ICasCallback.class);
        logger.info("Use login path: " + casProperties.getClientLoginPath());
        logger.info("Use logout path: " + casProperties.getClientLogoutPath());
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
        HttpSession session = req.getSession();
        debug("Get Servlet Path: " + req.getServletPath());

        if (casProperties.isAllowCors() && HttpMethod.OPTIONS.matches(req.getMethod())) {
            iCasCallback.onOptionsHttpMethodRequest(resp, req);
            return;
        }
        //登陆登出
        if (HttpMethod.GET.matches(req.getMethod())) {
            //login
            if (casProperties.getClientLoginPath().equals(req.getServletPath())) {
                //重定向到登陆地址
                String location = casProperties.getLoginUrl() + "?service=" + casProperties.getLocalServerUrl();
                debug("Match login path...");
                debug("Now send redirect to " + location);
                resp.sendRedirect(location);
                return;
            }
            //logout
            if (casProperties.getClientLogoutPath().equals(req.getServletPath())) {
                session.removeAttribute(casProperties.getSessionAttributeName());
                session.invalidate();
                //重定向到登出地址
                String location = casProperties.getLogoutUrl().toString();
                debug("Match logout path...");
                debug("Now send redirect to " + location);
                resp.sendRedirect(location);
                return;
            }
        }
        //CAS Start
        String ticket = req.getParameter("ticket");
        if (ticket != null) {
            debug("Get Ticket: " + ticket);
            debug("Send request to " + casProperties.getServerUrl() + "/serviceValidate?ticket=" + ticket + "&service=" + casProperties.getLocalServerUrl());
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(casProperties.getServerUrl() + "/serviceValidate?ticket={ticket}&service={local_server_url}", String.class, ticket, casProperties.getLocalServerUrl());
            debug("Get response status code: " + responseEntity.getStatusCode().value());
            if (responseEntity.getBody() != null) {
                debug("Get response body: ");
                debug(responseEntity.getBody());
                Map<String, String> map = analysisBody2Map(responseEntity.getBody());
                //解析成功,用户成功登陆
                session.setAttribute(casProperties.getSessionAttributeName(), map);
                debug("Set attribute " + casProperties.getSessionAttributeName() + " success");
                iCasCallback.onLoginSuccess(resp, req, map);
                return;
            } else {
                logger.error("AUTHENTICATION failed : Body is Null");
            }
            iCasCallback.onLoginFailure(resp, req);
            return;
        }
        if (session.getAttribute(casProperties.getSessionAttributeName()) == null) {
            iCasCallback.onNeverLogin(resp, req);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 解析响应体
     *
     * @param body 响应体
     * @return 解析完成的映射
     */
    private Map<String, String> analysisBody2Map(String body) {
        Map<String, String> map = new HashMap<>(16);
        try {
            Document doc = DocumentHelper.parseText(body);
            Node successNode = doc.selectSingleNode("//cas:authenticationSuccess");
            if (successNode != null) {
                @SuppressWarnings("unchecked")
                List<Node> attributesNode = doc.selectNodes("//cas:attributes/*");
                attributesNode.forEach(defaultElement -> map.put(defaultElement.getName(), defaultElement.getText()));
                logger.debug("Get Map: " + map);
            } else {
                //认证失败
                logger.error("AUTHENTICATION failed : cas:authenticationSuccess Not Found");
            }
        } catch (Exception e) {
            logger.error("AUTHENTICATION failed and Catch Exception: ", e);
        }
        return map;
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
