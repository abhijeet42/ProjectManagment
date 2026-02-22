package com.promanage.service;

import com.promanage.dao.WeeklyRevenueDAO;

import java.util.ArrayList;
import java.util.List;

public class PredictionService {

    private WeeklyRevenueDAO dao = new WeeklyRevenueDAO();

    public double[] predictNextWeek() {

        List<Double> rawData = dao.getAllRevenues();

        if (rawData.size() < 5) {
            throw new RuntimeException("Not enough data for prediction");
        }

        List<Double> smoothed = movingAverage(rawData, 3);

        double prediction = linearRegressionPredict(smoothed);

        double errorPercent = calculateErrorPercentage(smoothed);

        return new double[]{prediction, errorPercent};
    }

    //calculating moving average
    private List<Double> movingAverage(List<Double> data, int window) {

        List<Double> result = new ArrayList<>();

        for (int i = window - 1; i < data.size(); i++) {

            double sum = 0;

            for (int j = i - (window - 1); j <= i; j++) {
                sum += data.get(j);
            }

            result.add(sum / window);
        }

        return result;
    }

    //calculating linear regression
    private double linearRegressionPredict(List<Double> data) {

        int n = data.size();

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < n; i++) {

            double x = i + 1;
            double y = data.get(i);

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double denominator = (n * sumX2 - sumX * sumX);

        if (denominator == 0) {
            throw new RuntimeException("Cannot compute regression");
        }

        double m = (n * sumXY - sumX * sumY) / denominator;
        double b = (sumY - m * sumX) / n;

        double nextX = n + 1;

        return m * nextX + b;
    }

    //calculating error percentage
    private double calculateErrorPercentage(List<Double> smoothed) {

        int n = smoothed.size();

        List<Double> train = smoothed.subList(0, n - 1);

        double predicted = linearRegressionPredict(train);
        double actual = smoothed.get(n - 1);

        return Math.abs(actual - predicted) / actual * 100;
    }
}