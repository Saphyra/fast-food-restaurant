package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.util.Constants;
import restaurant.util.Logger;
import restaurant.util.Random;

public class Cassa implements Runnable {
    private BlockingQueue<ClientGroup> cassaQueue;

    public Cassa(BlockingQueue<ClientGroup> cassaQueue) {
        this.cassaQueue = cassaQueue;
    }

    // "Paying..."
    @Override
    public void run() {
        while (true) {
            try {
                ClientGroup group = cassaQueue.take();
                long sleepTime = Random.randLong(Constants.MIN_PAY_TIME, Constants.MAX_PAY_TIME);
                Thread.sleep(sleepTime);
                Logger.logToErr(group + " has paid and left the restaurant.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
