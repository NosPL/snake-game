package com.noscompany.snake.game.online.host.server.message.handler.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.commands.StartGame;
import com.noscompany.snake.game.online.contract.messages.lobby.command.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.lobby.command.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.command.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.server.message.handler.RoomMessageHandlerCreator;
import com.noscompany.snake.game.online.host.server.message.handler.RoomMessageHandler;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Before;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

import java.util.*;

public abstract class BaseTestClass {
    private Map<String, AtmosphereResourceMock> resources;
    private MockBroadcaster mockBroadcaster;
    private RoomMessageHandler roomMessageHandler;
    private ObjectMapper objectMapper;
    protected String userId;
    protected String userName;

    @Before
    public void setup() {
        userId = randomUserId();
        userName = randomValidUserName();
        resources = new HashMap<>();
        mockBroadcaster = new MockBroadcaster();
        roomMessageHandler = RoomMessageHandlerCreator.create(mockBroadcaster);
        objectMapper = ObjectMapperCreator.createInstance();
    }

    protected void enterTheRoom(String userId, String userName) {
        send(userId, new EnterRoom(userName));
    }

    protected void takeASeat(String userId, PlayerNumber playerNumber) {
        send(userId, new TakeASeat(playerNumber));
    }

    protected void changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        send(userId, new ChangeGameOptions(gridSize, gameSpeed, walls));
    }

    protected void freeUpASeat(String userId) {
        send(userId, new FreeUpASeat());
    }

    protected void sendChatMessage(String userId, String messageContent) {
        send(userId, new SendChatMessage(messageContent));
    }

    protected void startGame(String userId) {
        send(userId, new StartGame());
    }

    protected void send(String userId, OnlineMessage onlineMessage) {
        var atmosphereResourceMock = resources.computeIfAbsent(userId, AtmosphereResourceMock::create);
        roomMessageHandler.handle(atmosphereResourceMock, serialize(onlineMessage));
    }

    protected void removeUserById(String userId) {
        roomMessageHandler.removeUserById(userId);
    }

    protected Set<String> roomUsers() {
        return getRoomState().getUsers();
    }

    protected PlayerNumber anyFreeSeatNumber() {
        return getRoomState()
                .getLobbyState()
                .getSeats()
                .stream()
                .filter(seat -> !seat.isTaken())
                .map(LobbyState.Seat::getPlayerNumber)
                .findFirst()
                .get();
    }

    protected LobbyState lobbyState() {
        return getRoomState().getLobbyState();
    }

    protected Messages getResourceMessagesById(String userId) {
        return findResourceById(userId)
                .map(AtmosphereResourceMock::getMessages)
                .getOrElse(Messages.empty());
    }

    private Option<AtmosphereResourceMock> findResourceById(String userId) {
        return Option.of(resources.get(userId));
    }

    protected Messages getAllResourceMessages() {
        return resources
                .values().stream()
                .map(AtmosphereResourceMock::getMessages)
                .reduce(Messages.empty(), Messages::addAll);
    }

    protected Messages getBroadcasterMessages() {
        return mockBroadcaster.getMessages();
    }

    protected RoomState getRoomState() {
        return roomMessageHandler.getRoomState();
    }

    protected String randomValidUserName() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    protected String randomUserId() {
        return UUID.randomUUID().toString();
    }

    @SneakyThrows
    private String serialize(OnlineMessage onlineMessage) {
        return objectMapper.writeValueAsString(onlineMessage);
    }
}