package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import io.javalin.websocket.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
class ServerEventHandler implements WsConnectHandler,WsMessageHandler, WsCloseHandler, WsErrorHandler {
    private final RoomMediatorForRemoteClients roomMediator;
    private final MessageDeserializer messageDeserializer;
    private final ConcurrentHashMap<String, WsContext> connectedUsersById;
    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) {
        String userId = wsConnectContext.getSessionId();
        wsConnectContext.session.setIdleTimeout(Duration.ZERO);
        connectedUsersById.put(userId, wsConnectContext);
        log.info("new user connected: {}", userId);
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) {
        RemoteClientId remoteClientId = new RemoteClientId(wsMessageContext.getSessionId());
        String serializedMessage = wsMessageContext.message();
        handleMessage(remoteClientId, serializedMessage);
    }

    private void handleMessage(RemoteClientId remoteClientId, String serializedMessage) {
        log.debug("User with id {} sent a message: {}", remoteClientId.getId(), serializedMessage);
        messageDeserializer
                .deserialize(remoteClientId, serializedMessage)
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(roomMediator))
                .onFailure(throwable -> log.warn("Failed to deserialize message, cause: {}", throwable.getMessage()));
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) {
        RemoteClientId remoteClientId = new RemoteClientId(wsCloseContext.getSessionId());
        roomMediator.removeClient(remoteClientId);
    }

    @Override
    public void handleError(@NotNull WsErrorContext wsErrorContext) {
        String sessionId = wsErrorContext.getSessionId();
        Throwable error = wsErrorContext.error();
        log.warn("User with id {} caused error: {}", sessionId, error.getMessage());
    }
}