package com.tsystems.dco.release.controller;

import com.tsystems.dco.release.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/update")
public class ReleaseUpdateController {

  private final ReleaseService releaseService;


}
