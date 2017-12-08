package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.util.Constants;
import restaurant.util.Logger;
import restaurant.util.Random;

public class ClientGenerator implements Runnable {
    private BlockingQueue<ClientGroup> deskQueue;
    private BlockingQueue<ClientGroup> cassaQueue;

    public ClientGenerator(BlockingQueue<ClientGroup> deskQueue, BlockingQueue<ClientGroup> cassaQueue) {
        this.deskQueue = deskQueue;
        this.cassaQueue = cassaQueue;
    }

    // Creating new Client Groups til the limit reaches
    @Override
    public void run() {
        while (ClientGroup.getClientCount() < Constants.CLIENT_NUM) {
            try {
                createClientGroup();
                long sleepTime = Random.randLong(Constants.MIN_CLIENT_GROUP_ARRIVAL, Constants.MAX_CLIENT_GROUP_ARRIVAL);
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
