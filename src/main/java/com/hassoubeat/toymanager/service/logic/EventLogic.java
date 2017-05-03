/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.DiffSyncEventFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.RoleFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.util.GMailLogic;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.util.BitLogic;
import com.hassoubeat.toymanager.util.UtilLogic;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class EventLogic{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @Inject
    GMailLogic mailLogic; 
    
    @Inject
    EventRoopParamConst erpConst;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    DiffSyncEventFacade diffSyncEventFacade;
    
    @EJB
    RoleFacade roleFacade;
    
    @EJB
    BitLogic bitLogic;
    
    @EJB
    UtilLogic utilLogic;
    
    private final int DAY_ROOP_END_DEFAULT = 1;
    private final int WEEK_ROOP_END_DEFAULT = 3;
    private final int MOUTH_ROOP_END_DEFAULT = 6;
    private final int YEAR_ROOP_END_DEFAULT = 36;

    public EventLogic() {
    }
    
    /**
     * イベントの新規登録処理を実行する
     * 
     * @param event 登録するイベントエンティティ
     */
    public void create(Event event) {        
        
        // 日付間隔/曜日設定のチェック(指定なしの時に値を初期化する)
        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP) ) {
            // ループが有効な場合、ループ値のチェックを行う
            event = this.eventCheck(event);
        }
        
        // イベントの登録
        Event createEvent = eventFacade.create(event);
        
        // TODO 差分イベントの登録
//        DiffSyncEvent diffEvent = new DiffSyncEvent();
//        diffEvent.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
//        diffEvent.setEventId(createEvent);
//        diffEvent.setMethodType(MethodTypeConst.ADD.toString());
//        DiffSyncEvent createDiffEvent = diffSyncEventFacade.create(diffEvent);
        
        // TODO アカウント共有差分イベントの登録
        logger.info("{} : USER_ID:{}, EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_CREATE.getId() + ":" + MessageConst.SUCCESS_EVENT_CREATE.getMessage(), sessionBean.getUserId(), createEvent.getId(), this.getClass().getName() + "." + this.getClass());
//        logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_CREATE.getId() + ":" + MessageConst.SUCCESS_EVENT_CREATE.getMessage(), sessionBean.getUserId(), createEvent.getId(), createDiffEvent.getId(),this.getClass().getName() + "." + this.getClass());
        
    }
    
    /**
     * イベントの変更処理を実行する
     * 
     * @param event 変更するイベントエンティティ
     */
    public void edit(Event event) {        
        // 日付間隔/曜日設定のチェック(指定なしの時に値を初期化する)
        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP) ) {
            // ループが有効な場合、ループ値のチェックを行う
            event = this.eventCheck(event);
        }
        
        // イベントの変更
        Event editEvent = eventFacade.edit(event);
        
        
        // TODO 差分イベント登録処理
        
        // 既に今回変更したイベントIDのカラムがあった場合は、変更カウントで数える
        // TODO 変更前通常イベント→変更後アカウント共有イベント
        // TODO 変更前アカウント共有イベント→変更後通常イベント
        // TODO countByEventIdメソッドではアカウント共有イベントを特定できないため、合わせてToyIDも利用して検索する処理を設定する
