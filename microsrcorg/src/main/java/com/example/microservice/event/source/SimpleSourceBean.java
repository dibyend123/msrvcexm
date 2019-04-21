package com.example.microservice.event.source;


import com.example.microservice.event.model.OrganizationChangeModel;
import org.springframework.stereotype.Component;
import com.example.microservice.utils.UserContext;
import com.example.microservice.utils.UserContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class SimpleSourceBean{

    private Source source;

	@Autowired
        public SimpleSourceBean(Source source){
           this.source = source;
        }

   public void publishOrgChange(String action,String orgId){
              
       OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                orgId,
                UserContextHolder.getContext().getCorrelationId());

	source.output().send(MessageBuilder.withPayload(change).build());
        
   }
}
