import com.chronoscouriers.enums.PackageStatus;
import com.chronoscouriers.enums.PackageType;
import com.chronoscouriers.enums.RiderStatus;
import com.chronoscouriers.model.Assignment;
import com.chronoscouriers.model.Package;
import com.chronoscouriers.model.Rider;
import com.chronoscouriers.service.DispatchCenter;
import com.chronoscouriers.service.LoggingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DispatchAppTest {

    private DispatchCenter dispatchCenter;

    @BeforeEach
    void setup() {
        dispatchCenter = new DispatchCenter(new LoggingService());
    }

    @Test
    void testAddRiderAndCheckStatus() {
        Rider rider = dispatchCenter.addRider("R1", "Rahul", Set.of("FRAGILE"));
        assertEquals("R1", rider.getRiderId());
        assertEquals(RiderStatus.OFFLINE, rider.getStatus());
    }

    @Test
    void testPlaceOrderAndCheckPackage() {
        Package pkg = dispatchCenter.placeOrder("P1", PackageType.EXPRESS,
                System.currentTimeMillis() + 100000,
                "MG_Road_Bangalore",
                2.5,
                Set.of("FRAGILE"));
        assertEquals("P1", pkg.getPackageId());
        assertEquals(PackageStatus.PENDING_PICKUP, pkg.getStatus());
    }

    @Test
    void testDispatchAndAssign() {
        dispatchCenter.addRider("R1", "Rahul", Set.of("FRAGILE"));
        dispatchCenter.updateRiderStatus("R1", RiderStatus.AVAILABLE);
        dispatchCenter.placeOrder("P1", PackageType.EXPRESS,
                System.currentTimeMillis() + 100000,
                "MG_Road_Bangalore",
                2.5,
                Set.of("FRAGILE"));
        dispatchCenter.dispatchPackages();

        Optional<Assignment> assignment = dispatchCenter.getAllAssignments().values().stream()
                .filter(a -> a.getRiderId().equals("R1"))
                .findFirst();
        assertTrue(assignment.isPresent(), "Expected an assignment for rider R1");
    }

    @Test
    void testCompleteDelivery() {
        dispatchCenter.addRider("R1", "Rahul", Set.of("FRAGILE"));
        dispatchCenter.updateRiderStatus("R1", RiderStatus.AVAILABLE);
        dispatchCenter.placeOrder("P1", PackageType.EXPRESS,
                System.currentTimeMillis() + 100000,
                "MG_Road_Bangalore",
                2.5,
                Set.of("FRAGILE"));
        dispatchCenter.dispatchPackages();

        Assignment assignment = dispatchCenter.getAllAssignments().values().iterator().next();
        dispatchCenter.completeDelivery(assignment.getAssignmentId());

        assertEquals(PackageStatus.DELIVERED,
                dispatchCenter.findPackage("P1").getStatus());
    }

    @Test
    void testGetCompletedAssignmentsForRider() {
        dispatchCenter.addRider("R1", "Rahul", Set.of("FRAGILE"));
        dispatchCenter.updateRiderStatus("R1", RiderStatus.AVAILABLE);
        dispatchCenter.placeOrder("P1", PackageType.EXPRESS,
                System.currentTimeMillis() + 100000,
                "MG_Road_Bangalore",
                2.5,
                Set.of("FRAGILE"));
        dispatchCenter.dispatchPackages();

        Assignment assignment = dispatchCenter.getAllAssignments().values().iterator().next();
        dispatchCenter.completeDelivery(assignment.getAssignmentId());

        List<Assignment> results = dispatchCenter.getCompletedAssignmentsForRider("R1", 86400000);
        assertEquals(1, results.size(), "Rider R1 should have 1 completed assignment in the last 24h");
    }

    @Test
    void testGetMissedExpressDeliveries() {
        dispatchCenter.addRider("R1", "Rahul", Set.of("FRAGILE"));
        dispatchCenter.updateRiderStatus("R1", RiderStatus.AVAILABLE);
        dispatchCenter.placeOrder("P1", PackageType.EXPRESS,
                System.currentTimeMillis() - 1000,
                "MG_Road_Bangalore",
                2.5,
                Set.of("FRAGILE"));
        dispatchCenter.dispatchPackages();

        Assignment assignment = dispatchCenter.getAllAssignments().values().iterator().next();
        dispatchCenter.completeDelivery(assignment.getAssignmentId());

        List<Package> missed = dispatchCenter.getMissedExpressDeliveries();
        assertEquals(1, missed.size(), "Should have 1 missed express delivery");
    }
}


