package com.lenovo.smarttraffic.entityclass;

import com.lenovo.smarttraffic.InitApp;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *作用与2019国赛题用户添加收藏功能
 * 功能添加用户收藏数据和删除用户收藏数据
 * @author asus
 */
public class UserCollection {
    private JSONArray jsonArray = new JSONArray();
    public static UserCollection instance;

    public static  UserCollection getInstance(){
        if (instance == null){
            instance = new UserCollection();
            getCollection();
        }
        return instance;
    }

    private static void getCollection() {
        try {
            FileInputStream in = InitApp.getInstance().openFileInput("Collection");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
