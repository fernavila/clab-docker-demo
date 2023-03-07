package com.bns.docker.clabdockerdemo.configs;

import com.bns.docker.clabdockerdemo.entities.Transit;
import com.bns.docker.clabdockerdemo.repositories.TransitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LoadData {

    @Bean
    CommandLineRunner initDatabase(TransitRepository repository){
        return args -> {
            log.info("Loading transit {}", repository.save(Transit.builder().transitNumber("1212").transitType("888").build()));
            log.info("Loading transit {}", repository.save(Transit.builder().transitNumber("1111").transitType("222").build()));
        };
    }
}
