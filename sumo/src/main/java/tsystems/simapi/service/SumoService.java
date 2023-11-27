package tsystems.simapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tsystems.simapi.component.SumoFileGateway;
import tsystems.simapi.entity.SumoXMLObject;
import tsystems.simapi.entity.releaseinfo.EcuDatasInfo;
import tsystems.simapi.entity.releaseinfo.ReleaseInfo;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class SumoService {

    @Autowired
    private final SumoFileGateway sumoFileGateway;

    public SumoService(SumoFileGateway sumoFileGateway) {
        this.sumoFileGateway = sumoFileGateway;
    }

    public Boolean runSimulation() {
        List<Integer> exitCodes = new ArrayList<>();
        exitCodes.add(executeCommand(getSimulationCommand()));
        exitCodes.add(executeCommand(getCopyLogsCommand()));
        exitCodes.add(executeCommand(getPlotTrajectoriesCommand()));
        exitCodes.add(executeCommand(getPlotEmissionsCommand()));

        if (exitCodes.stream().anyMatch(num -> num == -1)) {
            System.out.println("An error occurred while executing the scripts.");
            return false;
        } else if (exitCodes.stream().anyMatch(num -> num == 1)) {
            System.out.println("A command execution failed.");
            return false;
        }
        System.out.println("Commands executed successfully");
        return true;
    }

    public List<String> getSimulationCommand() {
        return List.of("/sumo/bin/sumo",
                "-c",
                "sumo_scenario/dua.actuated.sumocfg",
                "--fcd-output",
                "output-files/outputs/vehicle-trajectories.xml",
                "--emission-output",
                "output-files/outputs/emissions.xml");
    }

    public List<String> getCopyLogsCommand() {
        return List.of("cp",
                "sumo_scenario/Battery.out.xml",
                "sumo_scenario/dua.actuated.log",
                "sumo_scenario/dua.actuated.summary.xml",
                "sumo_scenario/dua.actuated.tripinfo.xml",
                "output-files/logs");
    }

    public List<String> getPlotTrajectoriesCommand() {
        System.out.println(System.getProperty("user.dir"));
        return List.of("python3",
                "visualization-scripts/plot_trajectories.py",
                "-t",
                "xy",
                "-o",
                "output-files/graphs/Vehicle-Trajectories.png",
                "output-files/outputs/vehicle-trajectories.xml");
    }

    public List<String> getPlotEmissionsCommand() {
        return List.of("python3",
                "visualization-scripts/plotXMLAttributes.py",
                "-x",
                "time",
                "-y",
                "CO2",
                "-s",
                "1",
                "-o",
                "output-files/graphs/CO2_output.png",
                "output-files/outputs/emissions.xml",
                "-i",
                "id");
    }

    public static int executeCommand(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0)
                exitCode = 1;
            return exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void configureSumo(ReleaseInfo releaseInfo) {
        try {
            SumoXMLObject routesConfig = sumoFileGateway.readDefaultConfig();
            routesConfig = changeConfigs(routesConfig, releaseInfo);
            sumoFileGateway.writeConfigs(routesConfig);
            log.info("Updated route config file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SumoXMLObject changeConfigs(SumoXMLObject routesConfig, ReleaseInfo releaseInfo) {
        int numberOfVehicles = releaseInfo.getFunctions().size();
        int maxIter = Math.min(numberOfVehicles, 10);

        for (int i = 0; i < maxIter; i++) {
            SumoXMLObject.Vehicle vehicleToUpdate = routesConfig.getVehicles().get(i);
            EcuDatasInfo ecuDatasInfo = releaseInfo
                    .getFunctions()
                    .get(i)
                    .getEcuDatas()
                    .get(0)
                    .getData();

            for (SumoXMLObject.Vehicle.Param param : vehicleToUpdate.getParams()) {
                if (param.getKey().equals("actualBatteryCapacity")) {
                    param.setValue(ecuDatasInfo.getActualBatteryCapacity());
                    break;
                }
            }
        }
        return routesConfig;
    }
}
