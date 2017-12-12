package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.Entrance;
import restaurant.client.ClientGroup;
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
        int clientNum = Integer.valueOf((String) Entrance.CONFIG.get("clientgenerator.clientnum"));
        long minDelay = Long.valueOf((String) Entrance.CONFIG.get("clientgenerator.mindelay"));
        long maxDelay = Long.valueOf((String) Entrance.CONFIG.get("clientgenerator.maxdelay"));

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
