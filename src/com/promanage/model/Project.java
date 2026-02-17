package com.promanage.model;

import java.time.LocalDateTime;


public class Project {

    public int id;
    public String title;
    public int deadline;   // working days
    public int revenue;
    public String status;  // PENDING, SCHEDULED, REJECTED, COMPLETED
    public LocalDateTime createdAt;


    public Project() {
    }

    // Constructor without ID (for new project)
    public Project(String title, int deadline, int revenue) {
        this.title = title;
        this.deadline = deadline;
        this.revenue = revenue;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    // Full constructor (for data fetched from DB)
    public Project(int id, String title, int deadline, int revenue,
                   String status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.revenue = revenue;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Project [ID=" + id +
                ", Title=" + title +
                ", Deadline=" + deadline +
                ", Revenue=" + revenue +
                ", Status=" + status +
                ", CreatedAt=" + createdAt + "]";
    }
}
