package com.jc.projects;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class Controller {

    //private static final Logger log = LoggerFactory
    @GetMapping("/")
    String getHome(){
        log.info("Controller");
        return "Application running...";
    }
}
