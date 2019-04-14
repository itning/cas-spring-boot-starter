package top.itning.cas.config;

import java.util.Map;

/**
 * 解析响应体
 *
 * @author itning
 * @date 2019/4/14 22:31
 */
@FunctionalInterface
public interface IAnalysisResponseBody {
    /**
     * 解析响应体到Map
     *
     * @param body 响应体
     * @return Map
     */
    Map<String, String> analysisBody2Map(String body);
}
