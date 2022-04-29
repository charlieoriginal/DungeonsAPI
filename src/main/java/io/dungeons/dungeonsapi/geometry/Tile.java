package io.dungeons.dungeonsapi.geometry;

public class Tile {
    private int x;
    private int y;
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean equals(Tile tile) {
        return x == tile.getX() && y == tile.getY();
    }
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
