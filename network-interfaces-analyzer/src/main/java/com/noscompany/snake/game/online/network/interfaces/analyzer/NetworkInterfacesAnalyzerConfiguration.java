package com.noscompany.snake.game.online.network.interfaces.analyzer;

public class NetworkInterfacesAnalyzerConfiguration {

    public NetworkInterfacesAnalyzer createNetworkInterfacesAnalyzer() {
        return new JavaNetNetworkInterfacesAnalyzer();
    }
}