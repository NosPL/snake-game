package com.noscompany.snake.game.online.contract.messages.server;

import lombok.Value;

@Value
public class FailedToStartServer {
    Reason reason;

    public enum Reason {
        INCORRECT_IP_ADDRESS_FORMAT, PORT_IS_NOT_A_NUMBER, OTHER
    }
}