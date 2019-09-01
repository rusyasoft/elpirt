package io.github.rusyasoft.example.tour.elprit.elpritreview.controller;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ActionType;
import io.github.rusyasoft.example.tour.elprit.elpritreview.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Event API", description = "주택 금융 서비스 API")
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @ApiOperation(value = "Event Add", response = ResponseEntity.class)
    @PostMapping(value = {"/events"})
    public ResponseEntity<User> postEvent(@RequestBody EventParam eventParam) {

        User user = eventService.onEvent(eventParam);

        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Event Modify", response = ResponseEntity.class)
    @PutMapping(value = {"/events"})
    public ResponseEntity<User> putEvent(@RequestBody EventParam eventParam) {

        eventParam.setAction(ActionType.MOD);
        User user = eventService.onEvent(eventParam);

        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Event Delete", response = ResponseEntity.class)
    @DeleteMapping(value = {"/events"})
    public ResponseEntity<User> deleteEvent(@RequestBody EventParam eventParam) {

        eventParam.setAction(ActionType.DELETE);
        User user = eventService.onEvent(eventParam);

        return ResponseEntity.ok(user);
    }





}
