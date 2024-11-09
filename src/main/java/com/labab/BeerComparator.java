package com.labab;

import generated.Beer;

import java.util.Comparator;

public class BeerComparator implements Comparator<Beer.BeerItem> {
    @Override
    public int compare(Beer.BeerItem f1, Beer.BeerItem f2) {
        return f1.getName().compareTo(f2.getName());
    }
}
