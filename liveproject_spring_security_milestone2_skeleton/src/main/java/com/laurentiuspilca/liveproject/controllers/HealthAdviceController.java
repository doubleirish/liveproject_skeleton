package com.laurentiuspilca.liveproject.controllers;

import com.laurentiuspilca.liveproject.controllers.dto.HealthAdvice;
import com.laurentiuspilca.liveproject.controllers.dto.HealthMetricDto;
import com.laurentiuspilca.liveproject.controllers.dto.HealthProfileDto;
import com.laurentiuspilca.liveproject.entities.HealthProfile;
import com.laurentiuspilca.liveproject.entities.enums.HealthMetricType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/advice")
public class HealthAdviceController {
    RestTemplate restTemplate = new RestTemplate();

    @Value("${app.advice.url}")
    private String url;


    @PreAuthorize("#oauth2.hasScope('advice')")
    @PostMapping
    public void provideHealthAdviceCallback(@RequestBody List<HealthAdvice> healthAdvice, Authentication authentication) {
        log.info("authentication name {} ", authentication.getName());
        log.info("authorities{} ", authentication.getAuthorities());


        healthAdvice.forEach(h -> {
            final String username = h.getUsername();
            log.info("Advice for: {}  Advice text: {}", username, h.getAdvice());


            // send a request for health advice -> wll cause circular set of requests
            sendRequestForAdvice(username);
        });

    }
@Async
    protected void sendRequestForAdvice(String username) {
        HealthMetricDto metric = new HealthMetricDto(HealthMetricType.BLOOD_OXYGEN_LEVEL,95.0);
        HealthProfileDto profile = new HealthProfileDto(username,List.of(metric));
        HttpEntity<List<HealthProfileDto>> profileList = new HttpEntity<>(List.of(profile));
        log.info("requesting advice for profile user{} from url {}", username, url);
        val responseEntity =
                restTemplate.postForEntity(url, profileList, Void.class);
        log.info("POST to  url {} returned {}",url, responseEntity.getStatusCode());
    }
}
