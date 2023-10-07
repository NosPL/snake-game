package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.server.StartServer;
import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SnakeOnlineHostImpl implements SnakeOnlineHost {
    private final UserId hostId;
    private final RoomApiForHost roomApiForHost;

    @Override
    public void startServer(ServerParams serverParams) {
        roomApiForHost.startServer(new StartServer(serverParams));
    }

    @Override
    public void enterRoom(UserName userName) {
        roomApiForHost.enter(new EnterRoom(hostId, userName.getName()));
    }

    @Override
    public void sendChatMessage(String messageContent) {
        roomApiForHost.sendChatMessage(new SendChatMessage(hostId, messageContent));
    }

    @Override
    public void cancelGame() {
        roomApiForHost.cancelGame(new CancelGame(hostId));
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        roomApiForHost.changeSnakeDirection(new ChangeSnakeDirection(hostId, direction));
    }

    @Override
    public void pauseGame() {
        roomApiForHost.pauseGame(new PauseGame(hostId));
    }

    @Override
    public void resumeGame() {
        roomApiForHost.resumeGame(new ResumeGame(hostId));
    }

    @Override
    public void startGame() {
        roomApiForHost.startGame(new StartGame(hostId));
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        roomApiForHost.changeGameOptions(new ChangeGameOptions(hostId, gridSize, gameSpeed, walls));
    }

    @Override
    public void freeUpASeat() {
        roomApiForHost.freeUpASeat(new FreeUpASeat(hostId));
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        roomApiForHost.takeASeat(new TakeASeat(hostId, playerNumber));
    }

    @Override
    public void shutDownServer() {
        roomApiForHost.shutdown();
    }
}