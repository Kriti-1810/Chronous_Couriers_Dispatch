package com.chronoscouriers;

import com.chronoscouriers.enums.PackageType;
import com.chronoscouriers.enums.RiderStatus;
import com.chronoscouriers.model.Assignment;
import com.chronoscouriers.model.Package;
import com.chronoscouriers.service.DispatchCenter;
import com.chronoscouriers.service.LoggingService;

import java.util.*;

public class DispatchApp {

    public static void main(String[] args) {
        LoggingService loggingService = new LoggingService();
        DispatchCenter dispatchCenter = new DispatchCenter(loggingService);

        System.out.println("Chronos Couriers Dispatch System");
        System.out.println("Enter commands or 'HELP' for a list of commands, 'EXIT' to quit.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine();
                if (line == null || line.equalsIgnoreCase("EXIT")) {
                    System.out.println("Shutting down.");
                    break;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    processCommand(line, dispatchCenter);
                } catch (Exception e) {
                    System.err.println("ERROR: " + e.getMessage());
                }
            }
        }
    }

    private static void processCommand(String line, DispatchCenter center) {
        String[] parts = line.split("\\s+");
        String command = parts[0].toUpperCase();

        switch (command) {
            case "ADD_RIDER":
                // ADD_RIDER <id> <name> [capability1,capability2,...]
                Set<String> caps = parts.length > 3 ? new HashSet<>(Arrays.asList(parts[3].split(","))) : new HashSet<>();
                center.addRider(parts[1], parts[2], caps);
                System.out.println("Rider " + parts[1] + " added.");
                break;
            case "PLACE_ORDER":
                // PLACE_ORDER <id> <type> <deadline_timestamp> <address> <weight> [req1,req2,...]
                Set<String> reqs = parts.length > 6 ? new HashSet<>(Arrays.asList(parts[6].split(","))) : new HashSet<>();
                center.placeOrder(parts[1], PackageType.valueOf(parts[2]), Long.parseLong(parts[3]), parts[4], Double.parseDouble(parts[5]), reqs);
                System.out.println("Package " + parts[1] + " order placed.");
                break;
            case "UPDATE_RIDER_STATUS":
                // UPDATE_RIDER_STATUS <riderId> <status>
                center.updateRiderStatus(parts[1], RiderStatus.valueOf(parts[2]));
                System.out.println("Rider " + parts[1] + " status updated to " + parts[2]);
                break;
            case "DISPATCH":
                center.dispatchPackages();
                break;
            case "COMPLETE_DELIVERY":
                // COMPLETE_DELIVERY <assignmentId>
                center.completeDelivery(parts[1]);
                System.out.println("Assignment " + parts[1] + " marked as completed.");
                break;
            case "GET_RIDER_STATUS":
                System.out.println(center.findRider(parts[1]));
                break;
            case "GET_PACKAGE_STATUS":
                System.out.println(center.findPackage(parts[1]));
                break;
            case "HELP":
                printHelp();
                break;
            case "GET_RIDER_DELIVERED":
                long last24h = 24 * 60 * 60 * 1000;
                List<Assignment> delivered = center.getCompletedAssignmentsForRider(parts[1], last24h);
                if (delivered.isEmpty()) {
                    System.out.println("No deliveries found for rider " + parts[1] + " in the last 24h.");
                } else {
                    System.out.println("Deliveries for rider " + parts[1] + " in the last 24h:");
                    for (Assignment a : delivered) {
                        System.out.println(" -> Package " + a.getPackageId() + ", Completed at: " + new java.util.Date(a.getCompletionTime()));
                    }
                }
                break;

            case "GET_MISSED_EXPRESS":
                List<Package> missed = center.getMissedExpressDeliveries();
                if (missed.isEmpty()) {
                    System.out.println("No missed express deliveries.");
                } else {
                    System.out.println("Missed express deliveries:");
                    for (Package p : missed) {
                        System.out.println(" -> " + p.getPackageId() + " (Deadline: " + new java.util.Date(p.getDeliveryDeadline()) + ")");
                    }
                }
                break;

            default:
                System.out.println("Unknown command. Type 'HELP' for options.");
        }
    }

    private static void printHelp() {
        System.out.println("\n--- Available Commands ---");
        System.out.println("ADD_RIDER <id> <name> [capabilities]               - e.g., ADD_RIDER R01 Arun FRAGILE,HEAVY");
        System.out.println("PLACE_ORDER <id> <type> <deadline> <addr> <kg> [reqs] - e.g., PLACE_ORDER P01 EXPRESS 1735689600000 Koramangala 2.5 FRAGILE");
        System.out.println("UPDATE_RIDER_STATUS <riderId> <status>             - e.g., UPDATE_RIDER_STATUS R01 AVAILABLE");
        System.out.println("DISPATCH                                           - Attempts to assign pending packages to available riders.");
        System.out.println("COMPLETE_DELIVERY <assignmentId>                   - Marks an active assignment as completed.");
        System.out.println("GET_RIDER_STATUS <riderId>                         - Fetches the status of a specific rider.");
        System.out.println("GET_PACKAGE_STATUS <packageId>                     - Fetches the status of a specific package.");
        System.out.println("EXIT                                               - Shuts down the application.");
        System.out.println("\n[Notes]:");
        System.out.println("- <type> can be EXPRESS or STANDARD.");
        System.out.println("- <status> can be AVAILABLE, OFFLINE, ON_BREAK.");
        System.out.println("- <deadline> is a Unix timestamp in milliseconds.");
        System.out.println("- Capabilities/requirements are optional and comma-separated without spaces.");
        System.out.println("--------------------------\n");
    }
}