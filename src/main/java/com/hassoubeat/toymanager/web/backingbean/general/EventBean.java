/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.ColorTypeFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.ColorType;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 * イベント編集ページのBackingBean
 * @author hassoubeat
 */
@Named(value = "eventBean")
@ViewScoped
public class EventBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @Inject
    @Getter
    EventRoopParamConst erpConst;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    ColorTypeFacade colorTypeFacade;
    
    @NotNull
    @Size(min=1, max=50)
    @Getter
    @Setter
    private String eventName;
    
    @Size(min=0, max=200)
    @Getter
    @Setter
    private String eventContent;
    
    @NotNull
    @Getter
    @Setter
    private Date eventStartDate;
    
    @Getter
    @Setter
    private Date eventEndDate;
    
    @Getter
    @Setter
    private String eventColorCode;
    
    @Getter
    @Setter
    private int roop = 0;
    
    @Getter
    @Setter
    private Date roopEndDate;
    
    @NotNull
    @Getter
    @Setter
    private boolean isTalking = false;
    
    @NotNull
    @Getter
    @Setter
    private boolean isAccountShare = false;
    
    
    // アカウントに紐づくイベント
    @Getter
    private List<Event> accountEventList;
    
    // Toyに紐づくイベント
    @Getter
    private List<Event> toyEventList;
    
    // カラーコードの一覧
    @Getter
    private List<ColorType> colorTypeList;
    
    // ajaxロジックの成功判定処理
    @Getter
    private boolean ajaxResult; 
    
    
    /**
     * Creates a new instance of EventBean
     */
    public EventBean() {
    }
    
    /**
     * イベントの新規登録
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void addEvent() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        // ajax実行結果の初期化
        this.ajaxResult = false;
        
        
        if (this.eventEndDate != null) {
            // イベント終了日次が設定されている場合
            
            int timeDiff = this.getEventStartDate().compareTo(this.getEventEndDate());
            
            if (timeDiff < 0) {
                // イベント開始日付が終了日付よりも過去の日付の場合
                logger.warn("{}:{} USER_ID:{} {}.{}", MessageConst.DATE_CONTRADICTION.getId(), MessageConst.DATE_CONTRADICTION.getMessage(), sessionBean.getUserId(), this.getClass().getName(), this.getClass());

                FacesMessage message = new FacesMessage(MessageConst.DATE_CONTRADICTION.getMessage());
                context.addMessage("event-form:start-event-date", message);
                
                return;
            }
        }
        
        Event event = new Event();
        event.setName(this.getEventName());
        event.setContent(this.getEventContent());
        event.setStartDate(this.getEventStartDate());
        event.setEndDate(this.getEventEndDate());
        event.setColorCode(this.getEventColorCode());
        event.setRoop(this.getRoop());
        event.setRoopEndDate(this.getRoopEndDate());
        event.setIsTalking(this.isTalking);
        
        if (isAccountShare) {
            // アカウント共有がONの場合
            Account account = accountFacade.find(sessionBean.getId());
            event.setAccountId(account);
        }
        // TODO EventテーブルToyIDをNOT NULLにしない
        
        
        
        
        
        
        
//        FacesContext context = FacesContext.getCurrentInstance();
//        FacesMessage message = new FacesMessage("うんち");
//        context.addMessage("file-form:upload-file", message);
        // 開始日付と終了日付のチェック
        
        // イベントの登録
        // diffイベントの登録
        
        // 元の画面へ戻る(jQuery側のコールバック処理で成功と失敗を判別するためのパラメータを返却する)
        
        
    }
    
    /**
     * イベントの編集
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void editEvent() {
        // 
    }
    
    /**
     * イベントの削除
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void removeEvent() {
        
    }
    
    /**
     * イベントの選択
     * JSFのJavaScriptから呼び出される処理
     * 選択したイベントの情報をパラメータにセットする
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void selectEvent() {
        
        // ajax実行結果の初期化
        this.ajaxResult = false;
        
        String eventId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventId");
        if (eventFacade.countById(Integer.parseInt(eventId)) > 0) {
            Event targetEvent = eventFacade.find(Integer.parseInt(eventId));
            
            // 合致するイベントが存在していた時
            this.setEventName(targetEvent.getName());
            this.setEventContent(targetEvent.getContent());
            this.setEventStartDate(targetEvent.getStartDate());
            this.setEventEndDate(targetEvent.getEndDate());
            this.setEventColorCode(targetEvent.getColorCode());
            this.setRoop(targetEvent.getRoop());
            this.setRoopEndDate(targetEvent.getRoopEndDate());
            this.setTalking(targetEvent.getIsTalking());
            this.setAccountShare(targetEvent.getAccountId() != null);
            
            logger.warn("{}:{} USER_ID:{}, SELECT_EVENT_ID:{} {}.{}", MessageConst.SELECT_EVENT.getId(), MessageConst.SELECT_EVENT.getMessage(), sessionBean.getUserId(), eventId, this.getClass().getName(), this.getClass());
            
            // ajax処理結果にfaldse(成功)を設定する
            this.ajaxResult = true;
        } else {
            // 対象のイベントが存在しない場合
            logger.warn("{}:{} USER_ID:{}, SELECT_EVENT_ID:{} {}.{}", MessageConst.ALREADY_REMOVE_EVENT.getId(), MessageConst.ALREADY_REMOVE_EVENT.getMessage(), sessionBean.getUserId(), eventId, this.getClass().getName(), this.getClass());
        }
    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable() {
        if(sessionBean.getSelectedToyId() > 0) {
            // Toyが選択済であった場合
            
            // アカウントに紐づくイベントの取得
            accountEventList = eventFacade.findByAccountId(accountFacade.find(sessionBean.getId()));
            // Toyに紐づくイベントの取得
            toyEventList = eventFacade.findByToyId(toyFacade.find(sessionBean.getSelectedToyId()));
            // カラーコードの一覧を取得
            colorTypeList = colorTypeFacade.findAll();
        }
        
    }
    
}
