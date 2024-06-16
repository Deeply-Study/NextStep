package util;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class DataUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * User 모델 생성
     * @param br
     * @return User
     */
    public static boolean createUser(BufferedReader br) {
        int contentsLength = 0;
        try {
            String read = null;
            while ((read = br.readLine()) != null) {
                if (read != null && read.contains("Content-Length")) {
                    contentsLength = Integer.parseInt(IOUtils.bodyData(read));
                }

                if (read.length() == 0) {
                    String request = IOUtils.readData(br, contentsLength);
                    log.debug("request : {}", request);

                    String[] userArr = request.split("&");
                    String userId = userArr[0].split("=")[1];
                    String password = userArr[1].split("=")[1];
                    String name = userArr[2].split("=")[1];
                    String email = userArr[3].split("=")[1];

                    User user = new User(userId, password, name, email);

                    DataBase.addUser(user);

                    log.debug(DataBase.findUserById(userId).toString());

                    return true;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * User 로그인
     * @param br
     * @return Integer
     */
    public static Integer loginUser(BufferedReader br) {
        try {
            int contentsLength = 0;

            String read = null;
            while ((read = br.readLine()) != null) {
                if (read != null && read.contains("Content-Length")) {
                    contentsLength = Integer.parseInt(IOUtils.bodyData(read));
                }

                if (read.length() == 0) {
                    String request = IOUtils.readData(br, contentsLength);

                    String[] userArr = request.split("&");
                    String userId = userArr[0].split("=")[1];
                    String password = userArr[1].split("=")[1];

                    User user = DataBase.findUserById(userId);
                    if (user != null) {
                        if (password.equals(user.getPassword())) {
                            return 1;
                        }
                        return -2;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    /**
     * 로그인 상태인지 확인하기
     * @param br
     * @return
     */
    public static Boolean loginAuth(BufferedReader br) {
        String Cookies = "";
        try {
            String read = null;
            while ((read = br.readLine()) != null) {
                if (read != null && read.contains("Cookie")) {
                    Cookies = IOUtils.bodyData(read);
                    Map<String, String> cookies = HttpRequestUtils.parseCookies(Cookies);

                    if (!cookies.isEmpty()) {
                        if ("true".equals(cookies.get("logined"))) {
                            return true;
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 모든 User 출력하기
     * @return
     */
    public static String getUserAll() {
        StringBuilder userStr = new StringBuilder("user list ! 🐳").append("\n");
        Collection<User> allUsers = DataBase.findAll();

        for (User user : allUsers) {
            userStr.append("user name : " + user.getName());
            userStr.append("user email : " + user.getEmail());
            userStr.append("\n ----------------");
        }
        return String.valueOf(userStr);
    }
}
