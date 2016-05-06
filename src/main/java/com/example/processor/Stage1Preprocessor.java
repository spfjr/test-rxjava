package com.example.processor;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.example.model.Stage1Request;
import com.example.util.ProcessorUtils;

/**
 * @author Shaun Fleming
 */
@Component
@Slf4j
public class Stage1Preprocessor
{
    public Observable<Stage1Request> preprocess(final Stage1Request stage1Request)
    {
        log.info("Preprocessing Stage 1 Request");
        return Observable.timer(2, TimeUnit.SECONDS)
                .map(req -> new Stage1Request(
                        stage1Request.getRequestId(),
                        ProcessorUtils.copyListAndAdd(stage1Request.getMessages(), "stage1-preprocessor"))
                );
    }
}
