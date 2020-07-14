package com.gmail.fuskerr63.java.entity;

import java.util.List;

public class DirectionStatus {
    private final List<Position> bounds;
    private final List<Position> points;


    public DirectionStatus(List<Position> bounds, List<Position> points) {
        this.bounds = bounds;
        this.points = points;
    }

    public List<Position> getBounds() {
        return bounds;
    }

    public List<Position> getPoints() {
        return points;
    }
}
