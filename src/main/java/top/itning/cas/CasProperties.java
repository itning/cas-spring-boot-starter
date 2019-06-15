package top.itning.cas;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.util.List;

/**
 * CAS 配置
 *
 * @author itning
 */
@ConfigurationProperties(prefix = "cas")
public class CasProperties {
    /**
     * 启用
     */
    private boolean enabled = true;
    /**
     * CAS服务端地址
     */
    private URI serverUrl;
    /**
     * 登陆地址(CAS服务端地址)
     */
    private URI loginUrl;
    /**
     * 登出网址(CAS服务端地址)
     */
    private URI logoutUrl;

    /**
     * 登陆成功后跳转的网址
     */
    private URI loginSuccessUrl;

    /**
     * 本地服务端地址(该项目地址)
     */
    private URI localServerUrl;
    /**
     * 登陆地址(访问该地址会跳转到loginUrl)
     */
    private String clientLoginPath = "/login";
    /**
     * 登出地址(访问该地址会跳转到登出地址)
     */
    private String clientLogoutPath = "/logout";
    /**
     * 存储获取到的Attributes的Session Name
     */
    private String sessionAttributeName = "_cas_attributes_";
    /**
     * 请求读超时(ms)
     */
    private int requestReadTimeout = 5000;
    /**
     * 请求连接超时(ms)
     */
    private int requestConnectTimeout = 15000;
    /**
     * 开启调试
     */
    private boolean debug = false;
    /**
     * 允许跨域
     */
    private boolean allowCors = true;
    /**
     * 排除过滤
     */
    private List<String> exclude;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(URI serverUrl) {
        this.serverUrl = serverUrl;
    }

    public URI getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(URI loginUrl) {
        this.loginUrl = loginUrl;
    }

    public URI getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(URI logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public URI getLoginSuccessUrl() {
        return loginSuccessUrl;
    }

    public void setLoginSuccessUrl(URI loginSuccessUrl) {
        this.loginSuccessUrl = loginSuccessUrl;
    }

    public URI getLocalServerUrl() {
        return localServerUrl;
    }

    public void setLocalServerUrl(URI localServerUrl) {
        this.localServerUrl = localServerUrl;
    }

    public String getClientLoginPath() {
        return clientLoginPath;
    }

    public void setClientLoginPath(String clientLoginPath) {
        this.clientLoginPath = clientLoginPath;
    }

    public String getClientLogoutPath() {
        return clientLogoutPath;
    }

    public void setClientLogoutPath(String clientLogoutPath) {
        this.clientLogoutPath = clientLogoutPath;
    }

    public String getSessionAttributeName() {
        return sessionAttributeName;
    }

    public void setSessionAttributeName(String sessionAttributeName) {
        this.sessionAttributeName = sessionAttributeName;
    }

    public int getRequestReadTimeout() {
        return requestReadTimeout;
    }

    public void setRequestReadTimeout(int requestReadTimeout) {
        this.requestReadTimeout = requestReadTimeout;
    }

    public int getRequestConnectTimeout() {
        return requestConnectTimeout;
    }

    public void setRequestConnectTimeout(int requestConnectTimeout) {
        this.requestConnectTimeout = requestConnectTimeout;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isAllowCors() {
        return allowCors;
    }

    public void setAllowCors(boolean allowCors) {
        this.allowCors = allowCors;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    @Override
    public String toString() {
        return "CasProperties{" +
                "enabled=" + enabled +
                ", serverUrl=" + serverUrl +
                ", loginUrl=" + loginUrl +
                ", logoutUrl=" + logoutUrl +
                ", loginSuccessUrl=" + loginSuccessUrl +
                ", localServerUrl=" + localServerUrl +
                ", clientLoginPath='" + clientLoginPath + '\'' +
                ", clientLogoutPath='" + clientLogoutPath + '\'' +
                ", sessionAttributeName='" + sessionAttributeName + '\'' +
                ", requestReadTimeout=" + requestReadTimeout +
                ", requestConnectTimeout=" + requestConnectTimeout +
                ", debug=" + debug +
                ", allowCors=" + allowCors +
                ", exclude=" + exclude +
                '}';
    }
}
