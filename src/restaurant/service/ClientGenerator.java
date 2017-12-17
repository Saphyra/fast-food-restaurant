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

    private int clientNum = PropLoader.getIntegerProperty(CLIENT_NUM);
    private long minDelay = PropLoader.getLongProperty(MIN_DELAY);
    private long maxDelay = PropLoader.getLongProperty(MAX_DELAY);

    private BlockingQueue<ClientGroup> deskQueue;
    private BlockingQueue<ClientGroup> cassaQueue;

    public ClientGenerator(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<ClientGroup> cassaQueue) {
        this.deskQueue = deskQueue;
        this.cassaQueue = cassaQueue;
    }

    @Override
    public void run() {

        while (ClientGroup.getClientCount() < clientNum) {
            try {
                createClientGroup();
                long sleepTime = Random.randLong(minDelay, maxDelay);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.logToErr("No more clients.");
    }

    private void createClientGroup() throws InterruptedException {
        ClientGroup group = ClientGroup.clientGroupFactory(cassaQueue);
        Logger.logToErr("New clients! - " + ClientGroup.getClientCount() + "/" + PropLoader.getIntegerProperty(CLIENT_NUM));
        deskQueue.put(group);
    }
}
