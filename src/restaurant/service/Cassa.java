package restaurant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.util.Logger;
import restaurant.util.Logout;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Cassa implements Runnable {
    private static long END_TIME = 0;
    private static int clientNum = 0;
    private static final List<String> payOrder = new ArrayList<>();

    private static final String MAX_PAY_TIME = "cassa.maxpaytime";
    private static final String MIN_PAY_TIME = "cassa.minpaytime";
    private long minPayTime = PropLoader.getLongProperty(MIN_PAY_TIME);
    private long maxPayTime = PropLoader.getLongProperty(MAX_PAY_TIME);

    private BlockingQueue<ClientGroup> cassaQueue;

    public Cassa(BlockingQueue<ClientGroup> cassaQueue) {
        this.cassaQueue = cassaQueue;
    }

    @Override
    public void run() {
        try {
            while (isMoreClients()) {
                processPayment();
            }
            closingTheDoors();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processPayment() throws InterruptedException {
        ClientGroup group = cassaQueue.take();
        clientNum += group.getClientNum();

        long sleepTime = Random.randLong(minPayTime, maxPayTime);
        Thread.sleep(sleepTime);
        Logger.logToErr(group + " has paid and left the restaurant.");
        Logger.logToErr("Generated clients: " + ClientGroup.getClientCount() + " -- Paid clients: " + clientNum);
        payOrder.add(group.toString());
    }

    private void closingTheDoors() throws InterruptedException {
        Logger.logToErr("All customers have had his meal. The restaurant now closes.");
        END_TIME = System.currentTimeMillis();

        while (!Logger.isQueueEmpty()) {
            Thread.sleep(1000);
        }
        Logout.exit();
    }

    private boolean isMoreClients() {
        return !(clientNum == ClientGroup.getClientCount());
    }

    public static long getEND_TIME() {
        return END_TIME;
    }

    public static List<String> getPayorder() {
        return payOrder;
    }
}
