package com.noscompany.snakejavafxclient;

public class ApplicationProfile {
    public enum Profile {
        PROD, TEST
    }
    public static Profile profile = Profile.PROD;
}