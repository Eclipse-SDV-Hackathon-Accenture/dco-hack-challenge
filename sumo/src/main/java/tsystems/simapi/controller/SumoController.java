package tsystems.simapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsystems.simapi.entity.releaseinfo.ReleaseInfo;
import tsystems.simapi.service.SumoService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class SumoController {
    @Autowired SumoService sumoService;

    @PostMapping("/run-simulation")
    public Boolean runSimulation() {
        return sumoService.runSimulation();
    }

    @GetMapping(value = "/run-simulation-with-release-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean runSimulationWithArgs(@Valid @RequestBody ReleaseInfo releaseInfo){
        sumoService.adjustBatteryToTemperature(releaseInfo);
        //create json
        //add releaseInfoId
        sumoService.configureSumo(releaseInfo);
        //add exec status sumoService.runSimulation();
        sumoService.getPlotImages();
        //return json

        return true;
    }

    @GetMapping(value = "/img", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, List<Object>>> getPlotImages() throws IOException {
        return sumoService.getPlotImages();
    }
}
