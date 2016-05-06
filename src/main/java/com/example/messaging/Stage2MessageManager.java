package com.example.messaging;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.model.DemoResponse;
import com.example.model.Stage1Response;
import com.example.model.Stage2Request;
import com.example.model.Stage2Response;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class Stage2MessageManager
{
    @Resource private JmsMessagingTemplate jmsMessagingTemplate;

    private final Map<String, PublishSubject<Stage2Response>> publishSubjectMap = new HashMap<>();

    public Observable<DemoResponse> sendAndReceive(final Stage1Response stage1Response)
    {
        return Observable.defer(() -> Observable.from(splitRequest(stage1Response)))
                .flatMap(this::send)
                .reduce(
                        new DemoResponse(stage1Response.getRequestId(), ""),
                        (demoResp, stage2Resp) -> {
                            final String message = demoResp.getMessage() + " * " + stage2Resp.getMessage();
                            demoResp.setMessage(message);
                            return demoResp;
                        });
    }

    private List<Stage2Request> splitRequest(final Stage1Response stage1Response)
    {
        final List<String> messages = stage1Response.getMessages();
        final List<Stage2Request> requests = new ArrayList<>();

        for (int i = 0; i < messages.size(); i++)
        {
            requests.add(new Stage2Request(stage1Response.getRequestId(), Integer.valueOf(i), messages.get(i)));
        }

        return requests;
    }

    public Observable<Stage2Response> send(final Stage2Request request)
    {
        log.info("Sending Stage 2 request: requestId={}", request.getRequestId());

        final PublishSubject<Stage2Response> subject = PublishSubject.create();
        this.publishSubjectMap.put(request.getCompositeId(), subject);

        this.jmsMessagingTemplate.convertAndSend("stage.2.in", request);

        return subject;
    }

    @JmsListener(destination = "stage.2.out")
    public void receive(final Stage2Response response)
    {
        log.info("Receiving Stage 2 response: requestId={}", response.getRequestId());
        final PublishSubject<Stage2Response> subject = this.publishSubjectMap.remove(response.getCompositeId());

        subject.onNext(response);
        subject.onCompleted();
    }
}
