package com.noscompany.snake.game.utils.monitored.executor.service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

import static com.noscompany.snake.game.utils.monitored.executor.service.NamedRunnable.namedTask;

@Slf4j
public class MonitoredExecutorServiceTest {
    private final ExecutorService executorService = new MonitoredExecutorServiceCreator().create();

    public static void main(String[] args) throws InterruptedException {
        new MonitoredExecutorServiceTest().startTest();
    }

    public void startTest() throws InterruptedException {
        Thread workingThread1 = new Thread(putTasksInQueue(executorService), "working-thread-1");
        Thread workingThread2 = new Thread(putTasksInQueue(executorService), "working-thread-2");
        workingThread1.start();
        workingThread2.start();
        workingThread1.join();
        workingThread2.join();
        executorService.shutdown();
    }

    private Runnable putTasksInQueue(ExecutorService executorService) {
        return () -> {
            for (int i = 0; i < 100; i++) {
                int sleepTime = ThreadLocalRandom.current().nextInt(5, 10);
                sleepInMillis(sleepTime);
                executorService.submit(namedTask("sample task nr. " + i, this::sampleTask));
            }
        };
    }

    private void sampleTask() {
        log.info("doing some business logic");
        sleepInMillis(8);
        maybeThrowSomeError();
    }

    private void maybeThrowSomeError() {
        int errorOnZero = ThreadLocalRandom.current().nextInt(0, 4);
        if (errorOnZero == 0) {
            throw new RuntimeException("hibernate/spring/tomcat/whatever error");
        }
    }

    private void sleepInMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}