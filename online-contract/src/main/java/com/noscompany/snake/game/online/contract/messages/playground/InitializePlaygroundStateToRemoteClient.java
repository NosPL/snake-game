package com.noscompany.snake.game.online.contract.messages.playground;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class InitializePlaygroundStateToRemoteClient implements DedicatedClientMessage {
    OnlineMessage.MessageType messageType = OnlineMessage.MessageType.INITIALIZE_PLAYGROUND_STATE;
    UserId userId;
    PlaygroundState playgroundState;
}