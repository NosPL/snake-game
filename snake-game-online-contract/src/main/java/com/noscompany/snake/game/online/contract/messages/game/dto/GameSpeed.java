package com.noscompany.snake.game.online.contract.messages.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
public enum GameSpeed {
    x1(300),
    x2(200),
    x3(120),
    x4(70),
    x5(70),
    x6(70),
    x7(70),
    x8(70);

    final int pauseTimeInMillis;
}