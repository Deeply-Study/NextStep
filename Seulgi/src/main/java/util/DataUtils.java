package util;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class DataUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * 모든 User 출력하기
     * @return
     */
    public static String getUserAll() throws IOException{
        StringBuilder userStr = new StringBuilder("<center><h3> user list ! 🐳 </h3></center>").append("\n");
        Collection<User> allUsers = DataBase.findAll();
        String body = new String(Files.readAllBytes(new File("./webapp/user/list.html").toPath()));

        int idx = 0;

        for (User user : allUsers) {
            idx++;
            userStr.append("<tr>");
            userStr.append("<th scope=\"row\">" + idx + "</th>" +
                    "<td>" + user.getUserId() + "</td>" +
                    "<td>" + user.getName() + "</td>" +
                    "<td>" + user.getEmail() + "</td>" +
                    "<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            userStr.append("</tr>");
        }

        return body.replace("<%userList%>", URLDecoder.decode(String.valueOf(userStr), "UTF-8"));
    }
}
