package com.epam.jwd.core_final.util.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidFileFormatException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.SimpleSpaceshipService;
import com.epam.jwd.core_final.util.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SpaceshipFileParser implements FileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceshipFileParser.class);

    @Override
    public void fillEntityStorage() throws InvalidFileFormatException {
        ApplicationProperties props = ApplicationProperties.getInstance();
        String filePath = props.resourcesDir + "/" + props.inputRootDir + "/" + props.spaceshipsFileName;
        Collection<Spaceship> spaceships = Application.context.retrieveBaseEntityList(Spaceship.class);
        SpaceshipService service = SimpleSpaceshipService.INSTANCE;
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                service.createSpaceship(parseSpaceship(line));
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(FILE_NOT_FOUND_MSG);
            System.out.println(FILE_NOT_FOUND_MSG);
        }
    }

    private Spaceship parseSpaceship(String line) throws InvalidFileFormatException {
        EntityFactory<Spaceship> factory = SpaceshipFactory.getInstance();
        String[] args = line.split(";");
        if (args.length != 3) {
            throw new InvalidFileFormatException(INVALID_FILE_FORMAT_MSG);
        }
        String name = args[0];
        Map<Role, Short> crew = parseSpaceshipCrew(args[2]);
        Long distance = Long.parseLong(args[1]);
        return factory.create(name, crew, distance);
    }

    private Map<Role, Short> parseSpaceshipCrew(String arg) throws InvalidFileFormatException {
        arg = arg.substring(1, arg.length() - 1);
        String[] roleEntries = arg.split(",");
        if (roleEntries.length != Role.values().length) {
            throw new InvalidFileFormatException(INVALID_FILE_FORMAT_MSG);
        }
        Map<Role, Short> roleMap = new HashMap<>();
        for (String roleEntry : roleEntries) {
            String[] roleEntryArgs = roleEntry.split(":");
            Role role = Role.resolveRoleById(Integer.parseInt(roleEntryArgs[0]));
            Short count = Short.parseShort(roleEntryArgs[1]);
            roleMap.put(role, count);
        }
        return roleMap;
    }
}
