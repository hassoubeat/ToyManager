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
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacetFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import com.hassoubeat.toymanager.service.logic.EventLogic;
import com.hassoubeat.toymanager.util.BitLogic;
import com.hassoubeat.toymanager.util.QuartzLogic;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
//@RequestScoped
public class EventBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @Inject
    QuartzLogic quartzLogic;
    
    @Inject
    @Getter
    EventRoopParamConst erpConst;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    ToyFacetFacade toyFacetFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    EventLogic eventLogic;
    
    @EJB
    BitLogic bitLogic;
    
    @Getter
    @Setter
    private int eventId;
    
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
    
    @Getter
    @Setter
    private Integer priority;
    
    @NotNull
    @Getter
    @Setter
    private boolean isTalking = false;
    
    @NotNull
    @Getter
    @Setter
    private boolean isAccountShare = false;
    
    @Getter
    @Setter
    private Integer toyFacetId;
    
    // ajaxロジックの成功判定処理
    @Getter
    private boolean isAjaxProcessResult = false; 
    
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
        
        if (this.eventEndDate != null) {
            // イベント終了日次が設定されている場合
            
            int timeDiff = this.getEventStartDate().compareTo(this.getEventEndDate());
            
            if (timeDiff > 0) {
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
        if (this.getPriority() != null) {
            event.setPriority(this.getPriority());
        }
        event.setIsTalking(this.isTalking);
        
        if (toyFacetId == null) {
            // ファセットイベントではない場合
            
            if (isAccountShare) {
            // アカウント共有がONの場合
            Account account = accountFacade.find(sessionBean.getId());
            event.setAccountId(account);
            } else {
                // アカウント共有がOFFの場合(Toyに紐づくイベントの場合)
                event.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
            }
        } else {
            // ファセットイベントの場合
            event.setToyFacetId(toyFacetFacade.find(toyFacetId));
        }
        
        // 登録処理の実行
        eventLogic.create(event);
        
        // 元の画面へ戻る(jQuery側のコールバック処理で成功と失敗を判別するためのパラメータを返却する)
        this.isAjaxProcessResult = true;
        
        FacesMessage message = new FacesMessage(MessageConst.SUCCESS_EVENT_CREATE.getMessage());
        context.addMessage("add-event-success", message);
        
    }
    
    /**
     * イベントの編集
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void editEvent() {
        
        if (this.getEventId() == 0) {
            // イベントIDが渡ってこない時(開発者コンソール上から、無理やり変更ボタンを表示する場合など)
            throw new InvalidScreenTransitionException();
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (this.eventEndDate != null) {
            // イベント終了日次が設定されている場合
            
            int timeDiff = this.getEventStartDate().compareTo(this.getEventEndDate());
            
            if (timeDiff > 0) {
                // イベント開始日付が終了日付よりも過去の日付の場合
                logger.warn("{}:{} USER_ID:{} {}.{}", MessageConst.DATE_CONTRADICTION.getId(), MessageConst.DATE_CONTRADICTION.getMessage(), sessionBean.getUserId(), this.getClass().getName(), this.getClass());

                FacesMessage message = new FacesMessage(MessageConst.DATE_CONTRADICTION.getMessage());
                context.addMessage("event-form:start-event-date", message);
                
                return;
            }
        }
        
        if (eventFacade.countById(this.getEventId()) > 0) {
            Event event = eventFacade.find(this.getEventId());
            
            // 合致するイベントが存在していた時
            event.setName(this.getEventName());
            event.setContent(this.getEventContent());
            event.setStartDate(this.getEventStartDate());
            event.setEndDate(this.getEventEndDate());
            event.setColorCode(this.getEventColorCode());
            event.setRoop(this.getRoop());
            event.setRoopEndDate(this.getRoopEndDate());
            if (this.getPriority() != null) {
               event.setPriority(this.getPriority());
            }
            event.setIsTalking(this.isTalking);
            
            if (toyFacetId == null) {
                // ファセットイベントではない場合
            
                if (isAccountShare) {
                    // アカウント共有がONの場合
                    Account account = accountFacade.find(sessionBean.getId());
                    event.setAccountId(account);
                } else {
                    // アカウント共有がOFFの場合(Toyに紐づくイベントの場合)
                    event.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
                }
            } else {
                // ファセットイベントの場合
                event.setToyFacetId(toyFacetFacade.find(toyFacetId));
            }
            
            // 変更処理の実行
            eventLogic.edit(event);
            
            FacesMessage message = new FacesMessage(MessageConst.SUCCESS_EVENT_EDIT.getMessage());
            context.addMessage("edit-event-success", message);
            
            
        } else {
            // 対象のイベントが存在しない場合
            logger.warn("{}:{} USER_ID:{}, SELECT_EVENT_ID:{} {}.{}", MessageConst.ALREADY_REMOVE_EVENT.getId(), MessageConst.ALREADY_REMOVE_EVENT.getMessage(), sessionBean.getUserId(), this.getEventId(), this.getClass().getName(), this.getClass());
        }
    }
    
    /**
     * イベントの削除
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void removeEvent() {
        
        if (this.getEventId() == 0) {
            // イベントIDが渡ってこない時(開発者コンソール上から、無理やり削除ボタンを表示して押下した場合など)
            throw new InvalidScreenTransitionException();
        }
        
        if (eventFacade.countById(this.getEventId()) > 0) {
            Event event = eventFacade.find(this.getEventId());
            
            
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage();
            
            // 削除処理の実行
            switch(eventLogic.remove(event)) {
                case SUCCESS_EVENT_REMOVE:
                    // 削除処理を実行した場合
                    message = new FacesMessage(MessageConst.SUCCESS_EVENT_REMOVE.getMessage());
                    context.addMessage("remove-event-success", message);
                    break;
                case SUCCESS_EVENT_LOGIC_REMOVE:
                    // 論理削除処理を実行した場合
                    message = new FacesMessage(MessageConst.SUCCESS_EVENT_LOGIC_REMOVE.getMessage());
                    context.addMessage("logic-remove-event-success", message);
                    break;
            }
            
        } else {
            // 対象のイベントが存在しない場合
            logger.warn("{}:{} USER_ID:{}, SELECT_EVENT_ID:{} {}.{}", MessageConst.ALREADY_REMOVE_EVENT.getId(), MessageConst.ALREADY_REMOVE_EVENT.getMessage(), sessionBean.getUserId(), this.getEventId(), this.getClass().getName(), this.getClass());
        }
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
        
        String selectedEventId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventId");
        if (eventFacade.countById(Integer.parseInt(selectedEventId)) > 0) {
            Event targetEvent = eventFacade.find(Integer.parseInt(selectedEventId));
            
            // 合致するイベントが存在していた時
            this.setEventId(targetEvent.getId());
            this.setEventName(targetEvent.getName());
            this.setEventContent(targetEvent.getContent());
            this.setEventStartDate(targetEvent.getStartDate());
            this.setEventEndDate(targetEvent.getEndDate());
            this.setEventColorCode(targetEvent.getColorCode());
            this.setRoop(targetEvent.getRoop());
            this.setRoopEndDate(targetEvent.getRoopEndDate());
            this.setPriority(targetEvent.getPriority());
            this.setTalking(targetEvent.getIsTalking());
            this.setAccountShare(targetEvent.getAccountId() != null);
            if (targetEvent.getToyFacetId() != null) {
                // ファセットイベントの時のみ、パラメーターを保存する
                this.setToyFacetId(targetEvent.getToyFacetId().getId());
            } else {
                // ファセットイベントでは無いはnullをセットする
                this.setToyFacetId(null);
            }
            
            
            logger.warn("{}:{} USER_ID:{}, SELECT_EVENT_ID:{} {}.{}", MessageConst.SELECT_EVENT.getId(), MessageConst.SELECT_EVENT.getMessage(), sessionBean.getUserId(), selectedEventId, this.getClass().getName(), this.getClass());
            
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(MessageConst.SELECT_EVENT.getMessage());
            context.addMessage("select-event-success", message);
            
        } else {
            // 対象のイベントが存在しない場合
            logger.warn("{}:{} USER_ID:{}, SELECT_EVENT_ID:{} {}.{}", MessageConst.ALREADY_REMOVE_EVENT.getId(), MessageConst.ALREADY_REMOVE_EVENT.getMessage(), sessionBean.getUserId(), selectedEventId, this.getClass().getName(), this.getClass());
        }
    }
    
//    /**
//     * 現在EventBeanに保持されているイベント情報をクリアする
//     */
//    public void selectEventClear() {
//        eventId = 0;
//        eventName = "";
//        eventContent = "";
//        eventStartDate = null;
//        eventEndDate = null;
//        eventColorCode = "";
//        roop = 0;   
//        roopEndDate = null;
//        isTalking = false;
//        isAccountShare = false;
//        toyFacetId = null;
//    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable() {
        if(sessionBean.getSelectedToyId() > 0) {
            // Toyが選択済であった場合
            
            // アカウントに紐づくイベントの取得
//            accountEventList = eventFacade.findByAccountId(accountFacade.find(sessionBean.getId()));
            // Toyに紐づくイベントの取得
//            toyEventList = eventFacade.findByToyId(toyFacade.find(sessionBean.getSelectedToyId()));
            // カラーコードの一覧を取得
//            colorTypeList = colorTypeFacade.findAll();
        }
        
    }
    
}

