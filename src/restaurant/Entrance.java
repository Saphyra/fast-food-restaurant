package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.meals.Cookable;
import restaurant.service.Cassa;
import restaurant.service.Chef;
import restaurant.service.ClientGenerator;
import restaurant.service.Desk;
import restaurant.service.Table;
import restaurant.service.TableService;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.TableSortingComparator;

//Creating components
public class Entrance {
    private static final String DESK_QUEUE_SIZE = "entrance.deskqueuesize";
    private static final String MEAL_QUEUE_SIZE = "entrance.mealqueuesize";
    private static final String TABLE_QUEUE_SIZE = "entrance.tablequeuesize";
    private static final String CASSA_QUEUE_SIZE = "entrance.cassaqueuesize";
    private static final String MAX_CLIENT_GROUP_SIZE = "clientgroup.maxsize";

    private static final String TABLE_COUNT = "table.count";
    private static final String CHEF_COUNT = "chef.count";
    private static final String WAITER_COUNT = "desk.waitercount";

    public static final long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) {
        PropLoader.init();
        start();
    }

    private static void start() {
        BlockingQueue<ClientGroup> deskQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(DESK_QUEUE_SIZE));
        BlockingQueue<Cookable> mealQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(MEAL_QUEUE_SIZE));
        BlockingQueue<ClientGroup> tableQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(TABLE_QUEUE_SIZE));
        BlockingQueue<ClientGroup> cassaQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(CASSA_QUEUE_SIZE));

        createLogger();

        createClientGenerator(deskQueue, cassaQueue);
        createDesks(deskQueue, mealQueue, tableQueue);
        createChefs(mealQueue);

        List<Table> tables = Collections.synchronizedList(createTables());
        createTableService(tableQueue, tables);

        createCassa(cassaQueue);
    }

    // Starting Logger
    private static void createLogger() {
        Thread loggerThread = new Thread(new Logger());
        loggerThread.setName("Logger");
        loggerThread.start();
        Logger.logToConsole("Logger started");
    }

    // Creating ClientGenerator
    private static void createClientGenerator(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<ClientGroup> cassaQueue) {
        Thread generator = new Thread(new ClientGenerator(deskQueue, cassaQueue));
        generator.setName("ClientGenerator");
        generator.start();
        Logger.logToConsole("The doors of the restaurant are opened.");
    }

    // Creating Desks
    private static void createDesks(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<Cookable> mealQueue, BlockingQueue<ClientGroup> tableQueue) {
        int waiterCount = PropLoader.getIntegerProperty(WAITER_COUNT);

        for (int x = 1; x <= waiterCount; x++) {
            Thread thread = new Thread(new Desk(deskQueue, mealQueue, tableQueue));
            thread.setName("Desk " + x);
            thread.start();
            Logger.logToConsole("Desk " + x + " is waiting for clients.");
        }
    }

    // Creating Chefs
    private static void createChefs(BlockingQueue<Cookable> mealQueue) {
        int chefCount = PropLoader.getIntegerProperty(CHEF_COUNT);

        for (int x = 1; x <= chefCount; x++) {
            Thread thread = new Thread(new Chef(mealQueue));
            thread.setName("Chef " + x);
            thread.start();
            Logger.logToConsole("Chef " + x + " is heating the ovens.");
        }
    }

    // Creating Tables
    private static List<Table> createTables() {
        int tableCount = PropLoader.getIntegerProperty(TABLE_COUNT);
        int maxClientGroupSize = PropLoader.getIntegerProperty(MAX_CLIENT_GROUP_SIZE);

        List<Table> tables = new ArrayList<>(tableCount + 1);
        Table maxSizeTable = new Table(maxClientGroupSize);
        tables.add(maxSizeTable);
        Logger.logToErr(maxSizeTable.toString() + " is created.");

        for (int x = 0; x < tableCount; x++) {
            Table table = Table.randomTableFactory();
            Logger.logToErr(table.toString() + " is created.");
            tables.add(table);
        }

        tables.sort(new TableSortingComparator());
        return tables;
    }

    // Creating TableService
    private static void createTableService(BlockingQueue<ClientGroup> tableQueue, List<Table> tables) {
        Thread tableServiceThread = new Thread(new TableService(tableQueue, tables));
        tableServiceThread.setName("TableService");
        tableServiceThread.start();
        Logger.logToConsole("Tables are cleaned.");
    }

    // Crating Cassa
    private static void createCassa(BlockingQueue<ClientGroup> cassaQueue) {
        Thread cassa = new Thread(new Cassa(cassaQueue));
        cassa.setName("Cassa");
        cassa.start();
        Logger.logToConsole("Cassa is waiting for money.");
    }
}
