package restaurant.util;

import java.util.List;

import restaurant.Entrance;
import restaurant.client.ClientGroup;
import restaurant.service.Cassa;
import restaurant.service.Chef;
import restaurant.service.Table;

public class Logout {
    public static void exit() {
        double runTime = (Cassa.getEND_TIME() - Entrance.START_TIME) / 1000;
        double clientGroupNum = ClientGroup.getClientGroupCount();
        double clientNum = ClientGroup.getClientCount();
        int mealNum = Chef.getMealNum();
        double averageClientPerGroup = clientNum / clientGroupNum;

        writeLog(runTime, clientGroupNum, clientNum, mealNum, averageClientPerGroup);
        logPayOrder();

        System.exit(0);
    }

    private static void writeLog(double runTime, double clientGroupNum, double clientNum, int mealNum, double averageClientPerGroup) {
        System.out.println("Runtime: " + runTime + " seconds.");
        System.out.println(clientNum + " clients in " + clientGroupNum + " groups");
        System.out.println("Average " + averageClientPerGroup + " client/group");
        System.out.println("Table places in the restaurant: " + Table.getTablePlaces());
        System.out.println("Average service time: " + clientNum / runTime * 100 + " clients/100 second");
        System.out.println("Average service time: " + clientGroupNum / runTime * 100 + " client groups/100 second");
        System.out.println("Main courses eaten: " + mealNum);
        System.out.println("Average eating time: " + mealNum / runTime * 100 + " Main course/100 second");
    }

    private static void logPayOrder() {
        System.out.println("\nPay order:");
        List<String> payOrder = Cassa.getPayorder();
        for (String element : payOrder) {
            System.out.println(element);
        }
    }
}
