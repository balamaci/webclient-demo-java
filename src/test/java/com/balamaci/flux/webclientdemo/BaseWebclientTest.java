package com.balamaci.flux.webclientdemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author sbalamaci
 */
@Slf4j
public class BaseWebclientTest {

    protected  <T> Consumer<? super T> logNext() {
        return (Consumer<T>) val -> log.info("Subscriber received: {}", val);
    }

    protected Consumer<? super Throwable> logError(CountDownLatch latch) {
        return err -> {
            log.error("Subscriber received error '{}'", err.getMessage());
            latch.countDown();
        };
    }

    protected Runnable logComplete(CountDownLatch latch) {
        return () -> {
            log.info("Subscriber got Completed event");
            latch.countDown();
        };
    }


}
