package restaurant.client;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import restaurant.Entrance;
import restaurant.service.Table;
import restaurant.util.Logger;
import restaurant.util.Random;

//Groupping Clients
public class ClientGroup implements Runnable {
    private static int clientCount = 0;
    private static int clientGroupCount = 0;

    private final Vector<Client> groupMembers = new Vector<>();

    private final String id;
    private final int clientNum;
    private Table table;
    private BlockingQueue<ClientGroup> cassaQueue;

    private ClientGroup(Vector<Client> groupMembers, String id, BlockingQueue<ClientGroup> cassaQueue) {
        this.groupMembers.addAll(groupMembers);
        this.id = id;
        this.clientNum = groupMembers.size();
        this.cassaQueue = cassaQueue;
        Logger.logToErr(toString() + " group created");
    }

    // Creating new group of Clients
    public static ClientGroup clientGroupFactory(BlockingQueue<ClientGroup> cassaQueue) {
        int minClientGroupSize = Integer.valueOf((String) Entrance.CONFIG.get("clientgroup.minsize"));
        int maxClientGroupSize = Integer.valueOf((String) Entrance.CONFIG.get("clientgroup.maxsize"));

        int clientNum = Random.randInt(minClientGroupSize, maxClientGroupSize);
        String groupid = "ClientGroup" + ++clientGroupCount;
        Vector<Client> groupMembers = createClients(clientNum, groupid);

        return new ClientGroup(groupMembers, groupid, cassaQueue);
    }

    // Creating Clients
    private static Vector<Client> createClients(int clientNum, String groupid) {
        Vector<Client> groupMembers = new Vector<>();
        for (int x = 0; x < clientNum; x++) {
            clientCount++;
            groupMembers.add(new Client(clientCount, groupid));
        }
        return groupMembers;
    }

    @Override
    public void run() {
        startEating();
        waitingForMembers();
        table.setFree(true);
    }

    // Starting Client threads
    private void startEating() {
        for (Client client : groupMembers) {
            Thread cl = new Thread(client);
            cl.setName(client.toString());
            cl.start();
        }
    }

    // Waiting for members eating their food
    private void waitingForMembers() {
        boolean allAte = true;
        try {
            do {

                allAte = true;
                for (Client client : groupMembers) {
                    if (!client.isReadyWithFood()) {
                        allAte = false;
                    }
                }
                Thread.sleep(500);

            } while (!allAte);

            Logger.logToErr(toString() + " has finished eating. Going to cassa.");
            cassaQueue.put(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return id + " [" + clientNum + "]";
    }

    public Vector<Client> getGroupMembers() {
        return groupMembers;
    }

    public static int getClientCount() {
        return clientCount;
    }

    public static int getClientGroupCount() {
        return clientGroupCount;
    }

    public int getClientNum() {
        return clientNum;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
