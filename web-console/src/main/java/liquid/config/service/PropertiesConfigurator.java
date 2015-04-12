package liquid.config.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Tao Ma on 4/9/15.
 */
public class PropertiesConfigurator {
    private final Properties properties = new Properties();
    private static final String USER_SERVICE_CLASS_NAME = "user.service.class.name";

    private String userServiceClassName;

    private static PropertiesConfigurator ourInstance = new PropertiesConfigurator();

    public static PropertiesConfigurator getInstance() {
        return ourInstance;
    }

    private PropertiesConfigurator() {
        String target = System.getProperty("ENV_TARGET", "dev");
        try {
            InputStream inputStream = PropertiesConfigurator.class.getClassLoader().getResourceAsStream("liquid-" + target + ".properties");
            properties.load(inputStream);
            userServiceClassName = properties.getProperty(USER_SERVICE_CLASS_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserServiceClassName() {
        return userServiceClassName;
    }
}
