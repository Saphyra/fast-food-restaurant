package restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
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
import restaurant.util.Logger;
import restaurant.util.TableSortingComparator;

//Creating components
public class Entrance {
    public static final long START_TIME = System.currentTimeMillis();
    public static final Properties CONFIG = new Properties();
    public static final Properties EXTRAS = new Properties();
    public static final Properties MAINCOURSES = new Properties();

    public static void main(String[] args) {
        loadProperties();

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

    private static void loadProperties() {
        try {
            CONFIG.load(Entrance.class.getResourceAsStream("../config.properties"));
            EXTRAS.load(Entrance.class.getResourceAsStream("../extras.properties"));
            MAINCOURSES.load(Entrance.class.getResourceAsStream("../maincourses.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int waiterCount = Integer.valueOf((String) CONFIG.get("desk.waitercount"));

        for (int x = 1; x <= waiterCount; x++) {
            Logger.logToConsole("Desk " + x + " created.");
            Thread thread = new Thread(new Desk(deskQueue, mealQueue, tableQueue));
            thread.setName("Desk " + x);
            thread.start();
        }
    }

    // Creating Chefs
    private static void createChefs(BlockingQueue<MainCourse> mealQueue) {
        int chefCount = Integer.valueOf((String) CONFIG.get("chef.count"));

        for (int x = 1; x <= chefCount; x++) {
            Logger.logToConsole("Chef " + x + " created.");
            Thread thread = new Thread(new Chef(mealQueue));
            thread.setName("Chef " + x);
            thread.start();
        }
    }

    // Creating Tables
    private static List<Table> createTables() {
        int tableCount = Integer.valueOf((String) CONFIG.get("table.count"));
        int maxClientGroupSize = Integer.valueOf((String) CONFIG.get("clientgroup.maxsize"));

        List<Table> tables = new ArrayList<>(tableCount + 1);
        tables.add(new Table(maxClientGroupSize));
        for (int x = 0; x < tableCount; x++) {
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
