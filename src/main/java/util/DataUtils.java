package util;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class DataUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * User 모델 생성
     * @param req
     * @return User
     */
    public static User createUser(String req) {
        String[] userArr = req.split("&");
        String userId = userArr[0].split("=")[1];
        String password = userArr[1].split("=")[1];
        String name = userArr[2].split("=")[1];
        String email = userArr[3].split("=")[1];

        User user = new User(userId, password, name, email);

        DataBase.addUser(user);

        return user;
    }
}