//        // 日付間隔/曜日設定のチェック(指定なしの時に値を初期化する)
//        if (bitLogic.bitCheck(this.roop, erpConst.IS_ROOP) ) {
//            // ループ指定が存在した場合
//            
//            // ループ間隔値の取得
//            int roopInterval = bitLogic.bitAnd(this.roop, bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));
//            
//            if (roopInterval == 0) {
//                // ループ間隔値が指定されていなかった場合に初期値1と指定する
//                this.roop += 1;
//            }
//            
//            if(!bitLogic.dayOfTheWeekCheck(this.roop)) {
//                // ループ曜日設定が指定されていなかった場合に今日の曜日のビットを立てる
//                LocalDateTime now = LocalDateTime.now();
//                switch(now.getDayOfWeek().getValue()) {
//                    case 7:
//                        // 日曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_SUNDAY);
//                        break;
//                    case 1:
//                        // 月曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_MONDAY);
//                        break;
//                    case 2:
//                        // 火曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_TUESDAY);
//                        break;
//                    case 3:
//                        // 水曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_WEDNESDAY);
//                        break;
//                    case 4:
//                        // 木曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_THURSDAY);
//                        break;
//                    case 5:
//                        // 金曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_FRIDAY);
//                        break;
//                    case 6:
//                        // 土曜日の場合
//                        this.roop = bitLogic.bitOr(this.roop, erpConst.IS_ROOP_SATURDAY);
//                        break;
//                }
//            }
//        }
