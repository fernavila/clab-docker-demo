package com.bns.docker.clabdockerdemo.controllers;

import com.bns.docker.clabdockerdemo.entities.Transit;
import com.bns.docker.clabdockerdemo.exceptions.TransitNotFoundException;
import com.bns.docker.clabdockerdemo.repositories.TransitRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransitController {

    TransitRepository repository;

    TransitController(TransitRepository repository){
        this.repository = repository;
    }

    @GetMapping(value = "/transits", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Transit> getAll(){
        return repository.findAll();
    }

    @GetMapping("/transits/{id}")
    Transit transitById(@PathVariable Long id){
        return repository.findById(id).orElseThrow(()-> new TransitNotFoundException(id));
    }

    @DeleteMapping("/transits/{id}")
    void deleteTransit(@PathVariable Long id){
        repository.deleteById(id);
    }

    @PostMapping("/transits")
    Transit newTransit(@RequestBody Transit newTransit){
        return repository.save(newTransit);
    }

    @PutMapping("/transits/{id}")
    Transit newTransit(@RequestBody Transit newTransit, @PathVariable Long id){
        return repository.findById(id)
                .map(transit -> {
                    transit.setTransitNumber(newTransit.getTransitNumber());
                    transit.setTransitType(newTransit.getTransitType());
                    return repository.save(transit);
                })
                .orElseGet(() -> {
                    newTransit.setId(id);
                    return repository.save(newTransit);
                });
    }
}
