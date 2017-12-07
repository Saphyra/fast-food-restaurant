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
        int size = 0;
        while (true) {

            searchTableForWaitingGroups();

            size = tableQueue.size();
            if (size != lastSize) {
                Logger.logToErr(tableQueue.size() + " groups are waiting for empty table." + " " + size + " - " + lastSize);
                Logger.logToErr("Unsuitable empty tables: " + getEmptyTablesCount());
                lastSize = size;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        boolean found = false;
        for (Table table : tables) {
            if (isTableSuitable(clientNum, table)) {
                reserveTable(group, table);

                Logger.logToErr(group + " has found a table! - Table: " + table);
                found = true;
                break;
            }
        }
        if (!found) {
            Logger.logToConsole(group + " is waiting for table.");
        }
    }

    private void reserveTable(ClientGroup group, Table table) {
        group.setTable(table);
        table.setFree(false);
        startGroup(group);
        tableQueue.remove(group);
    }

    private void startGroup(ClientGroup group) {
        Thread th = new Thread(group);
        th.setName(group.toString());
        th.start();
    }

    private boolean isTableSuitable(int clientNum, Table table) {
        return table.isFree() == true && table.getTableSize() >= clientNum;
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
