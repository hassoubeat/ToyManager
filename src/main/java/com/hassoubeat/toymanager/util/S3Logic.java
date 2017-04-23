/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.DeleteObjectsResult.DeletedObject;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.constant.PropertyConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.IOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.Part;
import org.slf4j.Logger;

/**
 * AmazonS3にアクセスするためのロジック
 * @author hassoubeat
 */
@Stateless
public class S3Logic {
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    final private String S3_ACCESS_KEY = PropertyConst.S3_ACCESS_KEY;
    final private String S3_SECRET_KEY = PropertyConst.S3_SECRET_KEY;
    final private String S3_SERVICE_END_POINT = PropertyConst.S3_SERVICE_END_POINT;
    final private String S3_REGION = PropertyConst.S3_REGION;
    final private String S3_BUCKET_NAME = PropertyConst.S3_BUCKET_NAME;
    
    /**
     * ファイルのアップロードを行う
     * @param uploadFile アップロード対象のファイル
     * @param uploadPath S3バケット内のファイルのアップロード先(例：testフォルダへファイルをアップロードする場合 /test/ )
     * @param fileAuthority アップロードするファイルのアクセス権限
     * @return アップロードしたファイルのURI
     */
    public String upload(Part uploadFile, String uploadPath, CannedAccessControlList fileAuthority) throws AmazonClientException{
        AmazonS3 client = getS3Client();
        String fileUploadPath = uploadPath + uploadFile.getSubmittedFileName();
        try {
            // アップロード対象のオブジェクトを作成  
            final PutObjectRequest por = new PutObjectRequest(S3_BUCKET_NAME, fileUploadPath, uploadFile.getInputStream(), new ObjectMetadata());  
            // アップロード対象ファイルの権限を設定する  
            por.setCannedAcl(fileAuthority);  
            // アップロード  
            PutObjectResult result = client.putObject(por);
            logger.info("{}.{} USER_ID:{}, UPLOAD_OBJECT_KEY:{}", MessageConst.SUCCESS_S3_OBJECT_UPLOAD.getId(), MessageConst.SUCCESS_S3_OBJECT_UPLOAD.getMessage(), sessionBean.getId(), fileUploadPath);
        } catch(IOException | AmazonClientException ex) {  
            // ロガー出力
            logger.warn("{}.{} USER_ID" , MessageConst.S3_FILE_UPLOAD_FAILED.getId(), MessageConst.S3_FILE_UPLOAD_FAILED.getMessage(), sessionBean.getId());
            throw new AmazonS3Exception(MessageConst.S3_FILE_UPLOAD_FAILED.getMessage(), ex);
        }
        // アップロードしたURIを返却する
        return getFileUploadUri(fileUploadPath);   
    }
    
    /**
     * ファイルの削除を行う
     * @param removeKey 削除対象のキー
     */
    public void remove(String removeKey) {
        AmazonS3 client = getS3Client();
        
        DeleteObjectsRequest request = new DeleteObjectsRequest(S3_BUCKET_NAME).withKeys(removeKey);
        DeleteObjectsResult  result  = client.deleteObjects(request);
        
        for (DeletedObject object:result.getDeletedObjects()) {
            logger.info("{}.{} USER_ID:{} REMOVE_OBJECT_KEY:{}", MessageConst.SUCCESS_S3_OBJECT_REMOVE.getId(), MessageConst.SUCCESS_S3_OBJECT_REMOVE.getMessage(), sessionBean.getId(), object.getKey());
        }
    }
    
    /**
     * ファイルの削除を行う(複数同時削除)
     * @param removeKeys 削除対象のキー
     */
    public void remove(List<KeyVersion> removeKeys) {
        AmazonS3 client = getS3Client();
        
        DeleteObjectsRequest request = new DeleteObjectsRequest(S3_BUCKET_NAME).withKeys(removeKeys);
        DeleteObjectsResult  result  = client.deleteObjects(request);
        
        for (DeletedObject object:result.getDeletedObjects()) {
            logger.info("{}.{} USER_ID:{} REMOVE_OBJECT_KEY:{}", MessageConst.SUCCESS_S3_OBJECT_REMOVE.getId(), MessageConst.SUCCESS_S3_OBJECT_REMOVE.getMessage(), sessionBean.getId(), object.getKey());
        }
    }
    
    /**
     * AWSS3のS3クライアントを取得する
     * @return 
     */
    private AmazonS3 getS3Client() {
        // AWSの認証情報
        AWSCredentials credentials = new BasicAWSCredentials(S3_ACCESS_KEY, S3_SECRET_KEY);

        // クライアント設定
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);  // プロトコル
        clientConfig.setConnectionTimeout(15000);   // 接続タイムアウト(ms) 

        // エンドポイント設定
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(S3_SERVICE_END_POINT, S3_REGION);
        
        // S3アクセスクライアントの生成
        AmazonS3 client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withClientConfiguration(clientConfig)
                        .withEndpointConfiguration(endpointConfiguration).build();
        
        return client;
    }
    
    /**
     * アップロードしたファイルのURIを取得する。
     * @param uploadFileName
     * @return 生成したファイルアップロードURI
     */
    private String getFileUploadUri(String uploadFileName) {
        // アップロードファイル
        String uploadUri = S3_SERVICE_END_POINT + "/" + S3_BUCKET_NAME + "/" + uploadFileName;
        return uploadUri;
    }
    
        

}
