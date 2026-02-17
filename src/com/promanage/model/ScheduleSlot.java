package com.promanage.model;

public class ScheduleSlot {

    public int dayNumber;
    public Project project;  // null = free

    // Constructor
    public ScheduleSlot(int dayNumber, Project project) {
        this.dayNumber = dayNumber;
        this.project = project;
    }

    @Override
    public String toString() {
        if (project == null) {
            return "Day " + dayNumber + " -> Free";
        } else {
            return "Day " + dayNumber + " -> " + project.title +
                    " (Revenue: " + project.revenue + ")";
        }
    }
}
