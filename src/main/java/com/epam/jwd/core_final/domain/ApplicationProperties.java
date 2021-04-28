package com.epam.jwd.core_final.domain;

import static com.epam.jwd.core_final.util.PropertyReader.getProperty;

/**
 * This class should be IMMUTABLE!
 * <p>
 * Expected fields:
 * <p>
 * inputRootDir {@link String} - base dir for all input files
 * outputRootDir {@link String} - base dir for all output files
 * crewFileName {@link String}
 * missionsFileName {@link String}
 * spaceshipsFileName {@link String}
 * <p>
 * fileRefreshRate {@link Integer}
 * dateTimeFormat {@link String} - date/time format for {@link java.time.format.DateTimeFormatter} pattern
 */
public final class ApplicationProperties {
    private static ApplicationProperties properties;
    public final String resourcesDir = "src/main/resources";
    public final String inputRootDir = getProperty("inputRootDir");
    public final String outputRootDir = getProperty("outputRootDir");
    public final String crewFileName = getProperty("crewFileName");
    public final String missionsFileName = getProperty("missionsFileName");
    public final String spaceshipsFileName = getProperty("spaceshipsFileName");
    public final String spacemapFileName = getProperty("spacemapFileName");
    public final Integer fileRefreshRate = Integer.parseInt(getProperty("fileRefreshRate"));
    public final String dateTimeFormat = getProperty("dateTimeFormat");

    private ApplicationProperties() {
    }

    public static ApplicationProperties getInstance() {
        if (properties == null) {
            properties = new ApplicationProperties();
        }
        return properties;
    }
}
