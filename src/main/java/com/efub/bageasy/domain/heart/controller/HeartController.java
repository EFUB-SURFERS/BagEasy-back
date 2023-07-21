package com.efub.bageasy.domain.heart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class HeartController {

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public String create() {
        return "sample";
    }
}
