package com.home.traininfo.admin;

import com.home.traininfo.application.TrainDepartureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminController {

    private final String defaultStationCode;
    private final TrainDepartureService trainDepartureService;

    public AdminController(@Value("${stationUicCode}") String defaultStationCode,
                           TrainDepartureService trainDepartureService) {
        this.defaultStationCode = defaultStationCode;
        this.trainDepartureService = trainDepartureService;
    }

    @GetMapping("station")
    public ResponseEntity<Map<String, String>> setStationCode(@RequestParam String stationCode){
        trainDepartureService.setStationUicCode(stationCode);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("stationCode",stationCode));
    }

    @GetMapping("station/reset")
    public ResponseEntity<Map<String, String>> resetStationCode(){
        trainDepartureService.setStationUicCode(defaultStationCode);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("stationCode",defaultStationCode));
    }
}
