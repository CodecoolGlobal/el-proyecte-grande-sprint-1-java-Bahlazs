package com.codecool.grannymanager.controller;

import com.codecool.grannymanager.model.Granny;
import com.codecool.grannymanager.model.requestmodel.GrannyCreateRequest;
import com.codecool.grannymanager.model.requestmodel.GrannyGetRequest;
import com.codecool.grannymanager.service.GrannyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/granny")
public class GrannyController {
    private final GrannyService grannyService;

    @Autowired
    public GrannyController(GrannyService grannyService) {
        this.grannyService = grannyService;
    }

    @PostMapping("/create-granny")
    public ResponseEntity<Integer> createGranny(@RequestBody GrannyCreateRequest request) {
        int userId = request.getUserId();
        String name = request.getName();
        grannyService.createGranny(userId, name);
        return ResponseEntity.ok().body(userId);
    }

    @GetMapping("/visit-granny")
    public ResponseEntity<Granny> visitGranny(@RequestBody GrannyGetRequest request) {
        Granny granny = grannyService.visitGranny(request.getUserId());
        return ResponseEntity.ok().body(granny);
    }

    @GetMapping("/feed-pie")
    public ResponseEntity<Granny> feedPie(@RequestBody GrannyGetRequest request) {
        Granny granny = grannyService.feedPie(request.getUserId());
        return ResponseEntity.ok().body(granny);
    }

    @GetMapping("/play-mahjong")
    public ResponseEntity<Granny> playMahjong(@RequestBody GrannyGetRequest request) {
        Granny granny = grannyService.playMahjong(request.getUserId());
        return ResponseEntity.ok().body(granny);
    }

    @GetMapping("/clean-house")
    public ResponseEntity<Granny> cleanHouse(@RequestBody GrannyGetRequest request) {
        Granny granny = grannyService.cleanHouse(request.getUserId());
        return ResponseEntity.ok().body(granny);
    }

}
