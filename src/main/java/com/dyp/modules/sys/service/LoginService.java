package com.dyp.modules.sys.service;

import com.dyp.modules.sys.model.LoginResult;
import org.apache.shiro.session.Session;


public interface LoginService {
    LoginResult login(String userName, String password);
    String getCurrentUserName();
    Session getSession();
    void logout();
}
