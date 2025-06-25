package com.chronoscouriers.util;

import com.chronoscouriers.model.Package;
import java.util.Comparator;

/**
 * Comparator to prioritize express packages.
 * Strategy: Oldest (first created) packages first.
 */
public class ExpressPackageComparator implements Comparator<Package> {
    @Override
    public int compare(Package p1, Package p2) {
        // Sorts by the earliest creation time
        return Long.compare(p1.getOrderTime(), p2.getOrderTime());
    }
}