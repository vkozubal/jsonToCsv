package net.digicode;

import java.io.IOException;
import java.util.Properties;

/**
 * holds properties from the configuration file
 */
public final class Config {

    private Properties properties = null;

    public Config(String configName) {
        try {
            properties = new Properties();
            properties.load(getClass().getResourceAsStream(configName));
        } catch (IOException e) {
            throw new RuntimeException("Cant read config file " + configName);
        }
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}