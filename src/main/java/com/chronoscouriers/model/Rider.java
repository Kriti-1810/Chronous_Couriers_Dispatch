package com.chronoscouriers.model;

import com.chronoscouriers.enums.RiderStatus;

import javax.xml.stream.Location;
import java.util.List;

public class Rider {
    private String riderId;
    private RiderStatus status;
    private double reliabilityRating;
    private boolean canHandleFragile;
    private Location currentLocation;
    private List<Package> currentAssignments;
    // Other rider attributes

    // Constructor, getters, setters
}
