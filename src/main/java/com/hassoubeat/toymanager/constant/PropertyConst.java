/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.constant;

import java.util.ResourceBundle;

/**
 * 外部プロパティ値を保持するクラス
 * 
 * @author hassoubeat
 */
public class PropertyConst {
    // ホスト名
    public static final String HOST_PATH = ResourceBundle.getBundle("PathConfig").getString("host.path");
    // リソースフォルダのURL(プロパティファイルから取得)
    public static final String RESOURCES_URL = ResourceBundle.getBundle("PathConfig").getString("resources.url");
    // リソースフォルダの絶対パス(プロパティファイルから取得)
    public static final String RESOURCES_ABSOLUTE_PATH = ResourceBundle.getBundle("PathConfig").getString("resources.absolute.path");  
    
    // S3アクセスキー
    public static final String S3_ACCESS_KEY = ResourceBundle.getBundle("S3Config").getString("s3.accesskey");
    // S3シークレットキー
    public static final String S3_SECRET_KEY = ResourceBundle.getBundle("S3Config").getString("s3.secretkey");
    // S3サービスエンドポイント
    public static final String S3_SERVICE_END_POINT = ResourceBundle.getBundle("S3Config").getString("s3.service.end.point");
    // S3の配置国
    public static final String S3_REGION = ResourceBundle.getBundle("S3Config").getString("s3.region");
    // S3のバケット名
    public static final String S3_BUCKET_NAME = ResourceBundle.getBundle("S3Config").getString("s3.bucket.name");
    // S3のファセットプログラムアップロードパス
    public static final String S3_FACET_LIB_PATH = ResourceBundle.getBundle("S3Config").getString("s3.facet.lib.path");
    // S3のファセットプロパティアップロードパス
    public static final String S3_FACET_PROPERTIES_PATH = ResourceBundle.getBundle("S3Config").getString("s3.facet.properties.path");
    // S3のファセットプロパティ編集画面アップロードパス
    public static final String S3_FACET_PROPERTIES_EDIT_VIEW_PATH = ResourceBundle.getBundle("S3Config").getString("s3.facet.properties.edit.view.path");
    
    
}
