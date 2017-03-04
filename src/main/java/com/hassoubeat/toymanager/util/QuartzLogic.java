/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.service.entity.Event;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.Job;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author hassoubeat
 */
@ApplicationScoped
public class QuartzLogic {
    
    @Inject
    BitLogic bitLogic;
    
    @Inject
    EventRoopParamConst erpConst;
    
    /**
     * Quartzのスケジュールを生成する
     * @param eventList
     */
    public void ScheduleGen(List<Event> eventList) {
        
        for(Event event: eventList) {
            
        }
        
    }
    
    /**
     * Quartzのジョブを生成する
     * @param eventStartDate
     * @param roopParam
     * @return 
     */
    public Job jobGen(Date eventStartDate, int roopParam) {
        
        return null;
    }
    
    /**
     * Quartzのトリガーを生成する
     * @param event
     * @return 
     */
    public Trigger triggerGen(Event event) {
        
        Trigger trigger = null;
//                newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();
        
        if (!bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP)) {
            // イベントの繰り返しが指定されていなかった場合
            trigger = (SimpleTrigger) newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).build();
            return trigger;
        }
        
        Date eventStartDate = event.getStartDate();
        LocalDateTime ldtEventStartDate = LocalDateTime.ofInstant(eventStartDate.toInstant(), ZoneId.systemDefault());
        
        String sec = Integer.toString(ldtEventStartDate.getSecond());
        String min = Integer.toString(ldtEventStartDate.getMinute());
        String hour = Integer.toString(ldtEventStartDate.getHour());
        String day = Integer.toString(ldtEventStartDate.getDayOfMonth());
        String mouth = Integer.toString(ldtEventStartDate.getMonthValue());
        String year = Integer.toString(ldtEventStartDate.getYear());
        
        
//        sec = Integer.toString(ldtEventStartDate.getSecond());
//        min = Integer.toString(ldtEventStartDate.getMinute());
//        hour = Integer.toString(ldtEventStartDate.getHour());
//        day = Integer.toString(ldtEventStartDate.getDayOfMonth());
//        mouth = Integer.toString(ldtEventStartDate.getMonthValue());
//        year = Integer.toString(ldtEventStartDate.getYear());
        
        // ループ間隔値の取得
        int roopInterval = bitLogic.bitAnd(event.getRoop(), bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));
        
        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_EVERY_DAY_ROOP)) {
            // 日次ループの場合

            if (event.getEndDate() == null) {
                // ループの終わりが設定されていなかった場合
                trigger = newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInDays(roopInterval)).build();
            } else {
                // ループの終わりが設定されていた場合
                trigger = newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).endAt(event.getEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInDays(roopInterval)).build();
            }
            
            return trigger;
        }
        
        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_EVERY_WEEK_ROOP)) {
            // 週次ループの場合
            
            String dayOfWeek = "";
            
            // 曜日指定の確認
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_SUNDAY)) {
                // 日曜日が指定されていた場合
                dayOfWeek += "SUN,";
            }
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_MONDAY)) {
                // 月曜日が指定されていた場合
                dayOfWeek += "MON,";
            }
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_TUESDAY)) {
                // 火曜日が指定されていた場合
                dayOfWeek += "TUE,";
            }
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_WEDNESDAY)) {
                // 水曜日が指定されていた場合
                dayOfWeek += "WED,";
            }
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_THURSDAY)) {
                // 木曜日が指定されていた場合
                dayOfWeek += "THU,";
            }
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_FRIDAY)) {
                // 金曜日が指定されていた場合
                dayOfWeek += "FRI,";
            }
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_SATURDAY)) {
                // 土曜日が指定されていた場合
                dayOfWeek += "SAT,";
            }
            
            if (!"".equals(dayOfWeek)) {
                // 空文字でなかった場合、カンマ(最後の一文字)の切り取りを行う
                dayOfWeek = dayOfWeek.substring(0, dayOfWeek.length() - 1 );
            } 
            
            // TODO 隔週指定の実現
            
            // CronSchedule文字列の作成
            String cronString = sec + " " + min + " " + hour + " ? * " + dayOfWeek;
            System.out.println("cronString" +  cronString);
            
            if (event.getEndDate() == null) {
                // ループの終わりが設定されていなかった場合
                trigger = newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).withSchedule(cronSchedule(cronString)).build();
            } else {
                // ループの終わりが設定されていた場合
                trigger = newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).endAt(event.getEndDate()).withSchedule(cronSchedule(cronString)).build();
            } 
            
            return trigger;
        }
        
        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_EVERY_MOUTH_ROOP)) {
            // 月次ループ
            
            String cronString = "";
            
            if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_ROOP_STANDARD_DAY)) {
                // 日付基準の月次ループの場合
                
                // CronSchedule文字列の作成
                cronString = sec + " " + min + " " + hour + " " + day + " * ?";
                System.out.println("cronString" +  cronString);
                
            } else {
                // 曜日基準の月次ループの場合
                
                // 開始日が第N週であるかを計算する
                int weekNum = Integer.parseInt(day) / 7;
                if (Integer.parseInt(day) % 7 != 0) {
                    weekNum++;
                }
                
                cronString = sec + " " + min + " " + hour + " * * " + Integer.toString(ldtEventStartDate.getDayOfWeek().getValue()) + " # " + Integer.toString(weekNum);
                System.out.println("cronString" +  cronString);
            }
            
            // TODO 隔月指定の実現
            
            if (event.getEndDate() == null) {
                // ループの終わりが設定されていなかった場合
                trigger = newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).withSchedule(cronSchedule(cronString)).build();
            } else {
                // ループの終わりが設定されていた場合
                trigger = newTrigger().withIdentity(Integer.toString(event.getId()), "group1").startAt(event.getStartDate()).endAt(event.getEndDate()).withSchedule(cronSchedule(cronString)).build();
            } 
            
            return trigger;
        }
        
//        if (bitLogic.bitCheck(event.getRoop(), erpConst.IS_EVERY_YEAR_ROOP)) {
//            // 年次ループの場合
//            
//        }
        
        return trigger;
    }
    
}
