package com.noscompany.snake.game.commons;

public class Ipv4Validator {
    public static boolean isValid(String ipv4) {
        try {
            final String[] octets = ipv4.split("\\.");
            validateOctet(octets[0]);
            validateOctet(octets[1]);
            validateOctet(octets[2]);
            validateOctet(octets[3]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void validateOctet(String octet) {
        validateOctet(Integer.parseInt(octet));
    }

    private static void validateOctet(int octet) {
        if (octet > 255 || octet < 0)
            throw new RuntimeException();
    }
}