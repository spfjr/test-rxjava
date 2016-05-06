package com.example.messaging;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.model.Stage1Request;
import com.example.model.Stage1Response;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class Stage1MessageManager
{
    @Resource private JmsMessagingTemplate jmsMessagingTemplate;

    private final Map<Integer, PublishSubject<Stage1Response>> publishSubjectMap = new HashMap<>();

    public Observable<Stage1Response> sendAndReceive(final Stage1Request request)
    {
        return Observable.defer(() -> Observable.just(request))
                .switchMap(this::send);
    }

    public Observable<Stage1Response> send(final Stage1Request request)
    {
        log.info("Sending Stage 1 request: requestId={}", request.getRequestId());

        final PublishSubject<Stage1Response> subject = PublishSubject.create();
        this.publishSubjectMap.put(request.getRequestId(), subject);

        this.jmsMessagingTemplate.convertAndSend("stage.1.in", request);

        return subject;
    }

    @JmsListener(destination = "stage.1.out")
    public void receive(final Stage1Response response)
    {
        log.info("Receiving Stage 1 response: requestId={}", response.getRequestId());
        final PublishSubject<Stage1Response> subject = this.publishSubjectMap.get(response.getRequestId());
        subject.onNext(response);
        subject.onCompleted();
    }
}
