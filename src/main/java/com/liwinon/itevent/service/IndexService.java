package com.liwinon.itevent.service;

import com.liwinon.itevent.entity.primary.Admin;

import javax.servlet.http.HttpServletRequest;

public interface IndexService {
    String login(String user, String pwd, HttpServletRequest request);
    Admin getAdmin(String user);
}
