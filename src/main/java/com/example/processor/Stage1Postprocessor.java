package com.example.processor;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.example.model.Stage1Response;
import com.example.util.ProcessorUtils;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class Stage1Postprocessor
{
    public Observable<Stage1Response> postprocess(final Stage1Response stage1Response)
    {
        log.info("Postprocessing Stage 1 Response");
        return Observable.timer(2, TimeUnit.SECONDS)
                .map(req -> new Stage1Response(
                        stage1Response.getRequestId(),
                        ProcessorUtils.copyListAndAdd(stage1Response.getMessages(), "stage1-postprocessor"))
                );
    }
}
