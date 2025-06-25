package com.chronoscouriers.model;

import com.chronoscouriers.enums.AssignmentStatus;

public class Assignment {
    private final String assignmentId;
    private final String riderId;
    private final String packageId;
    private final long creationTime;
    private AssignmentStatus status;
    private long completionTime;

    public Assignment(String assignmentId, String riderId, String packageId) {
        this.assignmentId = assignmentId;
        this.riderId = riderId;
        this.packageId = packageId;
        this.status = AssignmentStatus.IN_PROGRESS;
        this.creationTime = System.currentTimeMillis();
    }

    public String getAssignmentId() { return assignmentId; }
    public String getRiderId() { return riderId; }
    public String getPackageId() { return packageId; }
    public AssignmentStatus getStatus() { return status; }
    public long getCreationTime() { return creationTime; }
    public long getCompletionTime() { return completionTime; }

    public void setStatus(AssignmentStatus status) { this.status = status; }
    public void setCompletionTime(long completionTime) { this.completionTime = completionTime; }

    @Override
    public String toString() {
        return String.format("Assignment[ID=%s, Rider=%s, Package=%s, Status=%s]",
                assignmentId, riderId, packageId, status);
    }
}