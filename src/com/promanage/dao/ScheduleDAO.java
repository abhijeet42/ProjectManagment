package com.promanage.dao;

import com.promanage.DBConnection;
import com.promanage.model.Project;
import com.promanage.service.PredictionService;

import java.sql.*;
import java.util.List;

public class ScheduleDAO {

    ProjectDAO projectDAO = new ProjectDAO();
    PredictionService predictionService = new PredictionService();

    // Returns true if day is Monday–Friday
    private boolean isWorkingDay(int dayNumber) {
        int dayOfWeek = dayNumber % 7;

        // Adjust because day 1 = Monday
        if (dayOfWeek == 0) dayOfWeek = 7;

        return dayOfWeek >= 1 && dayOfWeek <= 5;
    }

    // If deadline falls on weekend, move back to Friday
    private int adjustToWorkingDay(int deadline) {

        while (deadline > 0 && !isWorkingDay(deadline)) {
            deadline--;
        }

        return deadline;
    }

    //helper to get days
    private String getDayName(int dayNumber) {

        String[] days = {
                "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday",
                "Saturday (Planning Day)",
                "Sunday (Planning Day)"
        };

        int index = (dayNumber - 1) % 7;
        return days[index];
    }

    public void generateSchedule() {

        try (Connection cn = DBConnection.getConnection()) {

            // STEP 1: Fetch all projects
            List<Project> allProjects = projectDAO.getAllProjects();

            if (allProjects.isEmpty()) {
                System.out.println("No projects available.");
                return;
            }

            // 🔥 STEP 1.5: Prediction Layer
            double[] result = predictionService.predictNextWeek();
            double predictedRevenue = result[0];
            double errorPercent = result[1];

            double margin = predictedRevenue * (errorPercent / 100.0);

            System.out.println("\n--- MARKET ANALYSIS ---");
            System.out.println("Predicted Next Week Revenue: " + predictedRevenue);
            System.out.println("Model Error Percentage: " + errorPercent + "%");
            System.out.println("------------------------\n");

            // 🔥 STEP 2: Split Projects (NEW LOGIC)
            List<Project> currentWeek = new java.util.ArrayList<>();
            List<Project> futureWeek = new java.util.ArrayList<>();

            for (Project p : allProjects) {
                if (p.deadline <= 5) {
                    currentWeek.add(p);
                } else {
                    futureWeek.add(p);
                }
            }

            // 🔥 STEP 3: Apply prediction only to future projects
            List<Project> acceptedFuture = new java.util.ArrayList<>();

            for (Project p : futureWeek) {

                if (predictedRevenue - p.revenue <= margin) {
                    acceptedFuture.add(p);
                } else {
                    System.out.println("Future Project '" + p.title +
                            "' rejected by prediction.");
                    projectDAO.updateStatus(p.id, "POSTPONED");
                }
            }

            // 🔥 STEP 4: Edge Case Replacement Logic
            for (Project future : futureWeek) {

                if (!acceptedFuture.contains(future)) {

                    for (Project current : currentWeek) {

                        if (future.revenue > current.revenue) {

                            System.out.println("Replaced '" + current.title +
                                    "' with higher revenue future project '" +
                                    future.title + "'.");

                            currentWeek.remove(current);
                            currentWeek.add(future);
                            break;
                        }
                    }
                }
            }

            // 🔥 STEP 5: Final Pool for Greedy
            List<Project> finalProjects = new java.util.ArrayList<>();
            finalProjects.addAll(currentWeek);
            finalProjects.addAll(acceptedFuture);

            System.out.println("Proceeding with greedy scheduling...");

            // STEP 6: Clear old schedule
            cn.createStatement().executeUpdate("DELETE FROM schedule");
            projectDAO.resetAllStatus();

            // STEP 7: Find maximum deadline
            int maxDeadline = 0;
            for (Project p : finalProjects) {
                if (p.deadline > maxDeadline)
                    maxDeadline = p.deadline;
            }

            // STEP 8: Sort by revenue descending
            finalProjects.sort((a, b) -> Integer.compare(b.revenue, a.revenue));

            // STEP 9: Create slot array
            boolean[] slots = new boolean[maxDeadline + 1];

            // STEP 10: Greedy Scheduling
            for (Project p : finalProjects) {

                int adjustedDeadline = adjustToWorkingDay(p.deadline);

                for (int d = adjustedDeadline; d >= 1; d--) {

                    if (isWorkingDay(d) && !slots[d]) {

                        try (PreparedStatement ps = cn.prepareStatement(
                                "INSERT INTO schedule(day_number, project_id) VALUES (?, ?)")) {

                            ps.setInt(1, d);
                            ps.setInt(2, p.id);
                            ps.executeUpdate();
                        }

                        projectDAO.updateStatus(p.id, "SCHEDULED");
                        slots[d] = true;
                        break;
                    }
                }
            }

            System.out.println("\nSchedule generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewSchedule() {

        try (Connection cn = DBConnection.getConnection()) {

            String sql = "SELECT s.day_number, p.title, p.revenue " +
                    "FROM schedule s JOIN projects p ON s.project_id = p.id " +
                    "ORDER BY s.day_number";

            ResultSet rs = cn.createStatement().executeQuery(sql);

            System.out.println("\n--- FINAL SCHEDULE ---");

            while (rs.next()) {

                int dayNumber = rs.getInt("day_number");
                String dayName = getDayName(dayNumber);

                System.out.println("Day " + dayNumber + " (" + dayName + ")" +
                        " -> " + rs.getString("title") +
                        " (Revenue: " + rs.getInt("revenue") + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
