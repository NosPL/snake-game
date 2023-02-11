package com.noscompany.snake.game.online.contract.messages.game.options;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameOptionsChanged implements OnlineMessage {
    MessageType messageType = MessageType.GAME_OPTIONS_CHANGED;
    PlaygroundState playgroundState;
}