package com.epam.jwd.core_final.util.impl;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.InvalidFileFormatException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.impl.SimpleCrewService;
import com.epam.jwd.core_final.util.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;
import java.util.Scanner;

public class CrewFileParser implements FileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrewFileParser.class);

    @Override
    public void fillEntityStorage() throws InvalidFileFormatException {
        ApplicationProperties props = ApplicationProperties.getInstance();
        String filePath = props.resourcesDir + "/" + props.inputRootDir + "/" + props.crewFileName;
        Optional<String[]> oEntries = getEntries(filePath);
        EntityFactory<CrewMember> factory = CrewMemberFactory.getInstance();
        String[] entries = oEntries.orElseThrow(() -> new InvalidFileFormatException(FILE_IS_EMPTY_MSG));
        parseEntries(entries, factory);
    }

    private Optional<String[]> getEntries(String filePath) {
        Optional<String[]> entries = Optional.empty();
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                entries = Optional.of(line.split(";"));
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(FILE_NOT_FOUND_MSG);
            System.out.println(FILE_NOT_FOUND_MSG);
        }
        return entries;
    }

    private void parseEntries(String[] entries, EntityFactory<CrewMember> factory) {
        CrewService service = SimpleCrewService.INSTANCE;
        for (String entry : entries) {
            String[] memberArgs = entry.split(",");
            Role role = Role.resolveRoleById(Integer.parseInt(memberArgs[0]));
            String name = memberArgs[1];
            Rank rank = Rank.resolveRankById(Integer.parseInt(memberArgs[2]));
            service.createCrewMember(factory.create(name, role, rank));
        }
    }
}
