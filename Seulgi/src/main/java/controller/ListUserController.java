package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Collection;

public class ListUserController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        String auth = httpRequest.getHeader("logined");
        if ("true".equals(auth)) {
            try {
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

                byte[] data = body.replace("<%userList%>", URLDecoder.decode(String.valueOf(userStr), "UTF-8")).getBytes();
                httpResponse.response200Header(data.length, "html");
                httpResponse.responseBody(data);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            httpResponse.sendRedirect("/user/login.html");
        }
    }
}
