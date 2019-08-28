package io.github.rusyasoft.example.tour.elprit.elpritreview.controller;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Event API", description = "주택 금융 서비스 API")
@RestController
public class EventController {

    @ApiOperation(value = "Event Add", response = ResponseEntity.class)
    @PostMapping(value = {"/events"})
    public ResponseEntity<String> addEvent(EventParam eventParam) {

        return ResponseEntity.ok("success");
    }


}
