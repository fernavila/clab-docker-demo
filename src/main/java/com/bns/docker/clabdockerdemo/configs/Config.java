package com.bns.docker.clabdockerdemo.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "myprefix")
public class Config {
    private String fixedProp = "Fixed Prop from config";
    private int genClassCount = 1;
    private int memToConsumeMb =  1;
    private boolean sleepThread;
    private String propFromConfigMap;
}
