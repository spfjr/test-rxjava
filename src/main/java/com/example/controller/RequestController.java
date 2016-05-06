package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.DemoRequest;
import com.example.processor.RequestProcessor;

/**
 * @author Shaun Fleming
 */
@RestController
@Slf4j
public class RequestController
{
    @Resource private RequestProcessor processor;

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public ResponseEntity<String> doRequest(
            @RequestParam("id") final Integer requestId,
            @RequestParam("msg") final String message)
    {
        log.info("Received request: requestId={}", requestId);
        final DemoRequest request = new DemoRequest(requestId, message);
        this.processor.process(request);
        return new ResponseEntity<>("Processed request: requestId=" + request.getRequestId(), HttpStatus.OK);
    }
}
