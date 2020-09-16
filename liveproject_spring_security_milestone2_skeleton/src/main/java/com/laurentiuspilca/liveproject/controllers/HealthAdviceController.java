package com.laurentiuspilca.liveproject.controllers;

import com.laurentiuspilca.liveproject.controllers.dto.HealthAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/advice")
public class HealthAdviceController {


  @PreAuthorize("#oauth2.hasScope('advice')")
  @PostMapping
  public void provideHealthAdviceCallback(@RequestBody List<HealthAdvice> healthAdvice, Authentication authentication) {
    log.info("authentication name {} ", authentication.getName());
    log.info("authorities{} ", authentication.getAuthorities());
    healthAdvice.forEach(h -> log.info(
            "Advice for: {}  Advice text: {}",h.getUsername(), h.getAdvice()));
  }
}
