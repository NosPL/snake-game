package com.noscompany.snake.game.online.remote.server;

import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import io.vavr.control.Try;

import java.util.Objects;

class ServerParamsCreator {

    static ServerParams toServerParams(String[] args) {
        if (args.length != 2)
            throw new RuntimeException("Wrong amount of input params. Expected: 2, actual: " + args.length);
        return new ServerParams(checkIfIp(args[0]), toPort(args[1]));
    }

    private static String checkIfIp(String ip) {
        Objects.requireNonNull(ip);
        return ip;
    }

    private static int toPort(String port) {
        return Try
                .of(() -> Integer.valueOf(port))
                .getOrElseThrow(() -> new RuntimeException("Failed to map second parameter to int that supposed to be port number. actual value: " + port));
    }
}