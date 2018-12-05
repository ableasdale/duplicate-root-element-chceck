import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.exceptions.XccConfigException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;

public class MarkLogicContentSourceProvider {

    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String database;
    private String port;
    private String user;
    private String password;
    private ContentSource cs;

    private MarkLogicContentSourceProvider() {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File("configuration.properties"));
            database = config.getString("database.host");
            port = config.getString("database.port");
            user = config.getString("database.user");
            password = config.getString("database.password");
        } catch (ConfigurationException e) {
            LOG.error("Configuration Exception: ", e);
        }

        try {
            URI uri = new URI(generateXdbcConnectionUri());
            cs = ContentSourceFactory
                    .newContentSource(uri);
        } catch (URISyntaxException e) {
            LOG.error("URISyntaxException Exception Caught: ", e);
        } catch (XccConfigException e) {
            LOG.error("XccConfigException Exception Caught: ", e);
        }
    }

    private String generateXdbcConnectionUri() {
        StringBuilder sb = new StringBuilder();
        sb.append("xdbc://").append(user).append(":")
                .append(password).append("@")
                .append(database).append(":")
                .append(port);
        LOG.info(String.format("XCC Content Source URI: %s", sb.toString()));
        return sb.toString();
    }

    private static class MarkLogicContentSourceProviderHolder {
        private static final MarkLogicContentSourceProvider INSTANCE = new MarkLogicContentSourceProvider();
    }

    public static MarkLogicContentSourceProvider getInstance() {
        return MarkLogicContentSourceProviderHolder.INSTANCE;
    }

    public ContentSource getContentSource() {
        return cs;
    }

}