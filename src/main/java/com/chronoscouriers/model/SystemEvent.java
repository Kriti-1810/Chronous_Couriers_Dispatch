package com.chronoscouriers.model;

import java.time.LocalDateTime;

public class SystemEvent {
    private String eventId;
    private String description;
    private LocalDateTime timestamp;
    private String relatedEntityId; // e.g., trackingNumber, riderId, assignmentId


    public SystemEvent(String eventId, String description, String relatedEntityId) {
        this.eventId = eventId;
        this.description = description;
        this.relatedEntityId = relatedEntityId;
        this.timestamp = LocalDateTime.now();
    }


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
}