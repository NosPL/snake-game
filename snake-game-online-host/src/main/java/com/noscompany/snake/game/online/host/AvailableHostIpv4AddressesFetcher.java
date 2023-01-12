package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.host.server.dto.IpAddress;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.LinkedList;
import java.util.List;

public class AvailableHostIpv4AddressesFetcher {
    public List<IpAddress> fetch() {
        try {
            var iterator = NetworkInterface.getNetworkInterfaces().asIterator();
            var result = new LinkedList<IpAddress>();
            while (iterator.hasNext()) {
                List<IpAddress> ipAddresses = iterator.next()
                        .getInterfaceAddresses().stream()
                        .map(InterfaceAddress::getAddress)
                        .map(InetAddress::getHostAddress)
                        .filter(string -> string.contains("."))
                        .map(IpAddress::new)
                        .toList();
                result.addAll(ipAddresses);
            }
            return result;
        } catch (Throwable t) {
            return new LinkedList<>();
        }
    }
}