package restaurant.util;

import restaurant.Entrance;
import restaurant.client.ClientGroup;
import restaurant.service.Chef;

public class Logout {
    public static void exit() {
        long endTime = System.currentTimeMillis();

        double runTime = (endTime - Entrance.START_TIME) / 1000;
        System.out.println("Runtime: " + runTime + " seconds.");

        int clientGroupNum = ClientGroup.getClientGroupCount();
        int clientNum = ClientGroup.getClientCount();
        int mealNum = Chef.getMealNum();

        System.out.println(clientNum + " clients in " + clientGroupNum + " groups");
        System.out.println("Average " + clientNum / clientGroupNum + " client/group");
        System.out.println("Average service time: " + clientNum / runTime * 100 + " clients/100 second");
        System.out.println("Average service time: " + clientGroupNum / runTime * 100 + " client groups/100 second");
        System.out.println("Main courses eaten: " + mealNum);
        System.out.println("Average eating time: " + mealNum / runTime * 100 + " Main course/100 second");

        System.exit(0);
    }
}
