package com.example.messaging.external;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.model.Stage2Request;
import com.example.model.Stage2Response;
import com.example.util.ProcessorUtils;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class Stage2ExternalService
{
    @Resource private JmsMessagingTemplate jmsMessagingTemplate;

    @JmsListener(destination = "stage.2.in")
    public void receive(final Stage2Request request)
    {
        log.info("Processing Stage 2 request in external service: requestId={}", request.getRequestId());

        ProcessorUtils.randomSleep(5);

        final Stage2Response response = new Stage2Response(
                request.getRequestId(),
                request.getSubRequestId(),
                request.getMessage() + request.getCompositeId());
        this.jmsMessagingTemplate.convertAndSend("stage.2.out", response);
    }
}
