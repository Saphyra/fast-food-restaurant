package restaurant.service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.util.Logger;

public class TableService implements Runnable {
    private BlockingQueue<ClientGroup> tableQueue;
    private List<Table> tables;

    public TableService(BlockingQueue<ClientGroup> tableQueue, List<Table> tables) {
        this.tableQueue = tableQueue;
        this.tables = tables;
    }

    @Override
    public void run() {
        int lastSize = 0;
        while (true) {
            searchTableForWaitingGroups();
            lastSize = logWaitingGroupNum(lastSize);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int logWaitingGroupNum(int lastSize) {
        int size = tableQueue.size();
        if (size != lastSize) {
            Logger.logToErr(tableQueue.size() + " groups are waiting for empty table." + " " + size + " - " + lastSize);
            Logger.logToErr("Unsuitable empty tables: " + getEmptyTablesCount());
            lastSize = size;
        }
        return lastSize;
    }

    private void searchTableForWaitingGroups() {
        Iterator<ClientGroup> iter = tableQueue.iterator();

        while (iter.hasNext()) {
            ClientGroup group = iter.next();
            searchEmptyTable(group);
        }
    }

    private void searchEmptyTable(ClientGroup group) {
        int clientNum = group.getClientNum();
        for (Table table : tables) {
            if (isTableSuitable(clientNum, table)) {
                reserveTable(group, table);
                break;
            }
        }
    }

    private void reserveTable(ClientGroup group, Table table) {
        table.setFree(false);
        group.setTable(table);
        Logger.logToErr(group + " has found a table! - Table: " + table);
        startGroup(group);
        tableQueue.remove(group);
    }

    private void startGroup(ClientGroup group) {
        Thread groupThread = new Thread(group, group.toString());
        groupThread.start();
    }

    private boolean isTableSuitable(int clientNum, Table table) {
        return table.isFree() && table.getTableSize() >= clientNum;
    }

    private int getEmptyTablesCount() {
        int count = 0;
        for (Table table : tables) {
            if (table.isFree()) {
                count++;
            }
        }
        return count;
    }
}
