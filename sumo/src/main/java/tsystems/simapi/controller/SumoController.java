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

@RestController
@RequestMapping("/api")
public class SumoController {
    @Autowired SumoService sumoService;

    @PostMapping("/run-simulation")
    public Boolean runSimulation() {
        return sumoService.runSimulation();
    }

    @PostMapping("/run-simulation-with-release-info")
    public Boolean runSimulationWithArgs(@Valid @RequestBody ReleaseInfo releaseInfo){
        sumoService.configureSumo(releaseInfo);
        return sumoService.runSimulation();
    }
}
