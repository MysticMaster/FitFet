package com.example.mobile_project_01.function;

import java.util.Random;

public class AutoStringID {
    public static String createStringID(String s) {
        Random random = new Random();
        StringBuilder idBuilder = new StringBuilder(s);

        for (int i = 0; i < 8; i++) {
            int randomNumber = random.nextInt(10);
            idBuilder.append(randomNumber);
        }
        return idBuilder.toString();
    }
}
