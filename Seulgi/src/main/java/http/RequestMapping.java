package http;

import controller.Controller;
import controller.CreateUserController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, Controller> contrllers = new HashMap<>();

    static {
        contrllers.put("/user/create", new CreateUserController());
    }
}
