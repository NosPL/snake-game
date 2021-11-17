package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class FailedToAddUser implements MessageDto {
    MessageDto.MessageType messageType = MessageType.FAILED_TO_ADD_USER;
    String userId;
    Reason reason;

    public static FailedToAddUser userWithGivenIdAlreadyExists(String userId) {
        return new FailedToAddUser(userId, Reason.USER_WITH_GIVEN_ID_ALREADY_EXISTS);
    }

    public enum Reason {
        USER_WITH_GIVEN_ID_ALREADY_EXISTS
    }
}