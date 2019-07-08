package com.sports.clip.sport;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sport")
@Validated
class SportController {

    private final SportRepository repository;


    SportController(SportRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<Sport>> all() {
        return ResponseEntity.ok(repository.findAll());
    }

}
