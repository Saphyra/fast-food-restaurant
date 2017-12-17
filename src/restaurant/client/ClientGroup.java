package restaurant.client;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import restaurant.service.Table;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class ClientGroup implements Runnable {
    private static final String MAX_CLIENT_GROUP_SIZE = "clientgroup.maxsize";
    private static final String MIN_CLIENT_GROUP_SIZE = "clientgroup.minsize";

    private static int clientCount = 0;
    private static int clientGroupCount = 0;

    private final List<Client> groupMembers = new Vector<>();
    private final String id;
    private final int clientNum;
    private Table table;
    private BlockingQueue<ClientGroup> cassaQueue;

    private ClientGroup(List<Client> groupMembers, String id, BlockingQueue<ClientGroup> cassaQueue) {
        this.groupMembers.addAll(groupMembers);
        this.id = id;
        clientNum = groupMembers.size();
        this.cassaQueue = cassaQueue;
    }

    public static ClientGroup clientGroupFactory(BlockingQueue<ClientGroup> cassaQueue) {
        String groupid = "ClientGroup" + ++clientGroupCount;
        List<Client> groupMembers = createClients(groupid);

        ClientGroup group = new ClientGroup(groupMembers, groupid, cassaQueue);
        Logger.logToErr(group.toString() + " group created");
        return group;
    }

    private static List<Client> createClients(String groupid) {
        int minClientGroupSize = PropLoader.getIntegerProperty(MIN_CLIENT_GROUP_SIZE);
        int maxClientGroupSize = PropLoader.getIntegerProperty(MAX_CLIENT_GROUP_SIZE);

        int clientNum = Random.randInt(minClientGroupSize, maxClientGroupSize);
        List<Client> groupMembers = new Vector<>(clientNum);

        for (int x = 0; x < clientNum; x++) {
            Client client = createRandomClient(clientNum, groupid);
            groupMembers.add(client);
        }

        return groupMembers;
    }

    private static Client createRandomClient(int clientNum, String groupid) {
        String groupName = "[" + groupid + " - " + clientNum + "]";
        Client client = new Client(++clientCount, groupName);
        Logger.logToConsole("New Client: " + client.toString());
        return client;
    }

    @Override
    public void run() {
        try {
            startEating();
            waitingForMembersEating();
            Logger.logToErr(toString() + " has finished eating. Going to cassa.");
            table.setFree(true);
            cassaQueue.put(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startEating() {
        for (Client client : groupMembers) {
            Thread clientThread = new Thread(client, client.toString());
            clientThread.start();
        }
    }

    private void waitingForMembersEating() throws InterruptedException {
        do {
            Thread.sleep(500);
        } while (!areClientsReadyWithEating());
    }

    private boolean areClientsReadyWithEating() {
        boolean allAte = true;
        for (Client client : groupMembers) {
            if (!client.isReadyWithFood()) {
                allAte = false;
            }
        }
        return allAte;
    }

    @Override
    public String toString() {
        return id + " [" + clientNum + "]";
    }

    public List<Client> getGroupMembers() {
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

    public void setTable(Table table) {
        this.table = table;
    }
}
