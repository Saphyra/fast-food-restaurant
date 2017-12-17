package restaurant.service;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import restaurant.client.Client;
import restaurant.client.ClientGroup;
import restaurant.meals.Cookable;
import restaurant.meals.Meal;
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
    private BlockingQueue<ClientGroup> deskQueue;
    private BlockingQueue<Cookable> mealQueue;
    private BlockingQueue<ClientGroup> tableQueue;

    private Desk(String name, BlockingQueue<ClientGroup> deskQueue, BlockingQueue<Cookable> mealQueue, BlockingQueue<ClientGroup> tableQueue) {
        this.name = name;
        this.deskQueue = deskQueue;
        this.mealQueue = mealQueue;
        this.tableQueue = tableQueue;
    }

    public static Desk deskFactory(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<Cookable> mealQueue, BlockingQueue<ClientGroup> tableQueue) {
        deskCount++;
        String deskName = "Desk " + deskCount;
        Desk desk = new Desk(deskName, deskQueue, mealQueue, tableQueue);
        return desk;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientGroup group = deskQueue.take();

                Logger.logToErr(group + " are giving the orders.");
                List<Client> members = group.getGroupMembers();

                requestMeals(members);
                waitForCooking(members);
                sendGroupToTableService(group);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Group Members say their orders...
    private void requestMeals(List<Client> members) throws InterruptedException {
        for (Client client : members) {
            requestMeal(client);
            Logger.logToConsole(client + "'s order is taken.");
        }
    }

    // Deskman asks Chefs for making the meal.
    private void requestMeal(Client client) throws InterruptedException {
        List<Meal> meals = client.getMealList();

        for (Meal meal : meals) {
            long sleepTime = Random.randLong(minOrderTime, maxOrderTime);
            Thread.sleep(sleepTime);

            mealQueue.put(meal);
        }
    }

    // Waiting til all Group Members get all of their Main Courses
    private void waitForCooking(List<Client> members) throws InterruptedException {
        do {
            Thread.sleep(100);
        } while (!areMealsCooked(members));
    }

    private boolean areMealsCooked(List<Client> members) {
        boolean allReady = true;

        for (Client client : members) {
            List<Meal> meals = client.getMealList();

            for (Meal meal : meals) {
                if (!meal.isCooked()) {
                    allReady = false;
                }
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
