package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediator;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.server.Server;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.concurrent.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class PeriodicRoomStateUpdaterForRemoteClients {
    private final Server server;
    private final RoomMediatorForRemoteClients roomMediator;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final Duration duration;
    private volatile boolean stop;

    void start() {
        threadPoolExecutor.submit(this::updateRemoteClientsAboutRoomStatePeriodically);
    }

    void stop() {
        stop = true;
    }

    private void updateRemoteClientsAboutRoomStatePeriodically() {
        while (!stop) {
            sleep();
            send(roomMediator.getRoomState());
        }
    }

    private void send(RoomState roomState) {

    }

    private void sleep() {
        try {
            Thread.sleep(duration.toMillis());
        } catch (Throwable e) {

        }
    }

    static PeriodicRoomStateUpdaterForRemoteClients create(Server server, RoomMediator roomMediator, Duration duration) {
        return new PeriodicRoomStateUpdaterForRemoteClients(server, roomMediator, threadPool(), duration, false);
    }

    private static ThreadPoolExecutor threadPool() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setDaemon(true);
                    return thread;
                });
    }
}