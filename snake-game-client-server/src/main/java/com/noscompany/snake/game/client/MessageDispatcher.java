package com.noscompany.snake.game.client;

import com.noscompany.snake.game.commons.messages.events.decoder.MessageDecoder;
import lombok.AllArgsConstructor;
import org.atmosphere.wasync.Function;

@AllArgsConstructor
class MessageDispatcher implements Function<String> {
    private final ClientEventHandler eventHandler;
    private final MessageDecoder messageDecoder;

    @Override
    public void on(String json) {
        messageDecoder.onFailedStartGame(json, eventHandler::handle);
        messageDecoder.onFailedToChangeGameOptions(json, eventHandler::handle);
        messageDecoder.onFailedToJoinGame(json, eventHandler::handle);
        messageDecoder.onGameOptionsChanged(json, eventHandler::handle);
        messageDecoder.onChatMessageReceived(json, eventHandler::handle);
        messageDecoder.onPlayerTookASeat(json, eventHandler::handle);
        messageDecoder.onPlayerFreedUpASeat(json, eventHandler::handle);
        messageDecoder.onNewUserConnected(json, eventHandler::handle);
        messageDecoder.onNewUserConnectedAsAdmin(json, eventHandler::handle);
        messageDecoder.onUserDisconnected(json, eventHandler::handle);
        messageDecoder.onTimeLeftToGameStartHasChanged(json, eventHandler::handle);
        messageDecoder.onGameStarted(json, eventHandler::handle);
        messageDecoder.onGameContinues(json, eventHandler::handle);
        messageDecoder.onGameFinished(json, eventHandler::handle);
        messageDecoder.onGameCancelled(json, eventHandler::handle);
        messageDecoder.onGamePaused(json, eventHandler::handle);
        messageDecoder.onGameResumed(json, eventHandler::handle);
    }
}