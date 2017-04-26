/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.FacetEventFacade;
import com.hassoubeat.toymanager.service.dao.FacetFacade;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacetFacade;
import com.hassoubeat.toymanager.service.dao.ToyVoiceTypeFacade;
import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.service.entity.FacetEvent;
import com.hassoubeat.toymanager.service.entity.ToyFacet;
import com.hassoubeat.toymanager.service.entity.ToyVoiceType;
import com.hassoubeat.toymanager.service.logic.FacetEventLogic;
import com.hassoubeat.toymanager.service.logic.ToyFacetLogic;
import com.hassoubeat.toymanager.service.logic.ToyWebApiAccessFilterLogic;
import com.hassoubeat.toymanager.util.ToastMessage;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "toyCustomBean")
@ViewScoped
public class ToyCustomBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    FacetFacade facetFacade;
    
    @EJB
    ToyFacetFacade toyFacetFacade;
    
    @EJB
    FacetEventFacade facetEventFacade;
    
    @EJB
    ToyVoiceTypeFacade toyVoiceTypeFacade;
    
    @EJB
    ToyWebApiAccessFilterLogic toyWebApiAccessFilterLogic;
    
    @EJB
    ToyFacetLogic toyFacetLogic;
    
    @EJB
    FacetEventLogic facetEventLogic;
    
    @Getter
    @Setter
    private Toy targetToy;
    
    @Getter
    @Setter
    private List<Facet> facetList;
    
    @Getter
    @Setter
    private List<ToyVoiceType> toyVoiceTypeList;
    
    // ファセットアップデート時にイベントを全てファセットから登録しなおすかを判断する
    @Getter
    @Setter
    private Boolean isEventResetFacetUpdate = false;

    /**
     * Creates a new instance of ToyMenuBean
     */
    
    public ToyCustomBean() {
    }
    
    /**
     * 選択中のToyからToyFacetの削除
     * @param toyFacetId 削除するToyFacetID
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String removeToyFacet(Integer toyFacetId) {
        ToyFacet targetToyFacet = toyFacetFacade.find(toyFacetId);
        ToyFacet removeToyFacat = toyFacetFacade.remove(targetToyFacet);
        logger.info("{}.{} USER_ID:{} TOY_ID:{} REMOVE_FACET_ID:{} REMOVE_TOY_FACET_ID:{}", MessageConst.SUCCESS_TOY_FACET_REMOVE.getId(), MessageConst.SUCCESS_TOY_FACET_REMOVE.getMessage(), sessionBean.getId(), removeToyFacat.getToyId().getId(),removeToyFacat.getId(), removeToyFacat.getFacetId().getId());
        // メッセージの生成/自画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_TOY_FACET_REMOVE.getMessage());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "custom?faces-redirect=true";
    }
    
    /**
     * 選択中のToyに紐付いているToyFacetのアップデート
     * @param toyFacetId アップデートするToyFacetID
     * @return 遷移先
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String updateToyFacet(Integer toyFacetId) {
        if (isEventResetFacetUpdate) {
            // ToyFacetに紐づいたイベントをリセットする場合
            toyFacetLogic.eventResetUpdate(toyFacetId);
        } else {
            // ToyFacetに紐づいたイベントをリセットしない場合
            toyFacetLogic.update(toyFacetId);
        }
        // メッセージの生成/自画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_TOY_FACET_UPDATE.getMessage());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "custom?faces-redirect=true";
    }
    
    /**
     * 選択中のToyにファセットの追加
     * @param facetId
     * @return 
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String addToyFacet(Integer facetId) {
        toyFacetLogic.create(facetId);
        // メッセージの生成/自画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_TOY_FACET_CREATE.getMessage());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "custom?faces-redirect=true";
    }
    
    /**
     * ボイスタイプの変更
     * @return 
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public String editToyVoiceType() {
        ToyVoiceType toyVoiceType = toyVoiceTypeFacade.find(targetToy.getToyVoiceTypeId().getId());
        targetToy.setToyVoiceTypeId(toyVoiceType);
        Toy editToy = toyFacade.edit(targetToy);
        logger.info("{}.{} USER_ID:{} EDIT_TOY_ID:{}", MessageConst.SUCCESS_TOY_VOICE_TYPE_EDIT.getId(), MessageConst.SUCCESS_TOY_VOICE_TYPE_EDIT.getMessage(), sessionBean.getId(), editToy.getId());
        
        // メッセージの生成/自画面にリダイレクトで遷移する
        ToastMessage toastMessage = new ToastMessage();
        toastMessage.setRender(true);
        toastMessage.setHeading(MessageConst.SUCCESS_TOY_VOICE_TYPE_EDIT.getMessage());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("", new FacesMessage(toastMessage.genList()));
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "custom?faces-redirect=true";
    }
    
    /**
     * 渡されたFacetIDが既にToyに登録済であるかをチェックする
     * @param facetId 検索対象とするファセットID
     * @return 登録済みの場合 True
     */
    public boolean isFacetRegisted(Integer facetId) {
        for (ToyFacet toyFacet : targetToy.getToyFacetList()) {
            if (Objects.equals(toyFacet.getFacetId().getId(), facetId)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 渡されたFacetEventを論理的な文字列に表現して返却する
     * @param facetEventId
     * @return 
     */
    public String getLogicalEventInfo(Integer facetEventId) {
        FacetEvent targetFacetEvent = facetEventFacade.find(facetEventId);
        return facetEventLogic.genFacetEventInfo(targetFacetEvent);
    }
    
    @LogInterceptor
    private void fetchToy() {
        targetToy = toyFacade.find(sessionBean.getSelectedToyId());
    }
    
    @LogInterceptor
    private void fetchFacetList() {
        facetList = facetFacade.findAllByReleasedFacet();
    }
    
    @LogInterceptor
    private void fetchToyVoiceTypeList() {
        toyVoiceTypeList = toyVoiceTypeFacade.findAll();
    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable(){
        if (sessionBean.getSelectedToyId() != 0) {
            // Toyが選択済の場合
            fetchToy();
            fetchFacetList();
            fetchToyVoiceTypeList();
        }
    }
    
}
