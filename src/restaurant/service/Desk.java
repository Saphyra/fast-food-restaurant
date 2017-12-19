package restaurant.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import restaurant.client.Client;
import restaurant.client.ClientGroup;
import restaurant.meals.Meal;
import restaurant.meals.MealOrder;
import restaurant.meals.MealPackage;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Desk implements Runnable {
    private static int deskCount = 0;

    private static final String MAX_ORDER_TIME = "desk.maxordertime";
    private static final String MIN_ORDER_TIME = "desk.minordertime";

    private long minOrderTime = PropLoader.getLongProperty(MIN_ORDER_TIME);
    private long maxOrderTime = PropLoader.getLongProperty(MAX_ORDER_TIME);

    private final String name;
    private final BlockingQueue<ClientGroup> deskQueue;
    private final BlockingQueue<MealOrder> mealQueue;
    private final BlockingQueue<MealPackage> mealPackageQueue;
    private final BlockingQueue<ClientGroup> tableQueue;

    private Desk(String name, BlockingQueue<ClientGroup> deskQueue, BlockingQueue<MealOrder> mealQueue, BlockingQueue<MealPackage> mealPackageQueue,
            BlockingQueue<ClientGroup> tableQueue) {
        this.name = name;
        this.deskQueue = deskQueue;
        this.mealQueue = mealQueue;
        this.mealPackageQueue = mealPackageQueue;
        this.tableQueue = tableQueue;
    }

    public static Desk deskFactory(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<MealOrder> mealQueue, BlockingQueue<MealPackage> mealPackageQueue,
            BlockingQueue<ClientGroup> tableQueue) {
        deskCount++;
        String deskName = "Desk " + deskCount;

        Desk desk = new Desk(deskName, deskQueue, mealQueue, mealPackageQueue, tableQueue);
        return desk;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientGroup group = deskQueue.take();

                Logger.logToErr(group + " are giving the orders.");
                Map<String, Client> members = group.getGroupMembers();

                requestMeals(members);
                waitForCooking(members);
                sendGroupToTableService(group);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestMeals(Map<String, Client> members) throws InterruptedException {
        Set<String> clientNames = members.keySet();
        for (String clientName : clientNames) {
            Client client = members.get(clientName);
            requestMeal(client);
            Logger.logToConsole(client + "'s order is taken.");
        }
    }

    private void requestMeal(Client client) throws InterruptedException {
        List<MealOrder> mealOrders = client.getMealOrderList();

        for (MealOrder mealOrder : mealOrders) {
            long sleepTime = Random.randLong(minOrderTime, maxOrderTime);
            mealOrder.setDeskName(name);
            Thread.sleep(sleepTime);

            mealQueue.put(mealOrder);
        }
    }

    private void waitForCooking(Map<String, Client> members) throws InterruptedException {
        do {
            Thread.sleep(100);
            serveMeals(members);
        } while (!areMealsCooked(members));
    }

    private void serveMeals(Map<String, Client> members) throws InterruptedException {
        MealPackage mealPackage = mealPackageQueue.take();

        if (isItsOwn(mealPackage)) {
            String clientname = mealPackage.getClientName();
            Meal meal = mealPackage.getMeal();

            Client ownerClient = members.get(clientname);
            ownerClient.addMeal(meal);
        } else {
            mealPackageQueue.put(mealPackage);
        }
    }

    private boolean isItsOwn(MealPackage mealPackage) {
        return mealPackage.getDeskName() == name;
    }

    private boolean areMealsCooked(Map<String, Client> members) {
        boolean allReady = true;
        Set<String> memberNames = members.keySet();

        for (String clientName : memberNames) {
            Client client = members.get(clientName);
            if (!client.isGotHisFood()) {
                allReady = false;
            }
        }
        return allReady;
    }

    private void sendGroupToTableService(ClientGroup group) throws InterruptedException {
        tableQueue.put(group);
        Logger.logToErr(group + " has got his food and searching for table.");
    }

    public String getName() {
        return name;
    }
}
