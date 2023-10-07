package com.noscompany.snake.game.online.contract.messages.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ServerFailedToSendMessageToRemoteClients {
    Reason reason;

    public enum Reason {
        CONNECTION_ISSUES,
        SERVER_IS_NOT_STARTED,
        FAILED_TO_SERIALIZE_MESSAGE,
    }
}