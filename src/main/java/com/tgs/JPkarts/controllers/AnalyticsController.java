package com.tgs.JPkarts.controllers;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import com.tgs.JPkarts.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tgs/rmkarts/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/")
    public ResponseEntity<List<AnalyticsEntity>> listAnalytics(){
        List<AnalyticsEntity> analytics = analyticsService.getAllAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
