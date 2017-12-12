package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.Entrance;
import restaurant.client.ClientGroup;
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

                long minPayTime = Long.valueOf((String) Entrance.CONFIG.get("cassa.minpaytime"));
                long maxPayTime = Long.valueOf((String) Entrance.CONFIG.get("cassa.maxpaytime"));

                long sleepTime = Random.randLong(minPayTime, maxPayTime);
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
