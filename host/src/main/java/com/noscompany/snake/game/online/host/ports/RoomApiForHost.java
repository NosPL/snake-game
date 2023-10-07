package com.noscompany.snake.game.online.host.ports;

import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.StartServer;
import lombok.Value;

public interface RoomApiForHost {
    void startServer(StartServer command);
    void enter(EnterRoom command);
    void sendChatMessage(SendChatMessage command);
    void cancelGame(CancelGame command);
    void changeSnakeDirection(ChangeSnakeDirection command);
    void pauseGame(PauseGame command);
    void resumeGame(ResumeGame command);
    void startGame(StartGame command);
    void changeGameOptions(ChangeGameOptions command);
    void freeUpASeat(FreeUpASeat command);
    void takeASeat(TakeASeat command);
    void shutdown();
    @Value
    class HostId {
        String id;
    }
}
