package com.liwinon.itevent.service;

import org.springframework.ui.Model;

import java.util.Map;

public interface MissionService {
    Map<String,Object> queryMission(String uuid);
}
