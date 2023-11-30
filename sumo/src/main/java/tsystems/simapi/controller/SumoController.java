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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;


@RestController
@RequestMapping("/api")
public class SumoController {
    @Autowired SumoService sumoService;

    @PostMapping("/run-simulation")
    public Boolean runSimulation() {
        return sumoService.runSimulation("R000");
    }

    @PostMapping("/run-simulation-with-release-info")
    public Boolean runSimulationWithArgs(@Valid @RequestBody ReleaseInfo releaseInfo){
        sumoService.adjustBatteryToTemperature(releaseInfo);
        sumoService.configureSumo(releaseInfo);
        boolean status = sumoService.runSimulation(releaseInfo.getReleaseId());
        sumoService.updateStatus(status, releaseInfo.getReleaseId());
        return status;
    }

    @GetMapping("/{releaseId}")
    public ResponseEntity<byte[]> getImagesByReleaseId(@PathVariable String releaseId) throws IOException {
        return sumoService.getImage(releaseId);
    }
}
