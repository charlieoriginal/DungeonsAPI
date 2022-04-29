package io.dungeons.dungeonsapi.geometry;

import java.util.AbstractMap;
import java.util.Map;

public class Grid {
    private int width;
    private int height;
    private Map<AbstractMap.SimpleEntry<Integer, Integer>, Tile> tiles;
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Select a random x and y value between 1 and 2 billion.
    public Tile selectUnused() {
        int x = (int) (Math.random() * 2000000000);
        int y = (int) (Math.random() * 2000000000);
        //If the x and y values are not in the map, add them.
        if (!tiles.containsKey(new AbstractMap.SimpleEntry<>(x, y))) {
            tiles.put(new AbstractMap.SimpleEntry<>(x, y), new Tile(x, y));
        }
        //Return the tile.
        return tiles.get(new AbstractMap.SimpleEntry<>(x, y));
    }
}
