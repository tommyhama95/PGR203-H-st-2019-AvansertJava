package no.kristiania.http.controllers;

import no.kristiania.dao.daos.UserDao;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class UserValueController extends AbstractDaoController {

    private final UserDao userDao;
    private String name;
    private String email;
    private final String undefined = "[Undefined]";

    public UserValueController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(HttpMessage.getQueryString(requestTarget));
        try{
            if(requestAction.equals("POST")){
                query = HttpMessage.parseQueryString(body);
                setUrlQuery(HttpMessage.getQueryString(body));
                String userId = query.get("userid");
                name = query.get("userName");
                email = query.get("userEmail");
                name = checkValue(name);
                email = checkValue(email);
                userDao.updateUserValues(name, email, Long.parseLong(userId));
                serverRedirectResponse(query, out,
                        "http://localhost:8080/index.html");
                return;
            }
            serverResponse(query, out);
        } catch (SQLException e){
            serverErrorResponse(out, e);
        }
    }


    protected String getBody() throws SQLException {
        return null;
    }
}
