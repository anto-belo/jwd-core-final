package com.epam.jwd.core_final.util.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Point;
import com.epam.jwd.core_final.exception.InvalidFileFormatException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.PlanetFactory;
import com.epam.jwd.core_final.util.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Scanner;

public class SpacemapFileParser implements FileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpacemapFileParser.class);
    private static SpacemapFileParser parser;

    private SpacemapFileParser() {
    }

    public static SpacemapFileParser getInstance() {
        if (parser == null) {
            parser = new SpacemapFileParser();
        }
        return parser;
    }

    @Override
    public void parse() throws InvalidFileFormatException {
        ApplicationProperties props = ApplicationProperties.getInstance();
        String filePath = props.resourcesDir + File.separator + props.inputRootDir
                + File.separator + props.spacemapFileName;
        EntityFactory<Planet> factory = PlanetFactory.getInstance();
        Collection<Planet> planets = Application.context.retrieveBaseEntityList(Planet.class);
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            int i = 0;
            while (sc.hasNextLine()) {
                String[] spacemapLine = sc.nextLine().split(",");
                for (int j = 0; j < spacemapLine.length; j++) {
                    if (spacemapLine[j].equals("null")) {
                        continue;
                    }
                    planets.add(factory.create(spacemapLine[j], new Point(j, i)));
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(FILE_NOT_FOUND_MSG);
            System.out.println(FILE_NOT_FOUND_MSG);
        }
        if (planets.isEmpty()) {
            throw new InvalidFileFormatException(FILE_IS_EMPTY_MSG);
        }
    }
}
