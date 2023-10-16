package com.noscompany.snake.game.online.remote.client.gui;

import com.noscompany.snake.game.online.client.HostAddress;
import io.vavr.control.Either;
import io.vavr.control.Try;

class HostAddressCreator {
    enum Error {
        WRONG_IP_FORMAT, WRONG_PORT_FORMAT
    }

    Either<Error, HostAddress> create(String ip1,
                                      String ip2,
                                      String ip3,
                                      String ip4,
                                      String port) {
        if (!isIpValid(ip1, ip2, ip3, ip4))
            return Either.left(Error.WRONG_IP_FORMAT);
        if (!isPortValid(port))
            return Either.left(Error.WRONG_PORT_FORMAT);
        return Either.right(toAddress(ip1, ip2, ip3, ip4, port));
    }

    private HostAddress toAddress(String ip1, String ip2, String ip3, String ip4, String port) {
        return new HostAddress(ip1 + "." + ip2 + "." + ip3 + "." + ip4 + ":" + port);
    }

    private boolean isIpValid(String ip1, String ip2, String ip3, String ip4) {
        return isIpPartValid(ip1) && isIpPartValid(ip2) && isIpPartValid(ip3) && isIpPartValid(ip4);
    }

    private boolean isIpPartValid(String ipPart) {
        return Try
                .of(() -> Integer.valueOf(ipPart))
                .map(this::isIpPartInRange)
                .getOrElse(false);
    }

    private boolean isIpPartInRange(Integer ipPart) {
        return ipPart >= 0 && ipPart < 256;
    }

    private boolean isPortValid(String port) {
        return Try
                .of(() -> Integer.valueOf(port))
                .filter(portInt -> portInt > 0)
                .isSuccess();
    }
}