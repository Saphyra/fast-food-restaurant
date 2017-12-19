package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.meals.MealOrder;
import restaurant.meals.MealPackage;
import restaurant.service.Cassa;
import restaurant.service.Chef;
import restaurant.service.ClientGenerator;
import restaurant.service.Desk;
import restaurant.service.Table;
import restaurant.service.TableService;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.TableSortingComparator;

public class Restaurant {
    private static final String DESK_QUEUE_SIZE = "entrance.deskqueuesize";
    private static final String MEAL_QUEUE_SIZE = "entrance.mealqueuesize";
    private static final String MEAL_PACKAGE_QUEUE_SIZE = "entrance.mealpackagequeuesize";
    private static final String TABLE_QUEUE_SIZE = "entrance.tablequeuesize";
    private static final String CASSA_QUEUE_SIZE = "entrance.cassaqueuesize";
    private static final String MAX_CLIENT_GROUP_SIZE = "clientgroup.maxsize";
    private static final String TABLE_COUNT = "table.count";
    private static final String CHEF_COUNT = "chef.count";
    private static final String WAITER_COUNT = "desk.waitercount";

    private final BlockingQueue<ClientGroup> deskQueue;
    private final BlockingQueue<MealOrder> mealQueue;
    private final BlockingQueue<MealPackage> mealPackageQueue;
    private final BlockingQueue<ClientGroup> tableQueue;
    private final BlockingQueue<ClientGroup> cassaQueue;

    public Restaurant() {
        deskQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(DESK_QUEUE_SIZE));
        mealQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(MEAL_QUEUE_SIZE));
        mealPackageQueue = new LinkedBlockingQueue<>();// (PropLoader.getIntegerProperty(MEAL_PACKAGE_QUEUE_SIZE));
        tableQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(TABLE_QUEUE_SIZE));
        cassaQueue = new ArrayBlockingQueue<>(PropLoader.getIntegerProperty(CASSA_QUEUE_SIZE));
    }

    public void start() {
        startClientGenerator();
        startDesks();
        startChefs();
        List<Table> tables = Collections.synchronizedList(createTables());
        startTableService(tables);
        startCassa();
    }

    private void startClientGenerator() {
        ClientGenerator clientGenerator = new ClientGenerator(deskQueue, cassaQueue);
        Thread generatorThread = new Thread(clientGenerator, "ClientGenerator");
        generatorThread.start();
        Logger.logToConsole("The doors of the restaurant are opened.");
    }

    private void startDesks() {
        int waiterCount = PropLoader.getIntegerProperty(WAITER_COUNT);

        for (int x = 1; x <= waiterCount; x++) {
            Desk desk = Desk.deskFactory(deskQueue, mealQueue, mealPackageQueue, tableQueue);
            Thread thread = new Thread(desk, desk.getName());
            thread.start();
            Logger.logToConsole(desk.getName() + " is waiting for clients.");
        }
    }

    private void startChefs() {
        int chefCount = PropLoader.getIntegerProperty(CHEF_COUNT);

        for (int x = 1; x <= chefCount; x++) {
            createChef();
        }
    }

    private void createChef() {
        Chef chef = Chef.chefFactory(mealQueue, mealPackageQueue);
        Thread chefThread = new Thread(chef, chef.getName());
        chefThread.start();
        Logger.logToConsole(chef.getName() + " is heating the ovens.");
    }

    private List<Table> createTables() {
        int tableCount = PropLoader.getIntegerProperty(TABLE_COUNT) - 1;
        int maxClientGroupSize = PropLoader.getIntegerProperty(MAX_CLIENT_GROUP_SIZE);

        List<Table> tables = createListWithMaxSizeTable(tableCount, maxClientGroupSize);
        createRandomTables(tableCount, tables);

        tables.sort(new TableSortingComparator());
        return tables;
    }

    private List<Table> createListWithMaxSizeTable(int tableCount, int maxClientGroupSize) {
        List<Table> tables = new ArrayList<>(tableCount + 1);
        tables.add(Table.tableFactory(maxClientGroupSize));
        return tables;
    }

    private void createRandomTables(int tableCount, List<Table> tables) {
        for (int x = 0; x < tableCount; x++) {
            Table table = Table.randomTableFactory();
            tables.add(table);
        }
    }

    private void startTableService(List<Table> tables) {
        TableService tableService = new TableService(tableQueue, tables);
        Thread tableServiceThread = new Thread(tableService, "TableService");
        tableServiceThread.start();
        Logger.logToConsole("Tables are cleaned.");
    }

    private void startCassa() {
        Cassa cassa = new Cassa(cassaQueue);
        Thread cassaThread = new Thread(cassa, "Cassa");
        cassaThread.start();
        Logger.logToConsole("Cassa is waiting for money.");
    }
}
