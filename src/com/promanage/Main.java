package com.promanage;

import com.promanage.dao.ProjectDAO;
import com.promanage.dao.ScheduleDAO;
import com.promanage.model.Project;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProjectDAO projectDAO = new ProjectDAO();
    static ScheduleDAO scheduleDAO = new ScheduleDAO();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n1. Add Project");
            System.out.println("2. Delete Project");
            System.out.println("3. Generate Schedule");
            System.out.println("4. View Schedule");
            System.out.println("5. Exit");
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
                    scheduleDAO.generateSchedule();
                    break;

                case 4:
                    scheduleDAO.viewSchedule();
                    break;

                case 5:
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
}
