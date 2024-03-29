package com.liwinon.itevent.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface EventService {
    String startBillEvent(int index, int event, HttpServletRequest request);

    String newAssetsEvent(int index, int event, HttpServletRequest request);

    String addItemEvent(int index, int event,HttpServletRequest request );

    String exchangeEvent(int index, int event,HttpServletRequest request);

    String borrowEvent(int index, int event,HttpServletRequest request);

    String backEvent(int index,int event,HttpServletRequest request);

    String scrapEvent(int index, int event, HttpServletRequest request);

    String oldAssetsEvent(int index, int event, HttpServletRequest request);

	JSONObject resolveExcel(String path);
}
