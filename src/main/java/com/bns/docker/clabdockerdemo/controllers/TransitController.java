package com.bns.docker.clabdockerdemo.controllers;

import com.bns.docker.clabdockerdemo.configs.Config;
import com.bns.docker.clabdockerdemo.entities.Transit;
import com.bns.docker.clabdockerdemo.exceptions.TransitNotFoundException;
import com.bns.docker.clabdockerdemo.javassist.DynamicClassGen;
import com.bns.docker.clabdockerdemo.repositories.TransitRepository;
import com.bns.docker.clabdockerdemo.utils.MemoryConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class TransitController {

    TransitRepository repository;

    @Autowired
    Config config;

    @Value("${myprefix.configMapPropHot:defaultTransitController}")
    private String configMapPropHot;

    TransitController(TransitRepository repository){
        this.repository = repository;
    }

    @GetMapping("/getconfig")
    public String getConfig() {
        log.info("Get Config called");
        return "\ngetFixedProp: "+config.getFixedProp()+
                "\nconfigMapPropHot: "+configMapPropHot+
                "\ngetPropFromConfigMap: "+config.getPropFromConfigMap()+
                "\n\n";
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

    @RequestMapping(value = {"/makeLoadRuntime", "/makeLoadRuntime/{classCount}/{memByClass}"})
    public String makeLoadRuntime(
			@PathVariable("classCount") Optional<Integer> classCount,
			@PathVariable("memByClass") Optional<Integer> memByClass) {

		int genClassCount = config.getGenClassCount();
		int memToConsumeMb = config.getMemToConsumeMb();

		if(classCount.isPresent()) {
			genClassCount = classCount.get();
		}
		
		if(memByClass.isPresent()) {
			memToConsumeMb = memByClass.get();
		}

		log.info("Param  genClassCount : {}", genClassCount);
		log.info("Param  memToConsumeMb : {}", memToConsumeMb);
		
		long totalMemory ;
        long freeMemory;
        long usedMemory;
        int MB = 1024 * 1024;
	
		Runtime rt = Runtime.getRuntime();
		rt.gc();
		
		totalMemory = rt.totalMemory() / MB;
        freeMemory = rt.freeMemory() / MB;
        usedMemory = totalMemory - freeMemory;
		
		log.info("Before  totalMemory : {}", totalMemory);
		log.info("Before  freeMemory : {}", freeMemory);
		log.info("Before  usedMemory : {}", usedMemory);
		
		Vector cache = new Vector();
		
		byte[] arrByte = new byte[genClassCount];
        for(int i = 0; i < genClassCount; i++) {
        	cache.add(new byte[memToConsumeMb*MB]);
        }
        
        totalMemory = rt.totalMemory() / MB;
        freeMemory = rt.freeMemory() / MB;
        usedMemory = totalMemory - freeMemory;
		
		log.info("After  totalMemory : {}", totalMemory);
		log.info("After  freeMemory : {}", freeMemory);
		log.info("After  usedMemory : {}", usedMemory);
        log.info("After  vector.size() : {}", cache.size());
		
		return " success " ;
    }

    @RequestMapping(value = {"/makeLoadClassGen", "/makeLoadClassGen/{classCount}/{memByClass}/{sleepProcess}"})
    public String makeLoadClassGen(
            @PathVariable("classCount") Optional<Integer> classCount,
            @PathVariable("memByClass") Optional<Integer> memByClass,
            @PathVariable("sleepProcess") Optional<Boolean> sleepProcess) {

        int genClassCount = config.getGenClassCount();
        int memToConsumeMb = config.getMemToConsumeMb();
        boolean sleepThread = config.isSleepThread();

        if (classCount.isPresent()) {
            genClassCount = classCount.get();
        }

        if (memByClass.isPresent()) {
            memToConsumeMb = memByClass.get();
        }

        if (sleepProcess.isPresent()) {
            sleepThread = sleepProcess.get();
        }

        log.info("Param  genClassCount : {}", genClassCount);
        log.info("Param  memToConsumeMb : {}", memToConsumeMb);
        log.info("Param  sleepThread : {}", sleepThread);

        try {
            DynamicClassGen d = new DynamicClassGen();
            d.generateLoad(genClassCount, memToConsumeMb, sleepThread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return " success " ;
    }

    /*
    @GetMapping("/getMemoryConsumerInfoClean")
    public String getMemoryConsumerInfoClean() {
        MemoryConsumer.clean();
        return "Ejecutado con exito";
    }

    @GetMapping("/getMemoryConsumerInfo")
    public String getMemoryConsumerInfo() {
        MemoryConsumer.getMemInfo();
        return "Ejecutado con exito";
    }
    */
}
