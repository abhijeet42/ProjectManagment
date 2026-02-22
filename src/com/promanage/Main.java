package com.promanage;

import com.promanage.dao.ProjectDAO;
import com.promanage.dao.ScheduleDAO;
import com.promanage.model.Project;

import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProjectDAO projectDAO = new ProjectDAO();
    static ScheduleDAO scheduleDAO = new ScheduleDAO();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== Project Management System =====");
            System.out.println("1. Add Project");
            System.out.println("2. Delete Project");
            System.out.println("3. View All Projects");
            System.out.println("4. Update Project Status");
            System.out.println("5. Reset All Status to PENDING");
            System.out.println("6. Generate Schedule");
            System.out.println("7. View Schedule");
            System.out.println("8. Reset all Projects");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addProject();
                    break;

                case 2:
                    deleteProject();
                    break;

                case 3:
                    viewAllProjects();
                    break;

                case 4:
                    updateProjectStatus();
                    break;

                case 5:
                    projectDAO.resetAllStatus();
                    System.out.println("All project statuses reset to PENDING.");
                    break;

                case 6:
                    scheduleDAO.generateSchedule();
                    break;

                case 7:
                    scheduleDAO.viewSchedule();
                    break;

                case 8:
                    projectDAO.resetAllProjects();
                    break;

                case 9:
                    System.exit(0);

            }
        }
    }

    static void addProject() {

        sc.nextLine();

        Project p = new Project();

        System.out.print("Enter title: ");
        p.title = sc.nextLine();

        System.out.print("Enter deadline (days): ");
        p.deadline = sc.nextInt();

        System.out.print("Enter revenue: ");
        p.revenue = sc.nextInt();

        projectDAO.addProject(p);
    }

    static void deleteProject() {

        System.out.print("Enter project ID to delete: ");
        int id = sc.nextInt();

        projectDAO.deleteProject(id);
    }

    static void viewAllProjects() {

        List<Project> list = projectDAO.getAllProjects();

        if (list.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        System.out.println("\n----- Project List -----");

        for (Project p : list) {
            System.out.println(
                    "ID: " + p.id +
                            " | Title: " + p.title +
                            " | Deadline: " + p.deadline +
                            " | Revenue: " + p.revenue +
                            " | Status: " + p.status
            );
        }
    }

    static void updateProjectStatus() {

        System.out.print("Enter project ID: ");
        int id = sc.nextInt();

        sc.nextLine(); // consume leftover newline

        System.out.print("Enter new status (PENDING / SCHEDULED / COMPLETED): ");
        String status = sc.nextLine();

        projectDAO.updateStatus(id, status);

        System.out.println("Status updated successfully.");
    }
}