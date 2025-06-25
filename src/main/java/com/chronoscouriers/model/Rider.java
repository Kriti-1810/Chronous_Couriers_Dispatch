package com.chronoscouriers.model;

import com.chronoscouriers.enums.RiderStatus;
import java.util.HashSet;
import java.util.Set;

public class Rider {
    private final String riderId;
    private final String name;
    private final Set<String> capabilities; // e.g., 'FRAGILE_HANDLING', 'HEAVY_LIFTING'
    private RiderStatus status;
    private String currentAssignmentId;

    public Rider(String riderId, String name, Set<String> capabilities) {
        this.riderId = riderId;
        this.name = name;
        this.capabilities = capabilities != null ? new HashSet<>(capabilities) : new HashSet<>();
        this.status = RiderStatus.OFFLINE;
    }

    public String getRiderId() { return riderId; }
    public String getName() { return name; }
    public RiderStatus getStatus() { return status; }
    public Set<String> getCapabilities() { return capabilities; }
    public String getCurrentAssignmentId() { return currentAssignmentId; }

    public void setStatus(RiderStatus status) { this.status = status; }
    public void setCurrentAssignmentId(String assignmentId) { this.currentAssignmentId = assignmentId; }

    @Override
    public String toString() {
        return String.format("Rider[ID=%s, Name=%s, Status=%s, Assignment=%s, Capabilities=%s]",
                riderId, name, status, currentAssignmentId != null ? currentAssignmentId : "None", capabilities);
    }
}