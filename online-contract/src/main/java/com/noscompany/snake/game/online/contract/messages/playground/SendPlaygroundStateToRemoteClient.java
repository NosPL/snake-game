package com.noscompany.snake.game.online.contract.messages.playground;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class SendPlaygroundStateToRemoteClient implements OnlineMessage {
    OnlineMessage.MessageType messageType = OnlineMessage.MessageType.USER_CONNECTED_TO_THE_SERVER;
    UserId remoteClientId;
    PlaygroundState playgroundState;
}