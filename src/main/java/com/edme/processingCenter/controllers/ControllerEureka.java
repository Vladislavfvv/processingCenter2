package com.edme.processingCenter.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerEureka {
    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }
}
