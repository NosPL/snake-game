package com.noscompany.snake.game.online.host.pvp.server.internal.state;

import com.noscompany.snake.game.online.host.server.dto.IpAddress;
import io.vavr.control.Try;

public interface IpAddressChecker {
    Try<IpAddress> getIpAddress();
}