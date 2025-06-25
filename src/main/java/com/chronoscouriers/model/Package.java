package com.chronoscouriers.model;

import com.chronoscouriers.enums.PackageStatus;
import com.chronoscouriers.enums.PackageType;

public class Package {
    private String packageId;
    private PackageType type; // EXPRESS or STANDARD
    private long orderTime;
    private long deadline;
    private PackageStatus status;
    private boolean isFragile;
    private String destination;
    // Other package attributes

    // Constructor, getters, setters
}
