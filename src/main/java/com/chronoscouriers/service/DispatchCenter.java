package com.chronoscouriers.service;

import com.chronoscouriers.enums.AssignmentStatus;
import com.chronoscouriers.enums.PackageStatus;
import com.chronoscouriers.enums.PackageType;
import com.chronoscouriers.enums.RiderStatus;
import com.chronoscouriers.model.Assignment;
import com.chronoscouriers.model.Package;
import com.chronoscouriers.model.Rider;
import com.chronoscouriers.util.PackageComparator;

import java.util.*;
import java.util.stream.Collectors;

public class DispatchCenter {

    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Package> packages = new HashMap<>();
    private final Map<String, Assignment> assignments = new HashMap<>();
    private final PriorityQueue<Package> pendingPackages = new PriorityQueue<>(new PackageComparator());
    private final LoggingService logger;

    public DispatchCenter(LoggingService logger) {
        this.logger = logger;
    }

    public Rider addRider(String riderId, String name, Set<String> capabilities) {
        if (riders.containsKey(riderId)) {
            throw new IllegalArgumentException("Rider with ID " + riderId + " already exists.");
        }
        Rider rider = new Rider(riderId, name, capabilities);
        riders.put(riderId, rider);
        logger.logEvent("Rider created.", rider.getRiderId());
        return rider;
    }

    public Package placeOrder(String packageId, PackageType type, long deadline, String address, double weight, Set<String> reqs) {
        if (packages.containsKey(packageId)) {
            throw new IllegalArgumentException("Package with ID " + packageId + " already exists.");
        }
        Package newPackage = new Package(packageId, type, deadline, address, weight, reqs);
        packages.put(packageId, newPackage);
        pendingPackages.add(newPackage);
        logger.logEvent("New order placed.", newPackage.getPackageId());
        return newPackage;
    }

    public void updateRiderStatus(String riderId, RiderStatus newStatus) {
        Rider rider = findRider(riderId);
        if (rider.getStatus() == RiderStatus.ON_DELIVERY && newStatus == RiderStatus.OFFLINE) {
            handleRiderGoingOffline(rider);
        }
        rider.setStatus(newStatus);
        logger.logEvent("Rider status updated to " + newStatus, rider.getRiderId());
    }

    public void dispatchPackages() {
        logger.logEvent("Dispatch cycle started.", "SYSTEM");
        List<Rider> availableRiders = riders.values().stream()
                .filter(r -> r.getStatus() == RiderStatus.AVAILABLE)
                .collect(Collectors.toList());

        for (Rider rider : availableRiders) {
            if (pendingPackages.isEmpty()) break;

            Package packageToAssign = findMatchingPackageForRider(rider);
            if (packageToAssign != null) {
                assignPackage(rider, packageToAssign);
            }
        }
    }
    // At the end of DispatchCenter.java
    public Map<String, Assignment> getAllAssignments() {
        return this.assignments;
    }

    public void completeDelivery(String assignmentId) {
        Assignment assignment = findAssignment(assignmentId);
        if (assignment.getStatus() == AssignmentStatus.COMPLETED) {
            throw new IllegalStateException("Assignment " + assignmentId + " is already completed.");
        }

        // ✅ Mark assignment as completed
        assignment.markCompleted();

        // ✅ Update related entities
        Package pkg = findPackage(assignment.getPackageId());
        pkg.setStatus(PackageStatus.DELIVERED);

        Rider rider = findRider(assignment.getRiderId());
        rider.setStatus(RiderStatus.AVAILABLE);
        rider.setCurrentAssignmentId(null);

        logger.logEvent("Delivery completed.", assignment.getPackageId());

        // Try dispatching new packages
        dispatchPackages();
    }


    private void assignPackage(Rider rider, Package pkg) {
        pendingPackages.remove(pkg);

        String assignmentId = "ASN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Assignment assignment = new Assignment(assignmentId, rider.getRiderId(), pkg.getPackageId());
        assignments.put(assignmentId, assignment);

        rider.setStatus(RiderStatus.ON_DELIVERY);
        rider.setCurrentAssignmentId(assignmentId);
        pkg.setStatus(PackageStatus.IN_TRANSIT);

        logger.logEvent("Assigned to rider " + rider.getRiderId(), pkg.getPackageId());
        System.out.printf(">>> Assignment Created: %s for Package %s -> Rider %s%n", assignmentId, pkg.getPackageId(), rider.getRiderId());
    }

    private void handleRiderGoingOffline(Rider rider) {
        logger.logEvent("Rider went offline during delivery.", rider.getRiderId());
        String assignmentId = rider.getCurrentAssignmentId();
        if (assignmentId != null) {
            Assignment assignment = findAssignment(assignmentId);
            Package pkg = findPackage(assignment.getPackageId());

            // add the package in the queue again
            assignment.markCancelled();
            pkg.setStatus(PackageStatus.PENDING_PICKUP);
            pendingPackages.add(pkg);
            rider.setCurrentAssignmentId(null);

            logger.logEvent("Package re-queued for dispatch.", pkg.getPackageId());
            System.out.println(">>> ALERT: Rider " + rider.getRiderId() + " went offline. Package " + pkg.getPackageId() + " returned to queue.");
        }
    }

    private Package findMatchingPackageForRider(Rider rider) {
        Package highPriorityPackage = pendingPackages.peek();
        if (highPriorityPackage != null && rider.getCapabilities().containsAll(highPriorityPackage.getSpecialRequirements())) {
            return highPriorityPackage;
        }
        return null;
    }

    public Rider findRider(String riderId) {
        if (!riders.containsKey(riderId)) throw new NoSuchElementException("Rider not found: " + riderId);
        return riders.get(riderId);
    }

    public Package findPackage(String packageId) {
        if (!packages.containsKey(packageId)) throw new NoSuchElementException("Package not found: " + packageId);
        return packages.get(packageId);
    }

    public Assignment findAssignment(String assignmentId) {
        if (!assignments.containsKey(assignmentId)) throw new NoSuchElementException("Assignment not found: " + assignmentId);
        return assignments.get(assignmentId);
    }

    public List<Assignment> getCompletedAssignmentsForRider(String riderId, long withinLastMillis) {
        long now = System.currentTimeMillis();
        List<Assignment> results = new ArrayList<>();
        for (Assignment assignment : assignments.values()) {
            if (assignment.getRiderId().equals(riderId) &&
                    assignment.getStatus() == AssignmentStatus.COMPLETED &&
                    (now - assignment.getCompletionTime() <= withinLastMillis)) {
                results.add(assignment);
            }
        }
        return results;
    }

    public List<Package> getMissedExpressDeliveries() {
        List<Package> missed = new ArrayList<>();
        for (Package pkg : packages.values()) {
            if (pkg.getType() == PackageType.EXPRESS && pkg.getStatus() == PackageStatus.DELIVERED) {
                Assignment assignment = findAssignmentForPackage(pkg.getPackageId());
                if (assignment != null && assignment.getCompletionTime() > pkg.getDeliveryDeadline()) {
                    missed.add(pkg);
                }
            }
        }
        return missed;
    }

    // Helper method
    private Assignment findAssignmentForPackage(String packageId) {
        for (Assignment assignment : assignments.values()) {
            if (assignment.getPackageId().equals(packageId)) {
                return assignment;
            }
        }
        return null;
    }

}