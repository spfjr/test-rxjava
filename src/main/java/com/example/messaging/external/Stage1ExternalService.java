package com.example.messaging.external;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.model.Stage1Request;
import com.example.model.Stage1Response;
import com.example.util.ProcessorUtils;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class Stage1ExternalService
{
    @Resource private JmsMessagingTemplate jmsMessagingTemplate;

    @JmsListener(destination = "stage.1.in")
    public void receive(final Stage1Request request)
    {
        log.info("Processing Stage 1 request in external service: requestId={}", request.getRequestId());

        ProcessorUtils.randomSleep(5);

        final List<String> messages = new ArrayList<>(request.getMessages());
        messages.add("stage1-external");

        final Stage1Response response = new Stage1Response(
                request.getRequestId(),
                messages);
        this.jmsMessagingTemplate.convertAndSend("stage.1.out", response);
    }
}
