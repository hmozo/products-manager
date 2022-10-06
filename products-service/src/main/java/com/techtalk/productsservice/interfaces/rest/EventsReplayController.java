package com.techtalk.productsservice.interfaces.rest;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class EventsReplayController {

    @Autowired
    private EventProcessingConfiguration eventProcessingConfiguration;

    @PostMapping("eventProcessor/{processorName}/reset")
    public ResponseEntity<String> replayEvents(@PathVariable String processorName){
        var trackingEventProcessorOptional= eventProcessingConfiguration.eventProcessor(processorName, TrackingEventProcessor.class);
        if (trackingEventProcessorOptional.isPresent()){
            var trackingEventProcessor= trackingEventProcessorOptional.get();
            trackingEventProcessor.shutDown();
            trackingEventProcessor.resetTokens();
            trackingEventProcessor.start();

            return ResponseEntity.ok().body(String.format("The event processor [%s] has been reset ", processorName));
        }else{
            return ResponseEntity.badRequest().body(String.format("The event processor [%s] is not TEP", processorName));
        }
    }
}
