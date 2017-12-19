package restaurant.util;

import java.util.List;

import restaurant.Entrance;
import restaurant.client.ClientGroup;
import restaurant.service.Cassa;
import restaurant.service.Chef;
import restaurant.service.Table;

public class Logout {

    public static void exit() {
        logLogQueueSize();
        logPayOrder();
        printStatistics();

        System.exit(0);
    }

    private static void logLogQueueSize() {
        System.out.println("\nLogSize:");

        List<LogSize> logQueueSizeList = Logger.getQueueSizeList();
        for (int index = 0; index < logQueueSizeList.size(); index++) {
            LogSize logSize = logQueueSizeList.get(index);
            logSize.print();
        }
    }

    private static void printStatistics() {
        double runTime = (Cassa.getEND_TIME() - Entrance.START_TIME) / 1000;
        double clientGroupNum = ClientGroup.getClientGroupCount();
        double clientNum = ClientGroup.getClientCount();
        int mealNum = Chef.getMealNum();
        double averageClientPerGroup = clientNum / clientGroupNum;
        double logMessageCount = Logger.getLogMessageCount();

        writeLog(runTime, clientGroupNum, clientNum, mealNum, averageClientPerGroup, logMessageCount);
    }

    private static void writeLog(double runTime, double clientGroupNum, double clientNum, int mealNum, double averageClientPerGroup, double logMessageCount) {
        System.out.println();
        System.out.println("Runtime: " + runTime + " seconds.");
        System.out.println(clientNum + " clients in " + clientGroupNum + " groups");
        System.out.println("Average " + averageClientPerGroup + " client/group");
        System.out.println("Table places in the restaurant: " + Table.getTablePlaces());
        System.out.println("Average service time: " + clientNum / runTime * 100 + " clients/100 second");
        System.out.println("Average service time: " + clientGroupNum / runTime * 100 + " client groups/100 second");
        System.out.println("Meals eaten: " + mealNum);
        System.out.println("Average eating time: " + mealNum / runTime * 100 + " Meals/100 second");
        System.out.println("Log messages printed: " + logMessageCount);
        System.out.println("Average log messages: " + logMessageCount / runTime * 100 + " message/100 second");
    }

    private static void logPayOrder() {
        System.out.println("\nPay order:");
        List<String> payOrder = Cassa.getPayorder();
        for (String element : payOrder) {
            System.out.println(element);
        }
    }
}
