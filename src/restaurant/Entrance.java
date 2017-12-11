package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.meals.MainCourse;
import restaurant.service.Cassa;
import restaurant.service.Chef;
import restaurant.service.ClientGenerator;
import restaurant.service.Desk;
import restaurant.service.Table;
import restaurant.service.TableService;
import restaurant.util.Constants;
import restaurant.util.Logger;
import restaurant.util.TableSortingComparator;

//Creating components
public class Entrance {
    public static final long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) {
        BlockingQueue<ClientGroup> deskQueue = new ArrayBlockingQueue<>(5);
        BlockingQueue<MainCourse> mealQueue = new ArrayBlockingQueue<>(20);
        BlockingQueue<ClientGroup> tableQueue = new ArrayBlockingQueue<>(5);
        BlockingQueue<ClientGroup> cassaQueue = new ArrayBlockingQueue<>(5);

        startLogger();

        openDoor(deskQueue, cassaQueue);
        createDesk(deskQueue, mealQueue, tableQueue);
        createChefs(mealQueue);

        List<Table> tables = Collections.synchronizedList(createTables());
        createTableService(tableQueue, tables);

        createCassa(cassaQueue);
    }

    // Starting Logger
    private static void startLogger() {
        Thread loggerThread = new Thread(new Logger());
        loggerThread.setName("Logger");
        loggerThread.start();
        Logger.logToConsole("Logger started");
    }

    // Creating ClientGenerator
    private static void openDoor(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<ClientGroup> cassaQueue) {
        Thread generator = new Thread(new ClientGenerator(deskQueue, cassaQueue));
        generator.setName("ClientGenerator");
        generator.start();
        Logger.logToConsole("Generator complete.");
    }

    // Creating Desks
    private static void createDesk(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<MainCourse> mealQueue, BlockingQueue<ClientGroup> tableQueue) {
        for (int x = 1; x <= Constants.WAITER_COUNT; x++) {
            Logger.logToConsole("Desk " + x + " created.");
            Thread thread = new Thread(new Desk(deskQueue, mealQueue, tableQueue));
            thread.setName("Desk " + x);
            thread.start();
        }
    }

    // Creating Chefs
    private static void createChefs(BlockingQueue<MainCourse> mealQueue) {
        for (int x = 1; x <= Constants.CHEF_COUNT; x++) {
            Logger.logToConsole("Chef " + x + " created.");
            Thread thread = new Thread(new Chef(mealQueue));
            thread.setName("Chef " + x);
            thread.start();
        }
    }

    // Creating Tables
    private static List<Table> createTables() {
        List<Table> tables = new ArrayList<>(Constants.TABLE_COUNT + 1);
        tables.add(new Table(Constants.MAX_CLIENT_GROUP_SIZE));
        for (int x = 0; x < Constants.TABLE_COUNT; x++) {
            Table table = Table.randomTableFactory();
            tables.add(table);
        }
        tables.sort(new TableSortingComparator());
        return tables;
    }

    // Creating TableService
    private static void createTableService(BlockingQueue<ClientGroup> tableQueue, List<Table> tables) {
        TableService tableService = new TableService(tableQueue, tables);
        Thread tableServiceThread = new Thread(tableService);
        tableServiceThread.setName("TableService");
        tableServiceThread.start();
    }

    // Crating Cassa
    private static void createCassa(BlockingQueue<ClientGroup> cassaQueue) {
        Thread cassa = new Thread(new Cassa(cassaQueue));
        cassa.setName("Cassa");
        cassa.start();
    }
}
