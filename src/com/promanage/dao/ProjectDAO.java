package com.promanage.dao;

import com.promanage.DBConnection;
import com.promanage.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {


    public void addProject(Project p) {
        try (Connection cn = DBConnection.getConnection()) {

            String sql = "INSERT INTO projects(title, deadline, revenue, status) VALUES (?, ?, ?, 'PENDING')";
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, p.title);
            ps.setInt(2, p.deadline);
            ps.setInt(3, p.revenue);

            ps.executeUpdate();
            System.out.println("Project added successfully.");

        } catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }

    public List<Project> getAllProjects() {

        List<Project> list = new ArrayList<>();

        try (Connection cn = DBConnection.getConnection()) {

            ResultSet rs = cn.createStatement()
                    .executeQuery("SELECT * FROM projects");

            while (rs.next()) {
                Project p = new Project();
                p.id = rs.getInt("id");
                p.title = rs.getString("title");
                p.deadline = rs.getInt("deadline");
                p.revenue = rs.getInt("revenue");
                p.status = rs.getString("status");

                list.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error : "+e);
        }

        return list;
    }


    public void deleteProject(int id) {
        try (Connection cn = DBConnection.getConnection()) {

            PreparedStatement ps = cn.prepareStatement(
                    "DELETE FROM projects WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Project deleted.");

        } catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }


    public void resetAllStatus() {
        try (Connection cn = DBConnection.getConnection()) {
            cn.createStatement().executeUpdate("UPDATE projects SET status='PENDING'");
        } catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }


    public void updateStatus(int id, String status) {
        try (Connection cn = DBConnection.getConnection()) {

            PreparedStatement ps = cn.prepareStatement(
                    "UPDATE projects SET status=? WHERE id=?");

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }
    public void resetAllProjects() {
        try (Connection cn = DBConnection.getConnection()) {

            Statement st = cn.createStatement();
            st.executeUpdate("TRUNCATE TABLE projects RESTART IDENTITY CASCADE");

            System.out.println("All projects deleted and ID reset to 1.");

        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }
}
