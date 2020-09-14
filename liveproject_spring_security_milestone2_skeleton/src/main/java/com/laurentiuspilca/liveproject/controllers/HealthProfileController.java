package com.laurentiuspilca.liveproject.controllers;

import com.laurentiuspilca.liveproject.entities.HealthProfile;
import com.laurentiuspilca.liveproject.services.HealthProfileService;
// TODO import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/profile")
public class HealthProfileController {

  private final HealthProfileService healthProfileService;

  public HealthProfileController(HealthProfileService healthProfileService) {
    this.healthProfileService = healthProfileService;
  }

  @PostMapping
  public void addHealthProfile(@RequestBody HealthProfile healthProfile, Authentication authentication) {
    log.info("profile-user={}, authentication-user={} ",healthProfile.getUsername(), authentication.getPrincipal());
    healthProfileService.addHealthProfile(healthProfile);
  }

  @GetMapping("/{username}")
  public HealthProfile findHealthProfile(@PathVariable String username, Authentication authentication) {
    log.info("profile-user={}, authentication-user={} ",username, authentication.getPrincipal());
    return healthProfileService.findHealthProfile(username);
  }

  @DeleteMapping("/{username}")
  // TODO re-enable after adding security
  // public void deleteHealthProfile(@PathVariable String username, Authentication a) {
  public void deleteHealthProfile(@PathVariable String username ) {
    healthProfileService.deleteHealthProfile(username);
  }
}
