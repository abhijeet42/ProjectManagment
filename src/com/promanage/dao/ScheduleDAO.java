package com.promanage.dao;

import com.promanage.DBConnection;
import com.promanage.model.Project;
import com.promanage.service.PredictionService;

import java.sql.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScheduleDAO {

    ProjectDAO projectDAO = new ProjectDAO();
    PredictionService predictionService = new PredictionService();

    public void generateSchedule() {

        try (Connection cn = DBConnection.getConnection()) {

            // STEP 1: Ask AI first
            boolean shouldWait = predictionService.isHighRevenueExpectedNextWeek();

            if (shouldWait) {
                System.out.println("Decision: Skipping scheduling this week to wait for higher revenue projects.");
                return;
            }

            System.out.println("Decision: Proceeding with scheduling...");

            // STEP 2: Clear old schedule
            cn.createStatement().executeUpdate("DELETE FROM schedule");
            projectDAO.resetAllStatus();

            List<Project> projects = projectDAO.getAllProjects();

            if (projects.isEmpty()) {
                System.out.println("No projects found.");
                return;
            }

            int maxDeadline = 0;
            for (Project p : projects) {
                if (p.deadline > maxDeadline)
                    maxDeadline = p.deadline;
            }

            // Sort by revenue descending
            Collections.sort(projects, new Comparator<Project>() {
                public int compare(Project a, Project b) {
                    return b.revenue - a.revenue;
                }
            });

            boolean[] slots = new boolean[maxDeadline + 1];

            for (Project p : projects) {

                for (int d = p.deadline; d >= 1; d--) {

                    if (!slots[d]) {

                        PreparedStatement ps = cn.prepareStatement(
                                "INSERT INTO schedule(day_number, project_id) VALUES (?, ?)");

                        ps.setInt(1, d);
                        ps.setInt(2, p.id);
                        ps.executeUpdate();

                        projectDAO.updateStatus(p.id, "SCHEDULED");

                        slots[d] = true;
                        break;
                    }
                }
            }

            System.out.println("Schedule generated successfully.");

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
                System.out.println("Day " + rs.getInt("day_number") +
                        " -> " + rs.getString("title") +
                        " (Revenue: " + rs.getInt("revenue") + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
