package com.vik.covid19vik;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controller {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting") // ensures HTTP GET requests to /greeting mapped to greeting() method
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) { // @RequestParam binds value of query string parameter name into name parameter of greeting(); if name absent, "World" used instead
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
