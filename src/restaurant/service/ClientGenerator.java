package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class ClientGenerator implements Runnable {
    private static final String MAX_DELAY = "clientgenerator.maxdelay";
    private static final String MIN_DELAY = "clientgenerator.mindelay";
    private static final String CLIENT_NUM = "clientgenerator.clientnum";
    private BlockingQueue<ClientGroup> deskQueue;
    private BlockingQueue<ClientGroup> cassaQueue;

    public ClientGenerator(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<ClientGroup> cassaQueue) {
        this.deskQueue = deskQueue;
        this.cassaQueue = cassaQueue;
    }

    // Creating new Client Groups til the limit reaches
    @Override
    public void run() {
        int clientNum = PropLoader.getIntegerProperty(CLIENT_NUM);
        long minDelay = PropLoader.getLongProperty(MIN_DELAY);
        long maxDelay = PropLoader.getLongProperty(MAX_DELAY);

        while (ClientGroup.getClientCount() < clientNum) {
            try {
                createClientGroup();
                long sleepTime = Random.randLong(minDelay, maxDelay);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.logToErr("No more customers.");
    }

    private void createClientGroup() throws InterruptedException {
        ClientGroup group = ClientGroup.clientGroupFactory(cassaQueue);
        deskQueue.put(group);
        Logger.logToErr("New clients!");
    }
}
