package top.itning.cas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author itning
 */
@Configuration
@EnableConfigurationProperties(CasProperties.class)
public class CasAutoConfigure {
    private final CasProperties casProperties;

    @Autowired
    public CasAutoConfigure(CasProperties casProperties) {
        this.casProperties = casProperties;
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
    public ICasCallback iCasCallback() {
        return new CasCallBackDefaultImpl(casProperties);
    }
}
