package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginUserController implements Controller{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        User user = DataBase.findUserById(httpRequest.getParameter("userId"));
        String redirectUrl;

        if (user != null && httpRequest.getParameter("password").equals(user.getPassword())) {
            redirectUrl = "/index.html";
            httpResponse.addResponseHeader("Set-Cookie", "logined=" + true);
        } else {
            redirectUrl = "/user/login_failed.html";
            httpResponse.addResponseHeader("Set-Cookie", "logined=" + false);
        }
        httpResponse.sendRedirect(redirectUrl);
    }
}
