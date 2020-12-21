package com.nikita;

import com.nikita.planner.Planner;

public class Main {

    public static void main(String[] args) {
        Planner planner = new Planner();
        planner.printMatrix();
        planner.plan();
        planner.printRelationTaskResource();
    }
}
