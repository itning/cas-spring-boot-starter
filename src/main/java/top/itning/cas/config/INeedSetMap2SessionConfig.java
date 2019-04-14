package top.itning.cas.config;

/**
 * 是否需要将属性放到Session中
 *
 * @author itning
 * @date 2019/4/14 22:42
 */
@FunctionalInterface
public interface INeedSetMap2SessionConfig {
    /**
     * 是否需要将属性放到Session中
     *
     * @return 需要返回<code>true</code>
     */
    boolean needSetMapSession();
}
