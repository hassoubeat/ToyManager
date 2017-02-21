/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.constant;

import java.util.ResourceBundle;

/**
 * パスの定数を定義するクラス
 * 
 * @author hassoubeat
 */
public class PathConst {
    // リソースフォルダのURL(プロパティファイルから取得)
    public static final String FILE_UPLOAD_URL = ResourceBundle.getBundle("PathConfig").getString("resources.url");
    // リソースフォルダの絶対パス(プロパティファイルから取得)
    public static final String FILE_UPLOAD_ABSOLUTE_PATH = ResourceBundle.getBundle("PathConfig").getString("resources.absolute.path");  
}
