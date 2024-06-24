package webserver;

import static util.Constants.CONTENTE_LENGTH;
import static util.Constants.COOKIE_PAIR_SEPARATOR;
import static util.Constants.COOKIE_SEGMENT_SEPARATOR;
import static util.Constants.EQUAL_SYMBOL;
import static util.Constants.SIGN_IN_PAGE;
import static util.Constants.SIGN_IN_PATH;
import static util.Constants.SIGN_UP_PATH;
import static util.Constants.USER_LIST_PAGE;
import static util.Constants.USER_LIST_PATH;
import static util.Constants.WEB_ROOT;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private model.User user = new User();
    private IOUtils ioUtils = new IOUtils();
    private HttpRequestUtils httpRequestUtils = new HttpRequestUtils();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream is = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            DataOutputStream dos = new DataOutputStream(out);

            // HTTP Start Line
            String startLine = httpRequestUtils.getStartLine(br);
            String requestMethod = httpRequestUtils.getRequestMethod(startLine);
            String requestTarget = httpRequestUtils.getRequestTarget(startLine);

            // HTTP Header
            Map<String, String> httpHeader = httpRequestUtils.getHeader(br);

            // 로그인 여부 확인
            Boolean logined = false;

            if (httpHeader.containsKey("Cookie")) {
                String[] cookieSegments = httpHeader.get("Cookie").split(COOKIE_SEGMENT_SEPARATOR);
                for (String cookieSegment : cookieSegments) {
                    String[] cookiePairs = cookieSegment.split(COOKIE_PAIR_SEPARATOR);

                    for (String cookiePair : cookiePairs) {
                        String cookieKey = cookiePair.split(EQUAL_SYMBOL)[0];
                        String cookieValue = cookiePair.split(EQUAL_SYMBOL)[1];

                        if (cookieKey.equals("logined") && cookieValue.equals("true")) {
                            logined = true;
                        }
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            // 요청 확인
            if (requestMethod.equals("GET") && requestTarget.equals(USER_LIST_PATH)) {
                if (!logined) {
                    requestTarget = SIGN_IN_PAGE;
                    response302Header(dos, requestTarget);
                }

                if (logined) {
                    int idx = 3;
                    Collection<User> users = DataBase.findAll();
                    for(User user : users) {
                        sb.append("<tr>\n");
                        sb.append("<th scope=\"row\">" + idx+ "</th> <td>" + user.getUserId() + "</td> <td>" + user.getName() + "</td> <td>" + user.getEmail() + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
                        sb.append("</tr>\n");
                    }

                    requestTarget = USER_LIST_PAGE;
                }
            }

            if (requestMethod.equals("POST")) {
                String httpBody = ioUtils.readData(br, Integer.parseInt(httpHeader.get(CONTENTE_LENGTH)));

                if (requestTarget.startsWith(SIGN_UP_PATH)) {
                    user.signUp(httpRequestUtils.parseRequestParams(httpBody));

                    requestTarget = "/index.html";

                    response302Header(dos, requestTarget);
                }

                if (requestTarget.startsWith(SIGN_IN_PATH)) {
                    boolean signInSuccess = user.signIn(httpRequestUtils.parseRequestParams(httpBody));

                    if (signInSuccess) {
                        requestTarget = "/index.html";
                        response302SignInSuccessHeader(dos, requestTarget);
                    } else {
                        requestTarget = "/user/login_failed.html";
                        response302SignInFailureHeader(dos, requestTarget);
                    }
                }
            }
            String targetPath = WEB_ROOT + requestTarget;

            br = new BufferedReader(new FileReader(targetPath));
            StringBuilder stringB = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                stringB.append(line).append(System.lineSeparator());
            }

            br.close();
            String content = stringB.toString();
            content = content.replace("%userList%", URLDecoder.decode(sb.toString(), "UTF-8")); // Todo: 출력만이 아닌 server에 값을 제대로 저장하기

            // file로 읽는 방법
//            File targetFile = new File(targetPath);
//            byte[] responseBody = Files.readAllBytes(targetFile.toPath());
            byte[] responseBody = content.getBytes();

            response200Header(dos, responseBody.length);
            responseBody(dos, responseBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302SignInSuccessHeader(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("Set-Cookie: logined=true; path=/ \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302SignInFailureHeader(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("Set-Cookie: logined=false; path=/ \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
