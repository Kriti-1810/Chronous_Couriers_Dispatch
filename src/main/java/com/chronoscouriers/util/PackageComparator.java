package com.chronoscouriers.util;

import com.chronoscouriers.enums.PackageType;
import com.chronoscouriers.model.Package;
import java.util.Comparator;

public class PackageComparator implements Comparator<Package> {

    @Override
    public int compare(Package p1, Package p2) {
        // 1. Compare by Type
        if (p1.getType() != p2.getType()) {
            return p1.getType() == PackageType.EXPRESS ? -1 : 1;
        }
        // 2. Compare by Deadline
        int deadlineCompare = Long.compare(p1.getDeliveryDeadline(), p2.getDeliveryDeadline());
        if (deadlineCompare != 0) {
            return deadlineCompare;
        }
        // 3. Compare by Order Time
        return Long.compare(p1.getOrderTime(), p2.getOrderTime());
    }
}