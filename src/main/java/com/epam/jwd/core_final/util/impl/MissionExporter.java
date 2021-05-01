package com.epam.jwd.core_final.util.impl;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.util.Exporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

public enum MissionExporter implements Exporter {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionExporter.class);

    @Override
    public void export() {
        List<FlightMission> flightMissions = SimpleMissionService.INSTANCE.findAllMissions();
        try {
            ApplicationProperties props = ApplicationProperties.getInstance();
            String dirPath = props.resourcesDir + File.separator + props.outputRootDir + File.separator;
            Path directory = Paths.get(dirPath);
            if (!Files.isDirectory(directory)) {
                Files.createDirectory(directory);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            SimpleDateFormat sdf = new SimpleDateFormat(props.dateTimeFormat);
            objectMapper.setDateFormat(sdf);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(dirPath + props.missionsFileName), flightMissions);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
