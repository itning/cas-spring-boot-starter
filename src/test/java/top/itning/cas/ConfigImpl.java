package top.itning.cas;

import top.itning.cas.config.IAnalysisResponseBody;
import top.itning.cas.config.ICheckIsLoginConfig;
import top.itning.cas.config.INeedSetMap2SessionConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author itning
 * @date 2019/6/23 10:27
 */
public class ConfigImpl implements IAnalysisResponseBody, ICheckIsLoginConfig, INeedSetMap2SessionConfig {
    @Override
    public Map<String, String> analysisBody2Map(String body) {
        return null;
    }

    @Override
    public boolean isLogin(HttpServletResponse resp, HttpServletRequest req) {
        return false;
    }

    @Override
    public boolean needSetMapSession() {
        return false;
    }
}
