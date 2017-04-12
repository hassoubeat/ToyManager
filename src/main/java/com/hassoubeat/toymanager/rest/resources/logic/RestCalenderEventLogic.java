/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.logic;

import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.rest.resources.entity.RestCalenderEvent;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.exception.ToyManagerException;
import com.hassoubeat.toymanager.util.BitLogic;
import com.hassoubeat.toymanager.util.UtilLogic;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;



/**
 * RestCalenderEventに関するLogicクラス
 * @author hassoubeat
 */

@Stateless
public class RestCalenderEventLogic extends AbstractRestLogic {
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    RestEventLogic restEventLogic;
    
    @Inject
    EventRoopParamConst erpConst;
    
    @EJB
    BitLogic bitLogic;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    UtilLogic utilLogic;
    
    public RestCalenderEventLogic() {
    }
    
    /**
     * カレンダーイベントを取得する
     * TODO 引数に開始時間と終了時間を渡して指定した範囲のカレンダーイベントを取得する
     * @param startDate イベント取得開始範囲
     * @param endDate　イベント取得終了範囲
     * @return 
     */
    public List<RestCalenderEvent> fetchCalenderEvent (Date startDate, Date endDate) {
        List<RestCalenderEvent> responseList = new ArrayList();
        Toy targetToy = toyFacade.find(sessionBean.getSelectedToyId());
        // Toyのイベント
        responseList.addAll(fetchToyEvent(targetToy, startDate, endDate));
        // アカウントのイベント
        responseList.addAll(fetchAccountEvent(targetToy.getAccountId(), startDate, endDate));
        // TODO ファセットのイベント
        
        return responseList;
    }
    
    private List<RestCalenderEvent> fetchToyEvent(Toy targetToy, Date startDate, Date endDate) {
        List<Event> convertList = new ArrayList();
        List<RestCalenderEvent> responseList = new ArrayList();
        
        convertList.addAll(eventFacade.findStandardEventByToyId(targetToy, startDate, endDate));
        convertList.addAll(eventFacade.findRoopEventByToyId(targetToy, startDate));
        for(Event toyEvent : convertList) {
            if (!bitLogic.bitCheck(toyEvent.getRoop(), erpConst.IS_ROOP) || toyEvent.getRoopEndDate() == null) {
                // ループなしの場合(ループ終了日時が設定されていない時も同様
                responseList.add(convertEvent(toyEvent));
            } else {
                if (bitLogic.bitCheck(toyEvent.getRoop(), erpConst.IS_EVERY_DAY_ROOP)) {
                    // 日次ループの場合
                    responseList.addAll(convertRoopEvent(toyEvent, erpConst.IS_EVERY_DAY_ROOP , startDate, endDate));
                }
                if (bitLogic.bitCheck(toyEvent.getRoop(), erpConst.IS_EVERY_WEEK_ROOP)) {
                    // 週次ループの場合
                    responseList.addAll(convertWeekRoopAllEvent(toyEvent, startDate, endDate));
                }
                if (bitLogic.bitCheck(toyEvent.getRoop(), erpConst.IS_EVERY_MOUTH_ROOP)) {
                    // 月次ループの場合
                    responseList.addAll(convertRoopEvent(toyEvent, erpConst.IS_EVERY_MOUTH_ROOP , startDate, endDate));
                    // TODO 曜日基準の場合を表示する
                }
                if (bitLogic.bitCheck(toyEvent.getRoop(), erpConst.IS_EVERY_YEAR_ROOP)) {
                    // 年次ループの場合
                    responseList.addAll(convertRoopEvent(toyEvent, erpConst.IS_EVERY_YEAR_ROOP, startDate, endDate));
                }
            }
        }
        
//        for (RestCalenderEvent item: responseList) {
//            logger.debug(item.toString());
//        }
        return responseList;
    }
    
    private List<RestCalenderEvent> fetchAccountEvent(Account targetAccount, Date startDate, Date endDate) {
        List<Event> convertList = new ArrayList();
        List<RestCalenderEvent> responseList = new ArrayList();
        convertList.addAll(eventFacade.findStandardEventByAccountId(targetAccount, startDate, endDate));
        convertList.addAll(eventFacade.findRoopEventByAccountId(targetAccount, startDate));
        for(Event accountEvent : convertList) {
            if (!bitLogic.bitCheck(accountEvent.getRoop(), erpConst.IS_ROOP) || accountEvent.getRoopEndDate() == null) {
                // ループなしの場合(ループ終了日時が設定されていない時も同様
                responseList.add(convertEvent(accountEvent));
            } else {
                if (bitLogic.bitCheck(accountEvent.getRoop(), erpConst.IS_EVERY_DAY_ROOP)) {
                    // 日次ループの場合
                    responseList.addAll(convertRoopEvent(accountEvent, erpConst.IS_EVERY_DAY_ROOP, startDate, endDate));
                }
                if (bitLogic.bitCheck(accountEvent.getRoop(), erpConst.IS_EVERY_WEEK_ROOP)) {
                    // 週次ループの場合
                    responseList.addAll(convertWeekRoopAllEvent(accountEvent, startDate, endDate));
                }
                if (bitLogic.bitCheck(accountEvent.getRoop(), erpConst.IS_EVERY_MOUTH_ROOP)) {
                    // 月次ループの場合
                    responseList.addAll(convertRoopEvent(accountEvent, erpConst.IS_EVERY_MOUTH_ROOP, startDate, endDate));
                    // TODO 曜日基準の場合を表示する
                }
                if (bitLogic.bitCheck(accountEvent.getRoop(), erpConst.IS_EVERY_YEAR_ROOP)) {
                    // 年次ループの場合
                    responseList.addAll(convertRoopEvent(accountEvent, erpConst.IS_EVERY_YEAR_ROOP, startDate, endDate));
                }
            }
        }
        
//        for (RestCalenderEvent item: responseList) {
//            logger.debug(item.toString());
//        }
        return responseList;
    }
    
