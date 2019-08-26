package com.liwinon.itevent.service;

import net.sf.json.JSONObject;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface MissionService {
    Map<String,Object> queryMission(String uuid);

    JSONObject CanTurnOverUsers(String uuid, String qyid, HttpServletRequest request);

    JSONObject changeUser(String toPersonid, String fromPersonid, String uuid,String fromQyid,HttpServletRequest request);
}
