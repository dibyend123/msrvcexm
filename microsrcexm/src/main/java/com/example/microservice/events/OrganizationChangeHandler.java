package com.example.microservice.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import com.example.microservice.events.model.OrganizationChangeModel;

@EnableBinding(CustomChannels.class)
public class OrganizationChangeHandler{

    @StreamListener("inboundOrgChanges")
    public void loggerSink(OrganizationChangeModel orgChange) {

	System.out.println("Received a message of type " + orgChange.getType());
        
    }
}
