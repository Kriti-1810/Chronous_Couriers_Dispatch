package com.chronoscouriers.model;

import com.chronoscouriers.enums.AssignmentStatus;

public class Assignment {
    private final String assignmentId;
    private final String riderId;
    private final String packageId;

    private long pickupTime;
    private long completionTime;

    private AssignmentStatus status;

    public Assignment(String assignmentId, String riderId, String packageId) {
        this.assignmentId = assignmentId;
        this.riderId = riderId;
        this.packageId = packageId;
        this.status = AssignmentStatus.ASSIGNED;
        this.pickupTime = System.currentTimeMillis();
    }

    public void markCompleted() {
        this.status = AssignmentStatus.COMPLETED;
        this.completionTime = System.currentTimeMillis();
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getPackageId() {
        return packageId;
    }

    public long getPickupTime() {
        return pickupTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public AssignmentStatus getStatus() {
        return status;
    }
    public void markCancelled() {
        this.status = AssignmentStatus.CANCELLED;
    }

}