    /**
     * イベントをカレンダーイベントに変換して返却する
     * @return 
     */
    private RestCalenderEvent convertEvent(Event targetEvent) {
        // イベントをREST用エンティティに詰め替える
        RestCalenderEvent rcEvent = new RestCalenderEvent();
        rcEvent.setId(targetEvent.getId());
        rcEvent.setTitle(targetEvent.getName());
        rcEvent.setStart(targetEvent.getStartDate());
        rcEvent.setEnd(targetEvent.getEndDate());
        rcEvent.setColor(targetEvent.getColorCode());
        rcEvent.setClassName(generateClassList(targetEvent));
        return rcEvent;
    }
    
    /**
     * 繰り返しイベントをカレンダーイベントに変換して返却する
     * @return 
     */
    private List<RestCalenderEvent> convertRoopEvent(Event targetEvent, int roopType, Date startDate, Date endDate) {
        // イベントをREST用エンティティに詰め替える
        List<RestCalenderEvent> responseList = new ArrayList(); 

        Date nextStartDate = targetEvent.getStartDate();
        Date nextEndDate = targetEvent.getEndDate(); 
        int roopInterval = bitLogic.bitAnd(targetEvent.getRoop(), bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));
        
        int startIndex = 1;
        // TODO ロジックをなんとかするのだ。。。取得したい範囲の日付になるまで日付加算を繰り返すっていう頭のおかしいことをしてる
        while (nextStartDate.compareTo(startDate) <= 0) {
            if (roopType == erpConst.IS_EVERY_DAY_ROOP) {
                // 日次ループ
                nextStartDate = utilLogic.calDate(nextStartDate, roopInterval, Calendar.DAY_OF_MONTH);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(nextEndDate, roopInterval, Calendar.DAY_OF_MONTH);
                }
            }
            if (roopType == erpConst.IS_EVERY_WEEK_ROOP) {
                // 週次ループ
                nextStartDate = utilLogic.calDate(nextStartDate, roopInterval,  Calendar.WEEK_OF_MONTH);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(nextEndDate, roopInterval,  Calendar.WEEK_OF_MONTH);
                }
            }
            if (roopType == erpConst.IS_EVERY_MOUTH_ROOP) {
                // 月次ループ
                // 31日で渡すと加算しているうちにベースがズレてく問題
                nextStartDate = utilLogic.calDate(targetEvent.getStartDate(), roopInterval * startIndex, Calendar.MONTH);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(targetEvent.getEndDate(), roopInterval * startIndex, Calendar.MONTH);
                }
                startIndex++;
            }
            if (roopType == erpConst.IS_EVERY_YEAR_ROOP) {
                // 年次ループ
                nextStartDate = utilLogic.calDate(nextStartDate, roopInterval, Calendar.YEAR);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(nextEndDate, roopInterval, Calendar.YEAR);
                }
            }
        }
        
        int endIndex = 1;
        while (nextStartDate.compareTo(endDate) <= 0) {
            // クローンで実装する
            Event roopEvent = new Event();
            try {
                roopEvent = (Event)targetEvent.clone();
            } catch (CloneNotSupportedException ex) {
                // TODO 
                throw new ToyManagerException("何故か複製できなかった", ex);
            }
            roopEvent.setStartDate(nextStartDate);
            roopEvent.setEndDate(nextEndDate);
            
            responseList.add(convertEvent(roopEvent));
            
            if (roopType == erpConst.IS_EVERY_DAY_ROOP) {
                // 日次ループ
                nextStartDate = utilLogic.calDate(nextStartDate, roopInterval, Calendar.DAY_OF_MONTH);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(nextEndDate, roopInterval, Calendar.DAY_OF_MONTH);
                }
            }
            if (roopType == erpConst.IS_EVERY_WEEK_ROOP) {
                // 週次ループ
                nextStartDate = utilLogic.calDate(nextStartDate, roopInterval,  Calendar.WEEK_OF_MONTH);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(nextEndDate, roopInterval,  Calendar.WEEK_OF_MONTH);
                }
            }
            if (roopType == erpConst.IS_EVERY_MOUTH_ROOP) {
                // 月次ループ
                // 31日で渡すと加算しているうちにベースがズレてく問題
                nextStartDate = utilLogic.calDate(targetEvent.getStartDate(), roopInterval * endIndex, Calendar.MONTH);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(targetEvent.getEndDate(), roopInterval * endIndex, Calendar.MONTH);
                }
                endIndex++;
            }
            if (roopType == erpConst.IS_EVERY_YEAR_ROOP) {
                // 年次ループ
                nextStartDate = utilLogic.calDate(nextStartDate, roopInterval, Calendar.YEAR);
                if (targetEvent.getEndDate() != null) {
                    nextEndDate = utilLogic.calDate(nextEndDate, roopInterval, Calendar.YEAR);
                }
            }
            
        }
        return responseList;
    }
    
    /**
     * 週次ループイベントをカレンダーイベントに変換して返却する
     */
    private List<RestCalenderEvent> convertWeekRoopAllEvent(Event targetEvent, Date startDate, Date endDate) {
        // イベントをREST用エンティティに詰め替える
        List<RestCalenderEvent> responseList = new ArrayList(); 
        
        // 曜日指定の確認
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_SUNDAY)) {
            // 日曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.SUNDAY, startDate, endDate));
        }
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_MONDAY)) {
            // 月曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.MONDAY, startDate, endDate));
        }
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_TUESDAY)) {
            // 火曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.TUESDAY, startDate, endDate));
        }
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_WEDNESDAY)) {
            // 水曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.WEDNESDAY, startDate, endDate));
        }
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_THURSDAY)) {
            // 木曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.THURSDAY, startDate, endDate));
        }
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_FRIDAY)) {
            // 金曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.FRIDAY, startDate, endDate));
        }
        if (bitLogic.bitCheck(targetEvent.getRoop(), erpConst.IS_ROOP_SATURDAY)) {
            // 土曜日
            responseList.addAll(convertWeekRoopEvent(targetEvent, Calendar.SATURDAY, startDate, endDate));
        }
        
        return responseList;
    }
    
    /**
     * 週次ループイベントの指定した曜日のイベントカレンダーを生成する
     */
    private List<RestCalenderEvent> convertWeekRoopEvent(Event targetEvent, int dayOfTheWeek, Date startDate, Date endDate) {
        // イベント開始日時の曜日を取得する
        int eventStartDoW = utilLogic.getDayOfWeek(targetEvent.getStartDate());
        int eventEndDoW = 0;
        if (targetEvent.getEndDate() != null) {
           eventEndDoW = utilLogic.getDayOfWeek(targetEvent.getStartDate());
        }
         
        Event roopEvent = new Event();
        try {
            roopEvent = (Event)targetEvent.clone();
        } catch (CloneNotSupportedException ex) {
            // TODO 
            throw new ToyManagerException("何故か複製できなかった", ex);
        }
        roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), dayOfTheWeek - eventStartDoW, Calendar.DAY_OF_MONTH));
        if (roopEvent.getEndDate()!= null) {
            roopEvent.setEndDate(utilLogic.calDate(roopEvent.getEndDate(), dayOfTheWeek - eventEndDoW, Calendar.DAY_OF_MONTH));
        }
        
        
