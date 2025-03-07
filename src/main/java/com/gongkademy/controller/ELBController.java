package com.gongkademy.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Log4j2
public class ELBController {
    @GetMapping("/health")
    public HttpStatus healthCheck(){

        log.info("ELB Health Check 요청이 들어왔습니다.!");

        return HttpStatus.OK;
    }
}
