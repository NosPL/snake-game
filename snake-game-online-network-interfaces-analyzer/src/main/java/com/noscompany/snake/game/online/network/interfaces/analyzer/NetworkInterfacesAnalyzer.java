package com.noscompany.snake.game.online.network.interfaces.analyzer;

import java.util.Collection;

public interface NetworkInterfacesAnalyzer {
    Collection<IpV4Address> findIpV4Addresses();
}