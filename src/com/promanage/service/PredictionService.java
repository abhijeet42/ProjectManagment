package com.promanage.service;

import java.util.Random;

public class PredictionService {

    // Simulated AI prediction
    // Returns true if high probability of better revenue projects next week
    public boolean isHighRevenueExpectedNextWeek() {

        // Hardcoded AI logic simulation

        Random random = new Random();
        int value = random.nextInt(100);

        // Suppose AI predicts:
        // 40% chance better revenue project coming next week

        if (value < 40) {
            System.out.println("AI Prediction: High revenue projects likely next week.");
            return true;
        } else {
            System.out.println("AI Prediction: No major revenue spike expected next week.");
            return false;
        }
    }
}
