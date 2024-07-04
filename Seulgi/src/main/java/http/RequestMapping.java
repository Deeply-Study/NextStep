package http;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginUserController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, Controller> contrllers = new HashMap<>();

    static {
        contrllers.put("/user/create", new CreateUserController());
        contrllers.put("/user/login", new LoginUserController());
        contrllers.put("/user/list.html", new ListUserController());
    }

    public static Controller getController(String requestUrl) {
        return contrllers.get(requestUrl);
    }
}
