package restaurant.service;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import restaurant.client.Client;
import restaurant.client.ClientGroup;
import restaurant.meals.MainCourse;
import restaurant.util.Constants;
import restaurant.util.Logger;
import restaurant.util.Random;

public class Desk implements Runnable {

    private BlockingQueue<ClientGroup> deskQueue;
    private BlockingQueue<MainCourse> mealQueue;
    private BlockingQueue<ClientGroup> tableQueue;

    public Desk(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<MainCourse> mealQueue, BlockingQueue<ClientGroup> tableQueue) {
        this.deskQueue = deskQueue;
        this.mealQueue = mealQueue;
        this.tableQueue = tableQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientGroup group = deskQueue.take();
                takeOrder(group);
                sendToTableService(group);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Taking the Group Members' orders.
    private void takeOrder(ClientGroup group) throws InterruptedException {
        Logger.logToErr(group + " are giving the orders.");
        Vector<Client> members = group.getGroupMembers();
        giveOrders(members);
        waitForService(members);
    }

    // Group Members say their orders...
    private void giveOrders(Vector<Client> members) throws InterruptedException {
        for (Client client : members) {

            giveRequest(client);

            Logger.logToConsole(client + "'s order is taken.");
        }
    }

    // Deskman asks Chefs for making the meal.
    private void giveRequest(Client client) throws InterruptedException {
        Vector<MainCourse> meals = client.getMealList();
        for (MainCourse meal : meals) {

            long sleepTime = Random.randLong(Constants.MIN_ORDER_TIME, Constants.MAX_ORDER_TIME);
            Thread.sleep(sleepTime);

            mealQueue.put(meal);
        }
    }

    // Waiting til all Group Members get all of their Main Courses
    private void waitForService(Vector<Client> members) throws InterruptedException {
        boolean allReady;
        do {
            allReady = true;

            Thread.sleep(100);

            for (Client client : members) {
                Vector<MainCourse> meals = client.getMealList();

                for (MainCourse meal : meals) {
                    if (!meal.isCooked()) {
                        allReady = false;
                    }
                }
            }
        } while (!allReady);
    }

    private void sendToTableService(ClientGroup group) throws InterruptedException {
        Logger.logToErr(group + " has got his food and searching for table.");
        tableQueue.put(group);
    }
}