//        int count = diffSyncEventFacade.countByEventId(editEvent);
//        DiffSyncEvent editDiffEvent = new DiffSyncEvent();
//        if (count > 0) {
//            // Diffイベントがあった場合(新しく作成したEventがまだToy側に同期されていなかった場合)
//            editDiffEvent = diffSyncEventFacade.findByEventId(event).get(0);
//        } else {
//            // Diffイベントがなかった(既にToy側に同期されていた場合)
//            // 差分イベントの登録
//            DiffSyncEvent diffEvent = new DiffSyncEvent();
//            diffEvent.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
//            diffEvent.setEventId(editEvent);
//            diffEvent.setMethodType(MethodTypeConst.UPDATE.toString());
//            editDiffEvent  = diffSyncEventFacade.create(diffEvent);
//        }
        
        logger.info("{} : USER_ID:{}, EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_EDIT.getId() + ":" + MessageConst.SUCCESS_EVENT_EDIT.getMessage(), sessionBean.getUserId(), editEvent.getId(), this.getClass().getName() + "." + this.getClass());
//        logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_EDIT.getId() + ":" + MessageConst.SUCCESS_EVENT_EDIT.getMessage(), sessionBean.getUserId(), editEvent.getId(), editDiffEvent.getId(),this.getClass().getName() + "." + this.getClass());
    }
    
    /**
     * イベントの削除処理(場合によっては論理削除)を行う
     * 
     * @param event 削除するイベントエンティティ
     * @return 
     */
    public MessageConst remove(Event event) {
        
        // 既に今回論理削除したイベントIDのカラムが存在するか確認する
        // TODO 差分イベント処理
//        int count = diffSyncEventFacade.countByEventId(event);
//        if (count > 0) {
//            // Diffイベントがあった場合(新しく作成したEventがまだToy側に同期されていなかった場合)
//            
//            // イベントの削除(一緒に差分イベントも消える)
//            Event removeEvent = eventFacade.remove(event);            
//        
//            logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_REMOVE.getId() + ":" + MessageConst.SUCCESS_EVENT_REMOVE.getMessage(), sessionBean.getUserId(), removeEvent.getId(), removeEvent.getDiffSyncEventList().get(0).getId(),this.getClass().getName() + "." + this.getClass());
//            
//            return MessageConst.SUCCESS_EVENT_REMOVE;
//            
//        } else {
//            // Diffイベントがなかった(既にToy側に同期されていた場合)
//            
//            // イベントの論理削除(Toy側の削除の完了を受けた際に完全な削除を実施する)
//            event.setIsDeleted(true);
//            Event logicRemoveEvent = eventFacade.edit(event);
//            
//            // 差分イベントの登録
//            DiffSyncEvent diffEvent = new DiffSyncEvent();
//            diffEvent.setToyId(toyFacade.find(sessionBean.getSelectedToyId()));
//            diffEvent.setEventId(logicRemoveEvent);
//            diffEvent.setMethodType(MethodTypeConst.REMOVE.toString());
//            DiffSyncEvent removeDiffEvent = diffSyncEventFacade.create(diffEvent);
//            
//            // TODO アカウント共有イベントの削除パターンを追加
//            // TODO アカウントに紐づくToyの数分だけ差分イベントを登録する。
//            
//            logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_LOGIC_REMOVE.getId() + ":" + MessageConst.SUCCESS_EVENT_LOGIC_REMOVE.getMessage(), sessionBean.getUserId(), logicRemoveEvent.getId(), removeDiffEvent.getId(),this.getClass().getName() + "." + this.getClass());
//            
//            return MessageConst.SUCCESS_EVENT_LOGIC_REMOVE;
//        }
        Event removeEvent = eventFacade.remove(event);            
        logger.info("{} : USER_ID:{}, EVENT_ID:{}, {}.{}", MessageConst.SUCCESS_EVENT_REMOVE.getId() + ":" + MessageConst.SUCCESS_EVENT_REMOVE.getMessage(), sessionBean.getUserId(), removeEvent.getId(),this.getClass().getName() + "." + this.getClass());
        // TODO Diff
//        logger.info("{} : USER_ID:{}, EVENT_ID:{}, DIFF_SYNC_EVENT_ID:{} {}.{}", MessageConst.SUCCESS_EVENT_REMOVE.getId() + ":" + MessageConst.SUCCESS_EVENT_REMOVE.getMessage(), sessionBean.getUserId(), removeEvent.getId(), removeEvent.getDiffSyncEventList().get(0).getId(),this.getClass().getName() + "." + this.getClass());
        return MessageConst.SUCCESS_EVENT_REMOVE;
        
    }
    
    /**
     * イベントエンティティの登録前のチェック
     * ・日付間隔の値が指定されていなかった場合、"1"で初期化する
     * ・週次ループで曜日が指定されていなかった場合、現在日付の曜日で初期化する
     * ・ループの終了日時が指定されてなかった場合、開始日から一定の日付を進めたデフォルトの終了日時を設定する
     * 
     * @param event 登録前のイベントエンティティ
     * @return 
     */
    public Event eventCheck(Event event) {
        // 日付間隔/曜日設定のチェック(指定なしの時に値を初期化する)  
        
        int roop = event.getRoop();
        LocalDateTime startDate = LocalDateTime.ofInstant(event.getStartDate().toInstant(), ZoneId.systemDefault());
            
        // ループ間隔値の取得
        int roopInterval = bitLogic.bitAnd(roop, bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));

        if (roopInterval == 0) {
            // ループ間隔値が指定されていなかった場合に初期値1と指定する
            roop += 1;
        }
        
        if(bitLogic.bitCheck(roop, erpConst.IS_EVERY_WEEK_ROOP)) {
            if(!bitLogic.dayOfTheWeekCheck(roop)) {
            // 週次ループでループ曜日設定が指定されていなかった場合にイベント開始日のビットを立てる
            
                switch(startDate.getDayOfWeek().getValue()) {
                    case 7:
                        // 日曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_SUNDAY);
                        break;
                    case 1:
                        // 月曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_MONDAY);
                        break;
                    case 2:
                        // 火曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_TUESDAY);
                        break;
                    case 3:
                        // 水曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_WEDNESDAY);
                        break;
                    case 4:
                        // 木曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_THURSDAY);
                        break;
                    case 5:
                        // 金曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_FRIDAY);
                        break;
                    case 6:
                        // 土曜日の場合
                        roop = bitLogic.bitOr(roop, erpConst.IS_ROOP_SATURDAY);
                        break;
                }
            }
        }
        
        event.setRoop(roop);

