package com.promanage.dao;

import com.promanage.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeeklyRevenueDAO {

    public List<Double> getAllRevenues() {

        List<Double> revenues = new ArrayList<>();

        String sql = "SELECT revenue FROM weekly_revenue ORDER BY week_number";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                revenues.add(rs.getDouble("revenue"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return revenues;
    }
}