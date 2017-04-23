/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.management;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.hassoubeat.toymanager.annotation.AuthManagerInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.constant.PropertyConst;
import com.hassoubeat.toymanager.service.dao.FacetEventFacade;
import com.hassoubeat.toymanager.service.dao.FacetFacade;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.service.entity.FacetEvent;
import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import com.hassoubeat.toymanager.service.logic.EventLogic;
import com.hassoubeat.toymanager.util.S3Logic;
import com.hassoubeat.toymanager.util.ToastMessage;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "facetManagementEditBean")
@ViewScoped
public class FacetManagementEditBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    @EJB
    FacetFacade facetFacade;
    @EJB
    FacetEventFacade facetEventFacade;
    @EJB
    EventLogic eventLogic;
    @EJB
    S3Logic s3Logic;
    
    @Getter
    @Setter
    private Integer id;
    
    @Getter
    @Setter
    Facet facet = new Facet();
    
    @Getter
    @Setter
    FacetEvent facetEvent = new FacetEvent();
    
    @Getter
    @Setter
    Part facetProgram;
    
    @Getter
    @Setter
    Part facetProperties;
    
    @Getter
    @Setter
    Part facetPropertiesEditView;
    
    @Getter
    @Setter
    String logicalEventInfo = "";

    /**
     * Creates a new instance of TopBean
     */
    public FacetManagementEditBean() {
    }
    
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public void bookmarkable() {
        if (id == null) {
            // ファセットIDを受け取れなかった場合、不正な画面遷移として処理する
            throw new InvalidScreenTransitionException();
        }
        facet = facetFacade.find(id);
    }
    
    /**
     * ファセットを変更する処理
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public String edit() {
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
                String fpUploadUri = s3Logic.upload(facetProgram, PropertyConst.S3_FACET_LIB_PATH , CannedAccessControlList.PublicRead);
                facet.setProgramPath(fpUploadUri);
            } catch (AmazonS3Exception ex) {
                // ファセットのアップロードに失敗した場合、
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-program-upload", new FacesMessage(MessageConst.S3_FILE_UPLOAD_FAILED.getMessage()));
                // 元の画面に戻る
                return "";
            }
            
        } 
        if (facetProperties != null) {
            if (StringUtils.isEmpty(facet.getProgramPath())) {
                // ファセットプログラムがアップロードされていなかった場合
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-properties-upload", new FacesMessage(MessageConst.UN_REGISTED_FACET_PROGRAM.getMessage()));
                
                logger.info("{}.{} USER_ID:{}", MessageConst.UN_REGISTED_FACET_PROGRAM.getId(), MessageConst.UN_REGISTED_FACET_PROGRAM.getMessage(), sessionBean.getUserId());
                
                // 元の画面に戻る
                return "";
            }
            
            if (!facet.getName().equals(FilenameUtils.getBaseName(facetProperties.getSubmittedFileName()))) {
                // ファセット名とファセットプログラムの名前が不一致だった場合
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-properties-upload", new FacesMessage(MessageConst.MISMATCH_FACET_NAME.getMessage()));
                
                logger.info("{}.{} USER_ID:{}", MessageConst.MISMATCH_FACET_NAME.getId(), MessageConst.MISMATCH_FACET_NAME.getMessage(), sessionBean.getUserId());
                
                // 元の画面に戻る
                return "";
            }
            // ファセットプロパティをS3にアップロードする
            try {
                String fpUploadUri = s3Logic.upload(facetProperties, PropertyConst.S3_FACET_PROPERTIES_PATH , CannedAccessControlList.PublicRead);
                facet.setPropertiesPath(fpUploadUri);
            } catch (AmazonS3Exception ex) {
                // ファセットのアップロードに失敗した場合、
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-properties-upload", new FacesMessage(MessageConst.S3_FILE_UPLOAD_FAILED.getMessage()));
                // 元の画面に戻る
                return "";
            }
            
        } 
        
        if (facetPropertiesEditView != null) {
            if (StringUtils.isEmpty(facet.getProgramPath())) {
                // ファセットプログラムがアップロードされていなかった場合
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-properties-edit-view-upload", new FacesMessage(MessageConst.UN_REGISTED_FACET_PROGRAM.getMessage()));
                
                logger.info("{}.{} USER_ID:{}", MessageConst.UN_REGISTED_FACET_PROGRAM.getId(), MessageConst.UN_REGISTED_FACET_PROGRAM.getMessage(), sessionBean.getUserId());
                
                // 元の画面に戻る
                return "";
            }
            
            if (!facet.getName().equals(FilenameUtils.getBaseName(facetPropertiesEditView.getSubmittedFileName()))) {
                // ファセット名とファセットプログラムの名前が不一致だった場合
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-properties-edit-view-upload", new FacesMessage(MessageConst.MISMATCH_FACET_NAME.getMessage()));
                
                logger.info("{}.{} USER_ID:{}", MessageConst.MISMATCH_FACET_NAME.getId(), MessageConst.MISMATCH_FACET_NAME.getMessage(), sessionBean.getUserId());
                
                // 元の画面に戻る
                return "";
            }
            // ファセットプロパティ変更画面をS3にアップロードする
            try {
                String fpUploadUri = s3Logic.upload(facetPropertiesEditView, PropertyConst.S3_FACET_PROPERTIES_EDIT_VIEW_PATH, CannedAccessControlList.PublicRead);
                facet.setPropertiesEditViewPath(fpUploadUri);
            } catch (AmazonS3Exception ex) {
                // ファセットのアップロードに失敗した場合、
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("facet-form:facet-properties-edit-view-upload", new FacesMessage(MessageConst.S3_FILE_UPLOAD_FAILED.getMessage()));
                // 元の画面に戻る
                return "";
            }
        }
        
        // 更新処理(EJB)
        Facet editedFacet = facetFacade.edit(facet);
        logger.info("{}.{} USER_ID:{}, FACET_ID:{}", MessageConst.SUCCESS_FACET_EDIT.getId(), MessageConst.SUCCESS_FACET_EDIT.getMessage(), sessionBean.getUserId(), editedFacet.getId());
        
        // 自画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_FACET_EDIT.getMessage());
        toastMessage.setText("ファセットプログラム・ファセットイベントはファセットバージョンを更新しないと、クライアント側に更新の通知が行われません。");
        toastMessage.setHideAfter(15000);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "edit?faces-redirect=true&id=" + editedFacet.getId();
    }
    
    /**
     * ファセットを削除する
     * @return 遷移先
     */
    public String remove() {
        List<KeyVersion> removeS3KeyList = new ArrayList<>();
        if (facet.getProgramPath() != null) {
            // ファセットプログラムが存在する場合は、S3のオブジェクトを削除する
            String facetProgramS3Key = PropertyConst.S3_FACET_LIB_PATH + FilenameUtils.getName(facet.getProgramPath());
            removeS3KeyList.add(new KeyVersion(facetProgramS3Key));
        }
        if (facet.getPropertiesPath() != null) {
            // ファセットプロパティが存在する場合は、S3のオブジェクトを削除する
            String facetPropertiesS3Key = PropertyConst.S3_FACET_PROPERTIES_PATH + FilenameUtils.getName(facet.getPropertiesPath());
            removeS3KeyList.add(new KeyVersion(facetPropertiesS3Key));
        }
        if (facet.getPropertiesEditViewPath() != null) {
            // ファセットプロパティ編集画面が存在する場合は、S3のオブジェクトを削除する
            String facetPropertiesEditViewS3Key = PropertyConst.S3_FACET_PROPERTIES_EDIT_VIEW_PATH + FilenameUtils.getName(facet.getPropertiesEditViewPath());
            removeS3KeyList.add(new KeyVersion(facetPropertiesEditViewS3Key));
        }
        if(removeS3KeyList.size() > 0) {
            s3Logic.remove(removeS3KeyList);
        }
        Facet removeFacet = facetFacade.remove(facet);
        logger.info("{}.{} USER_ID:{}, FACET_ID:{}", MessageConst.SUCCESS_FACET_REMOVE.getId(), MessageConst.SUCCESS_FACET_REMOVE.getMessage(), sessionBean.getUserId(), removeFacet.getId());
        
        // ファセット一覧画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_FACET_REMOVE.getMessage());
        toastMessage.setHideAfter(15000);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "index?faces-redirect=true";
    }
    
    /**
     * ファセットプログラムを削除する
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public String removeFacetProgram() {
        List<KeyVersion> removeS3KeyList = new ArrayList<>();
        if (facet.getProgramPath() != null) {
            // ファセットプログラムが存在する場合は、S3のオブジェクトを削除する
            String facetProgramS3Key = PropertyConst.S3_FACET_LIB_PATH + FilenameUtils.getName(facet.getProgramPath());
            removeS3KeyList.add(new KeyVersion(facetProgramS3Key));
        }
        if (facet.getPropertiesPath() != null) {
            // ファセットプロパティが存在する場合は、S3のオブジェクトを削除する
            String facetPropertiesS3Key = PropertyConst.S3_FACET_PROPERTIES_PATH + FilenameUtils.getName(facet.getPropertiesPath());
            removeS3KeyList.add(new KeyVersion(facetPropertiesS3Key));
        }
        if (facet.getPropertiesEditViewPath() != null) {
            // ファセットプロパティ編集画面が存在する場合は、S3のオブジェクトを削除する
            String facetPropertiesEditViewS3Key = PropertyConst.S3_FACET_PROPERTIES_EDIT_VIEW_PATH + FilenameUtils.getName(facet.getPropertiesEditViewPath());
            removeS3KeyList.add(new KeyVersion(facetPropertiesEditViewS3Key));
        }
        if(removeS3KeyList.size() > 0) {
            s3Logic.remove(removeS3KeyList);
        }
        
        // エンティティのファセットパス関連をNULLに指定してアップデートする
        facet.setProgramPath(null);
        facet.setPropertiesPath(null);
        facet.setPropertiesEditViewPath(null);
        return edit();
    }
    
    /**
     * ファセットプロパティを削除する
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public String removeFacetProperties() {
        // S3のオブジェクトを削除する
        String facetPropertiesS3Key = PropertyConst.S3_FACET_PROPERTIES_PATH + FilenameUtils.getName(facet.getPropertiesPath());
        s3Logic.remove(facetPropertiesS3Key);
        
        // エンティティのファセットプロパティパスをNULLに指定してアップデートする
        facet.setPropertiesPath(null);
        return edit();
    }
    
    /**
     * ファセットプロパティ編集画面を削除する
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public String removeFacetPropertiesEditView() {
        // S3のオブジェクトを削除する
        String facetPropertiesEditViewS3Key = PropertyConst.S3_FACET_PROPERTIES_EDIT_VIEW_PATH + FilenameUtils.getName(facet.getPropertiesEditViewPath());
        s3Logic.remove(facetPropertiesEditViewS3Key);
        
        // エンティティのファセットプロパティ編集画面パスをNULLに指定してアップデートする
        facet.setPropertiesEditViewPath(null);
        return edit();
    }
    
    /**
     * ファセットイベントの選択
     * @param facetEventId 取得するファセットイベントのID
     */
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public void facetEventSelect(int facetEventId) {
        facetEvent = facetEventFacade.find(facetEventId);
    }
    
    /**
     * ファセットイベントの追加
     * @return 遷移先
     */
    public String addFacetEvent() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (facetEvent.getEndDate() != null) {
            // イベント終了日次が設定されている場合
            
            int timeDiff = facetEvent.getStartDate().compareTo(facetEvent.getEndDate());
            
            if (timeDiff > 0) {
                // イベント開始日付が終了日付よりも過去の日付の場合
                logger.warn("{}:{} USER_ID:{} {}.{}", MessageConst.DATE_CONTRADICTION.getId(), MessageConst.DATE_CONTRADICTION.getMessage(), sessionBean.getUserId(), this.getClass().getName(), this.getClass());

                FacesMessage message = new FacesMessage(MessageConst.DATE_CONTRADICTION.getMessage());
                context.addMessage("facet-event-form:start-event-date", message);
                return "";
            }
        }
        if (facetEvent.getRoop() == null) {
            facetEvent.setRoop(0);
        }
        // 外部キーの設定
        facetEvent.setFacetId(facet);
        
        // 登録処理の実行
        FacetEvent createFacetEvent = facetEventFacade.create(facetEvent);
        
        logger.info("{}.{} USER_ID:{} FACET_EVENT_ID", MessageConst.SUCCESS_FACET_EVENT_CREATE.getId(), MessageConst.SUCCESS_FACET_EVENT_CREATE.getMessage(), sessionBean.getId(), createFacetEvent.getId());
        
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_FACET_EVENT_CREATE.getMessage());
        toastMessage.setText("ファセットプログラム・ファセットイベントはファセットバージョンを更新しないと、クライアント側に更新の通知が行われません。");
        toastMessage.setHideAfter(15000);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        context.getExternalContext().getFlash().setKeepMessages(true);
        return "edit?faces-redirect=true&id=" + facet.getId();
    }
    
    /**
     * ファセットイベントの変更
     * @return 遷移先
     */
    public String editFacetEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (facetEvent.getEndDate() != null) {
            // イベント終了日次が設定されている場合
            
            int timeDiff = facetEvent.getStartDate().compareTo(facetEvent.getEndDate());
            
            if (timeDiff > 0) {
                // イベント開始日付が終了日付よりも過去の日付の場合
                logger.warn("{}:{} USER_ID:{} {}.{}", MessageConst.DATE_CONTRADICTION.getId(), MessageConst.DATE_CONTRADICTION.getMessage(), sessionBean.getUserId(), this.getClass().getName(), this.getClass());

                FacesMessage message = new FacesMessage(MessageConst.DATE_CONTRADICTION.getMessage());
                context.addMessage("facet-event-form:start-event-date", message);
                return "";
            }
        }
        // 変更処理の実行
        FacetEvent editFacetEvent = facetEventFacade.edit(facetEvent);

        logger.info("{}.{} USER_ID:{} FACET_EVENT_ID", MessageConst.SUCCESS_FACET_EVENT_EDIT.getId(), MessageConst.SUCCESS_FACET_EVENT_EDIT.getMessage(), sessionBean.getId(), editFacetEvent.getId());

        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_FACET_EVENT_EDIT.getMessage());
        toastMessage.setText("ファセットプログラム・ファセットイベントはファセットバージョンを更新しないと、クライアント側に更新の通知が行われません。");
        toastMessage.setHideAfter(15000);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        context.getExternalContext().getFlash().setKeepMessages(true);
        return "edit?faces-redirect=true&id=" + facet.getId();
    }
    
    /**
     * ファセットイベントの削除
     * @return 遷移先
     */
    public String removeFacetEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // 削除処理の実行
        FacetEvent removeFacetEvent = facetEventFacade.remove(facetEvent);

        logger.info("{}.{} USER_ID:{} FACET_EVENT_ID", MessageConst.SUCCESS_FACET_EVENT_REMOVE.getId(), MessageConst.SUCCESS_FACET_EVENT_REMOVE.getMessage(), sessionBean.getId(), removeFacetEvent.getId());

        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_FACET_EVENT_REMOVE.getMessage());
        toastMessage.setText("ファセットプログラム・ファセットイベントはファセットバージョンを更新しないと、クライアント側に更新の通知が行われません。");
        toastMessage.setHideAfter(15000);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        context.getExternalContext().getFlash().setKeepMessages(true);
        return "edit?faces-redirect=true&id=" + facet.getId();
    }
    
    
    /**
     * ajaxでループイベントの論理的な情報を取得する
     */
    public void genLogicalEventInfo() {
        Event event = new Event();
        
        String startDateStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("startDate");
        String roopParamStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roopParam");
        String roopEndDateStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roopEndDate");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date startDate = new Date();
        int roopParam;
        Date roopEndDate = new Date();
        
        try {
            startDate = sdf.parse(startDateStr);
        } catch (ParseException ex) {
            return;
        }
        
        try {
            roopParam = Integer.parseInt(roopParamStr);
        } catch (NumberFormatException ex) {
            roopParam = 0;
        }
        
        try {
            roopEndDate = sdf.parse(roopEndDateStr);
        } catch (ParseException ex) {
            roopEndDate = null;
        }
        event.setStartDate(startDate);
        event.setRoop(roopParam);
        event.setRoopEndDate(roopEndDate);
        
        logicalEventInfo = eventLogic.genEventInfo(event);
    }
    
} 