// TODO ループ終了日時のデフォルトを指定するかは要検討
//        if (event.getRoopEndDate() == null) {
//            if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_DAY_ROOP)) {
//                event.setRoopEndDate(utilLogic.calDate(event.getStartDate(), DAY_ROOP_END_DEFAULT, Calendar.MONTH));
//            }
//            if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_WEEK_ROOP)) {
//                event.setRoopEndDate(utilLogic.calDate(event.getStartDate(), WEEK_ROOP_END_DEFAULT, Calendar.MONTH));
//            }
//            if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_MOUTH_ROOP)) {
//                event.setRoopEndDate(utilLogic.calDate(event.getStartDate(), MOUTH_ROOP_END_DEFAULT, Calendar.MONTH));
//            }
//            if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_YEAR_ROOP)) {
//                event.setRoopEndDate(utilLogic.calDate(event.getStartDate(), YEAR_ROOP_END_DEFAULT, Calendar.MONTH));
//            }
//        }
        
        return event;
    }
    
    /**
     * 受け取ったEventエンティティからどういったイベントであるかを論理的な文字列で返却するメソッド
     * @param Eventエンティティ
     * @return 
     */
    public String genEventInfo(Event event) {
        final LocalDateTime EVENT_START_DATE = LocalDateTime.ofInstant(event.getStartDate().toInstant(), ZoneId.systemDefault());
        final int ROOP_PARAM = event.getRoop();
        LocalDateTime ROOP_END_DATE = null;
        if (event.getRoopEndDate() != null) {
            ROOP_END_DATE = LocalDateTime.ofInstant(event.getRoopEndDate().toInstant(), ZoneId.systemDefault());
        }
        // ループ間隔値の取得
        final int ROOP_INTERVAL = bitLogic.bitAnd(ROOP_PARAM, bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));
        
        String eventInfo = "";
        
        if (!bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP)) {
            // イベントの繰り返しが無効の場合
            eventInfo += EVENT_START_DATE.getYear() + "年" + EVENT_START_DATE.getMonthValue() + "月" + EVENT_START_DATE.getDayOfMonth() + "日" + EVENT_START_DATE.getHour() + "時" + EVENT_START_DATE.getMinute() + "分に一度だけ実行されます。";
            
        } 
        
        // 以下、イベントの繰り返しが有効の場合
        
        if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_EVERY_DAY_ROOP)) {
            // 日時繰り返しの場合
            eventInfo += ROOP_INTERVAL + "日間隔で" + EVENT_START_DATE.getHour()  + "時" + EVENT_START_DATE.getMinute() + "分に実行されます。";
        }

        if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_EVERY_WEEK_ROOP)) {
            // 週次繰り返しの場合
            eventInfo += ROOP_INTERVAL + "週間隔で";
            
            // 曜日指定の確認
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_SUNDAY)) {
                // 日曜日が指定されていた場合
                eventInfo += "日";
            }
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_MONDAY)) {
                // 月曜日が指定されていた場合
                eventInfo += "月";
            }
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_TUESDAY)) {
                // 火曜日が指定されていた場合
                eventInfo += "火";
            }
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_WEDNESDAY)) {
                // 水曜日が指定されていた場合
                eventInfo += "水";
            }
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_THURSDAY)) {
                // 木曜日が指定されていた場合
                eventInfo += "木";
            }
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_FRIDAY)) {
                // 金曜日が指定されていた場合
                eventInfo += "金";
            }
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_SATURDAY)) {
                // 土曜日が指定されていた場合
                eventInfo += "土";
            }
            
            eventInfo += "曜日の" + EVENT_START_DATE.getHour()  + "時" + EVENT_START_DATE.getMinute() + "分に実行されます。";
        }
        
        if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_EVERY_MOUTH_ROOP)) {
            // 月次繰り返しの場合
            
            eventInfo += ROOP_INTERVAL + "月間隔で";
            
            if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_ROOP_STANDARD_DAY)) {
                // 日付基準の繰り返しの場合(毎月 N 日)
                eventInfo += EVENT_START_DATE.getHour()  + "時" + EVENT_START_DATE.getMinute() + "分に実行されます。";
            } else {
                // TODO 曜日基準の繰り返しの場合(毎月 第 N 曜日)
                
            }
        }
        
        if (bitLogic.bitCheck(ROOP_PARAM, erpConst.IS_EVERY_YEAR_ROOP)) {
            // 年次繰り返しの場合
            eventInfo += eventInfo += ROOP_INTERVAL + "年間隔で" + EVENT_START_DATE.getMonthValue() + "月" + EVENT_START_DATE.getDayOfMonth() + "日" + EVENT_START_DATE.getHour() + "時" + EVENT_START_DATE.getMinute();
        }
        
        if (ROOP_END_DATE != null) {
            // ループの終わりが設定されていた場合
            eventInfo += "(" + ROOP_END_DATE.getYear() + "年" + ROOP_END_DATE.getMonthValue() + "月" + ROOP_END_DATE.getDayOfMonth() + "日" + ROOP_END_DATE.getHour() + "時" + ROOP_END_DATE.getMinute() + "分まで本イベントは継続的に行われます。)";
        }
        
        return eventInfo;
    }
    
}
