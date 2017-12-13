package restaurant.service;

import java.util.concurrent.atomic.AtomicBoolean;

import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Table {
    private static final String MAX_CLIENT_GROUP_SIZE = "clientgroup.maxsize";
    private static final String MIN_CLIENT_GROUP_SIZE = "clientgroup.minsize";
    private static int tableCounter = 0;
    private volatile AtomicBoolean free = new AtomicBoolean(true);
    private int tableSize;
    private final String name;

    public Table(int size) {
        tableCounter++;
        tableSize = size;
        name = "Table" + tableCounter;
        Logger.logToErr(toString() + " is created.");
    }

    public static Table randomTableFactory() {
        long minClientGroupSize = PropLoader.getLongProperty(MIN_CLIENT_GROUP_SIZE);
        long maxClientGroupSize = PropLoader.getLongProperty(MAX_CLIENT_GROUP_SIZE);

        int tableSize = Random.randInt(minClientGroupSize, maxClientGroupSize);
        return new Table(tableSize);
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public synchronized boolean isFree() {
        return free.get();
    }

    public synchronized void setFree(boolean free) {
        this.free.set(free);
    }

    @Override
    public String toString() {
        return name + " (Size: " + tableSize + ")";
    }
}
