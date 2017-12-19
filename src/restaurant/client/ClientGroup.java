package restaurant.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import restaurant.service.Table;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class ClientGroup implements Runnable {
    private static final String MAX_CLIENT_GROUP_SIZE = "clientgroup.maxsize";
    private static final String MIN_CLIENT_GROUP_SIZE = "clientgroup.minsize";

    private static volatile int clientCount = 0;
    private static int clientGroupCount = 0;

    private final Map<String, Client> groupMembers;
    private final String id;
    private final int clientNum;
    private Table table;
    private BlockingQueue<ClientGroup> cassaQueue;

    private ClientGroup(Map<String, Client> groupMembers, String id, BlockingQueue<ClientGroup> cassaQueue) {
        this.groupMembers = Collections.synchronizedMap(groupMembers);
        this.id = id;
        clientNum = groupMembers.size();
        this.cassaQueue = cassaQueue;
    }

    public static ClientGroup clientGroupFactory(BlockingQueue<ClientGroup> cassaQueue) {
        String groupid = "ClientGroup" + ++clientGroupCount;
        Map<String, Client> groupMembers = createClients(groupid);

        ClientGroup group = new ClientGroup(groupMembers, groupid, cassaQueue);
        Logger.logToErr(group.toString() + " group created");
        return group;
    }

    private static Map<String, Client> createClients(String groupid) {
        int minClientGroupSize = PropLoader.getIntegerProperty(MIN_CLIENT_GROUP_SIZE);
        int maxClientGroupSize = PropLoader.getIntegerProperty(MAX_CLIENT_GROUP_SIZE);

        int clientNum = Random.randInt(minClientGroupSize, maxClientGroupSize);
        Map<String, Client> groupMembers = Collections.synchronizedMap(new HashMap<>());

        for (int x = 0; x < clientNum; x++) {
            Client client = createRandomClient(clientNum, groupid);
            groupMembers.put(client.getName(), client);
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
        Set<String> clientNames = groupMembers.keySet();
        for (String clientName : clientNames) {
            Client client = groupMembers.get(clientName);
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
        Set<String> clientNames = groupMembers.keySet();
        for (String clientName : clientNames) {
            Client client = groupMembers.get(clientName);
            {
                if (!client.isReadyWithFood()) {
                    allAte = false;
                }
            }
        }
        return allAte;
    }

    @Override
    public String toString() {
        return id + " [" + clientNum + "]";
    }

    public Map<String, Client> getGroupMembers() {
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
