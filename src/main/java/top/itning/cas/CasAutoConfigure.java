package top.itning.cas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import top.itning.cas.callback.AbstractCasCallBackImpl;
import top.itning.cas.callback.option.IOptionsHttpMethodCallBack;
import top.itning.cas.callback.login.ILoginFailureCallBack;
import top.itning.cas.callback.login.ILoginNeverCallBack;
import top.itning.cas.callback.login.ILoginSuccessCallBack;
import top.itning.cas.config.AbstractCasConfigImpl;
import top.itning.cas.config.IAnalysisResponseBody;
import top.itning.cas.config.ICheckIsLoginConfig;
import top.itning.cas.config.INeedSetMap2SessionConfig;

/**
 * @author itning
 */
@Configuration
@EnableConfigurationProperties(CasProperties.class)
public class CasAutoConfigure {
    private final AbstractCasCallBackImpl abstractCasCallBack;
    private final AbstractCasConfigImpl abstractCasConfig;

    @Autowired
    public CasAutoConfigure(CasProperties casProperties) {
        abstractCasCallBack = new AbstractCasCallBackImpl(casProperties) {
        };
        abstractCasConfig = new AbstractCasConfigImpl(casProperties) {
        };
    }

    @Bean
    public FilterRegistrationBean topItningCasFilterRegistration() {
        FilterRegistrationBean<CasFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CasFilter());
        registration.addUrlPatterns("*");
        registration.setName("top.itning.cas.filter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginFailureCallBack iLoginFailureCallBack() {
        return abstractCasCallBack;
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginNeverCallBack iLoginNeverCallBack() {
        return abstractCasCallBack;
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginSuccessCallBack iLoginSuccessCallBack() {
        return abstractCasCallBack;
    }

    @Bean
    @ConditionalOnMissingBean
    public IOptionsHttpMethodCallBack iOptionsHttpMethodCallBack() {
        return abstractCasCallBack;
    }

    @Bean
    @ConditionalOnMissingBean
    public IAnalysisResponseBody iAnalysisResponseBody() {
        return abstractCasConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public ICheckIsLoginConfig iCheckIsLoginConfig() {
        return abstractCasConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public INeedSetMap2SessionConfig iNeedSetMap2SessionConfig() {
        return abstractCasConfig;
    }
}
