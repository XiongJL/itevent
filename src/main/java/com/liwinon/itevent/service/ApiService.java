package com.liwinon.itevent.service;

import net.sf.json.JSONObject;

import java.util.List;

public interface ApiService {
    JSONObject getNameDepart(String userid);
    List<String> getTypes();
    List<String> getBrands(String type);
    String getUnit(String type);
}
