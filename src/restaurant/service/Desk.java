package restaurant.service;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import restaurant.client.Client;
import restaurant.client.ClientGroup;
import restaurant.meals.Cookable;
import restaurant.meals.Meal;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Desk implements Runnable {

    private static final String MAX_ORDER_TIME = "desk.maxordertime";
    private static final String MIN_ORDER_TIME = "desk.minordertime";
    private long minOrderTime = PropLoader.getLongProperty(MIN_ORDER_TIME);
    private long maxOrderTime = PropLoader.getLongProperty(MAX_ORDER_TIME);

    private BlockingQueue<ClientGroup> deskQueue;
    private BlockingQueue<Cookable> mealQueue;
    private BlockingQueue<ClientGroup> tableQueue;

    public Desk(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<Cookable> mealQueue, BlockingQueue<ClientGroup> tableQueue) {
        this.deskQueue = deskQueue;
        this.mealQueue = mealQueue;
        this.tableQueue = tableQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientGroup group = deskQueue.take();

                Logger.logToErr(group + " are giving the orders.");
                Vector<Client> members = group.getGroupMembers();

                requestMeals(members);
                waitForCooking(members);
                sendGroupToTableService(group);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Group Members say their orders...
    private void requestMeals(Vector<Client> members) throws InterruptedException {
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
    private void waitForCooking(Vector<Client> members) throws InterruptedException {
        boolean allReady;
        do {
            allReady = true;
            Thread.sleep(100);

            for (Client client : members) {
                List<Meal> meals = client.getMealList();

                for (Meal meal : meals) {
                    if (!meal.isCooked()) {
                        allReady = false;
                    }
                }
            }
        } while (!allReady);
    }

    private void sendGroupToTableService(ClientGroup group) throws InterruptedException {
        tableQueue.put(group);
        Logger.logToErr(group + " has got his food and searching for table.");
    }
}