//        switch (dayOfTheWeek) {
//            case Calendar.SUNDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.SUNDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.SUNDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//            case Calendar.MONDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.MONDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.MONDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//            case Calendar.TUESDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.TUESDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.TUESDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//            case Calendar.WEDNESDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.WEDNESDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.WEDNESDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//            case Calendar.THURSDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.THURSDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.THURSDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//            case Calendar.FRIDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.FRIDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.FRIDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//            case Calendar.SATURDAY:
//                roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.SATURDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                if (roopEvent.getRoopEndDate()!= null) {
//                    roopEvent.setStartDate(utilLogic.calDate(roopEvent.getStartDate(), Calendar.SATURDAY - eventStartDoW, Calendar.DAY_OF_MONTH));
//                }
//                break;
//        }
        return convertRoopEvent(roopEvent, erpConst.IS_EVERY_WEEK_ROOP, startDate, endDate);
    }
    
    
    /**
     * イベント属性を取得する
     */
    private List<String> generateClassList(Event targetEvent) {
        List<String> eventType = new ArrayList();
        if (targetEvent.getAccountId() != null) {
            eventType.add("account-share");
        }
        if (targetEvent.getToyFacetId() != null) {
            eventType.add("facet");
        }
        if (targetEvent.getRoop() > 0) {
            eventType.add("roop");
        }
        if (targetEvent.getIsTalking()) {
            eventType.add("is-toy-talk");
        }
        if (targetEvent.getIsDeleted()) {
            eventType.add("is-event-deleted");
        }
        
        return eventType;
    }
}
