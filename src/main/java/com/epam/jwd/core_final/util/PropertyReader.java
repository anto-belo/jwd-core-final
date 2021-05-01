package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReader.class);
    private static final Properties properties = new Properties();

    private PropertyReader() {
    }

    public static String getProperty(String propertyName) {
        if (properties.isEmpty()) {
            loadProperties();
        }
        return properties.getProperty(propertyName, null);
    }

    /**
     * try-with-resource using FileInputStream
     *
     * @see <a href="https://www.netjstech.com/2017/09/how-to-read-properties-file-in-java.html">for an example</a> }
     * <p>
     * as a result - you should populate {@link ApplicationProperties} with corresponding
     * values from property file
     */
    private static void loadProperties() {
        final String propertiesFileName = "application.properties";

        InputStream iStream = null;
        try {
            // Loading properties file from the classpath
            iStream = PropertyReader.class.getClassLoader()
                    .getResourceAsStream(propertiesFileName);
            if (iStream == null) {
                throw new IOException("File not found");
            }
            properties.load(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (iStream != null) {
                    iStream.close();
                }
            } catch (IOException e) {
                LOGGER.error(FileParser.FILE_NOT_FOUND_MSG);
                System.out.println(FileParser.FILE_NOT_FOUND_MSG);
            }
        }
    }
}
