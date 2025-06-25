package com.chronoscouriers.service;

import com.chronoscouriers.model.SystemEvent;
import java.util.UUID;

public class LoggingService {

    public void logEvent(String description, String relatedEntityId) {
        String eventId = UUID.randomUUID().toString();
        SystemEvent event = new SystemEvent(eventId, description, relatedEntityId);
        System.out.printf("[LOG | %s] %s (Entity ID: %s)%n",
                event.getTimestamp(),
                event.getDescription(),
                event.getRelatedEntityId()
        );
    }
}