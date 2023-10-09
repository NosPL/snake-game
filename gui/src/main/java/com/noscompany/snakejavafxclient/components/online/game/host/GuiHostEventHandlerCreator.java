package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snakejavafxclient.utils.Controllers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class GuiHostEventHandlerCreator {

    GuiHostEventHandler create(UserId userId) {
        try {
            return new GuiHostEventHandler(
                    Controllers.get(FleetingMessageController.class),
                    Controllers.get(SetupHostController.class),
                    Controllers.get(ServerController.class),
                    Controllers.get(OnlineGameOptionsController.class),
                    Controllers.get(LobbySeatsController.class),
                    Controllers.get(GameGridController.class),
                    Controllers.get(ChatController.class),
                    Controllers.get(JoinedUsersController.class),
                    Controllers.get(MessageController.class),
                    Controllers.get(ScoreboardController.class),
                    Controllers.get(ScprButtonsController.class),
                    userId);
        } catch (Throwable throwable) {
            log.error("Failed to create gui host event handler, cause:", throwable);
            throw throwable;
        }
    }
}