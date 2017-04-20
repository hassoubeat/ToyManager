/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.management;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.hassoubeat.toymanager.annotation.AuthManagerInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.constant.PropertyConst;
import com.hassoubeat.toymanager.service.dao.FacetFacade;
import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.util.S3Logic;
import com.hassoubeat.toymanager.util.ToastMessage;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "facetManagementCreateBean")
@ViewScoped
public class FacetManagementCreateBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    @EJB
    FacetFacade facetFacade;
    @EJB
    S3Logic s3Logic;
    
    @Getter
    @Setter
    Facet facet = new Facet();
    
    @Getter
    @Setter
    Part facetProgram;

    /**
     * Creates a new instance of TopBean
     */
    public FacetManagementCreateBean() {
    }
    
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public void bookmarkable() {
    }
    
    /**
     * ファセットを新規登録する処理
     * @return 
     */
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public String create(){
        
        if (facetFacade.isFacetNameOverrap(facet.getName())) {
            // ファセット名が既に登録されていた場合
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("facet-form:facet-name", new FacesMessage(MessageConst.ALREADY_USED_FACET_NAME.getMessage()));

            logger.info("{}.{} USER_ID:{}, FACET_NAME:{}", MessageConst.ALREADY_USED_FACET_NAME.getId(), MessageConst.ALREADY_USED_FACET_NAME.getMessage(), sessionBean.getUserId(), facet.getName());

            // 元の画面に戻る
            return "";
        }
        
        if (facetProgram != null) {
            if (!facet.getName().equals(FilenameUtils.getBaseName(facetProgram.getSubmittedFileName()))) {
                // ファセット名とファセットプログラムの名前が不一致だった場合
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-program-upload", new FacesMessage(MessageConst.MISMATCH_FACET_NAME.getMessage()));
                
                logger.info("{}.{} USER_ID:{}", MessageConst.MISMATCH_FACET_NAME.getId(), MessageConst.MISMATCH_FACET_NAME.getMessage(), sessionBean.getUserId());
                
                // 元の画面に戻る
                return "";
            }
            // ファセットプログラムをS3にアップロードする
            try {
                String fpUploadUri = s3Logic.upload(facetProgram, PropertyConst.S3_FACET_LIB_PATH, CannedAccessControlList.PublicRead);
                facet.setProgramPath(fpUploadUri);
            } catch (AmazonS3Exception ex) {
                // ファセットのアップロードに失敗した場合、
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-program-upload", new FacesMessage(MessageConst.S3_FILE_UPLOAD_FAILED.getMessage()));
                // 元の画面に戻る
                return "";
            }
            
        } 
        // 登録処理(EJB)
        Facet createFacet = facetFacade.create(facet);
        logger.info("{}.{} USER_ID:{}, FACET_ID:{}", MessageConst.SUCCESS_FACET_CREATE.getId(), MessageConst.SUCCESS_FACET_CREATE.getMessage(), sessionBean.getUserId(), createFacet.getId());
        
        // ファセット編集画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_FACET_CREATE.getMessage());
        toastMessage.setText("ファセットはまだ公開されていません。公開する場合は【公開状態】にチェックを入れて変更ボタンを押下してください。");
        toastMessage.setHideAfter(15000);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "edit?faces-redirect=true&id=" + createFacet.getId();
        
    }
}
