import services.Service;

import java.util.*;

public class RunApp {
    public static void main(String[] args) {
        System.out.println(Service.buildNumber(String.valueOf(args[0])));
    }

}
