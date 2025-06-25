package com.chronoscouriers.util;

import com.chronoscouriers.model.Package;
import java.util.Comparator;

public class PackageComparator implements Comparator<Package> {

    @Override
    public int compare(Package p1, Package p2) {
        // 1. Prioritize by Package Type (EXPRESS comes first)
        if (p1.getType() != p2.getType()) {
            return p1.getType().compareTo(p2.getType());
        }

        // 2. Prioritize by Delivery Deadline (sooner deadline first)
        if (p1.getDeliveryDeadline() != p2.getDeliveryDeadline()) {
            return Long.compare(p1.getDeliveryDeadline(), p2.getDeliveryDeadline());
        }

        // 3. Prioritize by Order Time (earlier order time first)
        return Long.compare(p1.getOrderTime(), p2.getOrderTime());
    }
}