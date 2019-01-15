package top.itning.cas;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cas配置默认实现
 *
 * @author itning
 */
final public class CasConfigDefaultImpl implements ICasConfig {
    private static final Logger logger = LoggerFactory.getLogger(CasConfigDefaultImpl.class);
    private final CasProperties casProperties;

    CasConfigDefaultImpl(CasProperties casProperties) {
        this.casProperties = casProperties;
    }

    @Override
    public Map<String, String> analysisBody2Map(String body) {
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
    }

    @Override
    public boolean isLogin(HttpServletResponse resp, HttpServletRequest req) {
        return req.getSession().getAttribute(casProperties.getSessionAttributeName()) != null;
    }
}
