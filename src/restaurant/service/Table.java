package restaurant.service;

import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Table {
    private static final String MAX_CLIENT_GROUP_SIZE = "clientgroup.maxsize";
    private static final String MIN_CLIENT_GROUP_SIZE = "clientgroup.minsize";
    private static int tablePlaces = 0;
    private static int tableCounter = 0;
    private volatile boolean free = true;
    private final int tableSize;
    private final String name;

    public Table(int size) {
        tableSize = size;
        name = "Table" + ++tableCounter;
    }

    public static Table randomTableFactory() {
        long minClientGroupSize = PropLoader.getLongProperty(MIN_CLIENT_GROUP_SIZE);
        long maxClientGroupSize = PropLoader.getLongProperty(MAX_CLIENT_GROUP_SIZE);

        int tableSize = Random.randInt(minClientGroupSize, maxClientGroupSize);
        tablePlaces += tableSize;

        Table table = tableFactory(tableSize);
        return table;
    }

    public static Table tableFactory(int size) {
        Table table = new Table(size);
        Logger.logToErr(table.toString() + " is created.");
        return table;
    }

    public int getTableSize() {
        return tableSize;
    }

    public synchronized boolean isFree() {
        return free;
    }

    public synchronized void setFree(boolean value) {
        free = value;
    }

    public static int getTablePlaces() {
        return tablePlaces;
    }

    @Override
    public String toString() {
        return name + " (Size: " + tableSize + ")";
    }
}
