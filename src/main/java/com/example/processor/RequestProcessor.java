package com.example.processor;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.example.messaging.Stage1MessageManager;
import com.example.messaging.Stage2MessageManager;
import com.example.model.DemoRequest;
import com.example.model.Stage1Request;
import com.example.model.Stage2Request;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class RequestProcessor
{
    @Resource private Stage1Preprocessor stage1Preprocessor;
    @Resource private Stage1MessageManager stage1MessageManager;
    @Resource private Stage1Postprocessor stage1Postprocessor;
    @Resource private Stage2MessageManager stage2MessageManager;

    public void process(final DemoRequest demoRequest)
    {
        Observable.just(demoRequest)
                .map(demoReq -> new Stage1Request(demoReq.getRequestId(), demoReq.getMessage()))
                .switchMap(stage1Req -> this.stage1Preprocessor.preprocess(stage1Req))
                .switchMap(stage1Req -> this.stage1MessageManager.sendAndReceive(stage1Req))
                .switchMap(stage1Resp -> this.stage1Postprocessor.postprocess(stage1Resp))
                .switchMap(stage1Resp -> this.stage2MessageManager.sendAndReceive(stage1Resp))
                .subscribe(resp -> log.info(
                        "Completed request: requestId={}, message={}, objType={}",
                        resp.getRequestId(),
                        resp.getMessage(),
                        resp.getClass().getSimpleName()));
    }
}
