package com.noscompany.snake.game.online.network.interfaces.analyzer;

import io.vavr.control.Try;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class JavaNetNetworkInterfacesAnalyzer implements NetworkInterfacesAnalyzer {

    @Override
    public Collection<IpV4Address> findIpV4Addresses() {
        return Try
                .of(this::doGetIpV4Addresses)
                .getOrElse(LinkedList::new);
    }

    private List<IpV4Address> doGetIpV4Addresses() throws SocketException {
        return networkInterfacesStream()
                .map(NetworkInterface::getInterfaceAddresses)
                .flatMap(List::stream)
                .map(InterfaceAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .filter(string -> string.contains("."))
                .map(IpV4Address::new)
                .toList();
    }

    private Stream<NetworkInterface> networkInterfacesStream() throws SocketException {
        Iterator<NetworkInterface> iterator = NetworkInterface.getNetworkInterfaces().asIterator();
        Iterable<NetworkInterface> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}