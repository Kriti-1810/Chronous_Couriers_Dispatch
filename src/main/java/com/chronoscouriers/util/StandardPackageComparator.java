package com.chronoscouriers.util;

import com.chronoscouriers.model.Package;
import java.util.Comparator;

/**
 * Comparator to prioritize standard packages.
 * Strategy: Heavier packages first.
 */
public class StandardPackageComparator implements Comparator<Package> {
    @Override
    public int compare(Package p1, Package p2) {
        // Sorts in descending order of weight
        return Double.compare(p2.getWeightInKg(), p1.getWeightInKg());
    }
}