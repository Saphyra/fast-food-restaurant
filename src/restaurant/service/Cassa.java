package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.client.ClientGroup;
import restaurant.util.Constants;
import restaurant.util.Logger;
import restaurant.util.Logout;
import restaurant.util.Random;

public class Cassa implements Runnable {
    private static int clientNum = 0;
    private BlockingQueue<ClientGroup> cassaQueue;

    public Cassa(BlockingQueue<ClientGroup> cassaQueue) {
        this.cassaQueue = cassaQueue;
    }

    // "Paying..."
    @Override
    public void run() {
        try {
            while (true) {

                ClientGroup group = cassaQueue.take();
                clientNum += group.getClientNum();
                long sleepTime = Random.randLong(Constants.MIN_PAY_TIME, Constants.MAX_PAY_TIME);
                Thread.sleep(sleepTime);
                Logger.logToErr(group + " has paid and left the restaurant.");

                if (!isMoreClients()) {
                    Logger.logToErr("All customers have had his meal. The restaurant now closes.");
                    break;
                }
            }
            while (!Logger.isQueueEmpty()) {
                Thread.sleep(1000);
            }
            Logout.exit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isMoreClients() {
        Logger.logToErr("Generated customers: " + ClientGroup.getClientCount() + " -- Paid clients: " + clientNum);
        return !(clientNum == ClientGroup.getClientCount());
    }
}
