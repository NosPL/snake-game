package com.noscompany.snake.game.commons.online.client;

import com.noscompany.snake.game.commons.messages.commands.chat.SendChatMessage;
import com.noscompany.snake.game.commons.messages.commands.game.*;
import com.noscompany.snake.game.commons.messages.commands.lobby.ChangeGameOptions;
import com.noscompany.snake.game.commons.messages.commands.lobby.FreeUpASeat;
import com.noscompany.snake.game.commons.messages.commands.lobby.TakeASeat;
import com.noscompany.snake.game.commons.messages.commands.room.EnterRoom;

public interface ServerCommandSender {

    void send(EnterRoom command);

    void send(TakeASeat command);

    void send(FreeUpASeat command);

    void send(ChangeGameOptions command);

    void send(StartGame command);

    void send(ChangeSnakeDirection command);

    void send(CancelGame command);

    void send(PauseGame command);

    void send(ResumeGame command);

    void send(SendChatMessage command);
}