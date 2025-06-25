package com.chronoscouriers.model;

import com.chronoscouriers.enums.PackageStatus;
import com.chronoscouriers.enums.PackageType;
import java.util.HashSet;
import java.util.Set;

public class Package {

    private final String packageId;
    private final String destinationAddress;
    private final PackageType type;
    private final double weightInKg;
    private final long orderTime;
    private final long deliveryDeadline;
    private final Set<String> specialRequirements;

    private PackageStatus status;

    public Package(String packageId, PackageType type, long deliveryDeadline, String destinationAddress, double weightInKg, Set<String> requirements) {
        this.packageId = packageId;
        this.type = type;
        this.deliveryDeadline = deliveryDeadline;
        this.destinationAddress = destinationAddress;
        this.weightInKg = weightInKg;
        this.specialRequirements = requirements != null ? new HashSet<>(requirements) : new HashSet<>();
        this.status = PackageStatus.PENDING_PICKUP;
        this.orderTime = System.currentTimeMillis(); // for Setting the order time on creation
    }

    public String getPackageId() { return packageId; }
    public PackageType getType() { return type; }
    public long getDeliveryDeadline() { return deliveryDeadline; }
    public long getOrderTime() { return orderTime; }
    public PackageStatus getStatus() { return status; }
    public Set<String> getSpecialRequirements() { return specialRequirements; }
    public String getDestinationAddress() { return destinationAddress; }
    public double getWeightInKg() { return weightInKg; }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Package[ID=%s, Type=%s, Status=%s, Deadline=%d, Requirements=%s]",
                packageId, type, status, deliveryDeadline, specialRequirements);
    }
}