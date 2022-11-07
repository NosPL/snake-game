package com.noscompany.snake.game.online.host.server.javalin.internal.state.running;

import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import io.javalin.websocket.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Slf4j
@AllArgsConstructor
class RoomMessageHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler, WsErrorHandler {
    private final RoomCommandHandlerForRemoteClients roomCommandHandler;
    private final MessageDeserializer messageDeserializer;
    private final Map<String, WsContext> connectedUsersById;

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        String userId = wsConnectContext.getSessionId();
        connectedUsersById.put(userId, wsConnectContext);
        log.info("new user connected: {}", userId);
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        RemoteClientId remoteClientId = new RemoteClientId(wsMessageContext.getSessionId());
        String serializedMessage = wsMessageContext.message();
        handleMessage(remoteClientId, serializedMessage);
    }

    private void handleMessage(RemoteClientId remoteClientId, String serializedMessage) {
        log.info("User with id {} sent a message: {}", remoteClientId.getId(), serializedMessage);
        messageDeserializer
                .deserialize(remoteClientId, serializedMessage)
                .onSuccess(deserializedMessage -> log.info("message deserialized, passing it to command handler"))
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(roomCommandHandler))
                .onFailure(t -> log.warn("Failed to deserialize message, cause: ", t));
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        connectedUsersById.remove(wsCloseContext.getSessionId());
        RemoteClientId remoteClientId = new RemoteClientId(wsCloseContext.getSessionId());
        roomCommandHandler.removeClient(remoteClientId);
    }

    @Override
    public void handleError(@NotNull WsErrorContext wsErrorContext) throws Exception {
        String sessionId = wsErrorContext.getSessionId();
        Throwable error = wsErrorContext.error();
        log.warn("User with id {} caused error: ", sessionId, error);
    }
}