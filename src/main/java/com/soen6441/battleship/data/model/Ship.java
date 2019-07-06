package com.soen6441.battleship.data.model;

import com.soen6441.battleship.enums.ShipDirection;

public class Ship {
    private final String uniqueID;
    private final String name;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private final int length;
    private int hits = 0;
    private final ShipDirection direction;

    public static class Builder {
        private String uniqueId;
        private String name;
        private int startX;
        private int startY;
        private int endX;
        private int endY;
        private int length;
        private ShipDirection direction;

        public Builder setStartCoordinates(int x, int y) {
            this.startX = x;
            this.startY = y;
            return this;
        }

        public Builder setEndCoordinates(int x, int y) {
            this.endX = x;
            this.endY = y;
            return this;
        }

        public Builder setDirection(ShipDirection direction) {
            this.direction = direction;
            return this;
        }

        public Builder setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLength(int length) {
            this.length = length;
            return this;
        }

        public Ship build() {
            return new Ship(this);
        }
    }

    private Ship(Builder builder) {
        this.uniqueID = builder.uniqueId;
        this.name = builder.name;
        this.startX = builder.startX;
        this.startY = builder.startY;
        this.endX = builder.endX;
        this.endY = builder.endY;
        this.direction = builder.direction;
        this.length = builder.length;
    }

    public String getName() {
        return name;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public int getLength() {
        return length;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public boolean isSunk() {
        return this.length == this.hits;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "uniqueID='" + uniqueID + '\'' +
                ", name='" + name + '\'' +
                ", startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", length=" + length +
                ", hits=" + hits +
                ", direction=" + direction +
                '}';
    }
}
