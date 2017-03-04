/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;


import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 * Cronコマンドを生成するロジック
 * @author hassoubeat
 */
@ApplicationScoped
public class CommandLogic {

    @Inject
    Logger logger;
    
    @Inject
    BitLogic bitLogic;
    
    @Inject
    EventRoopParamConst erpConst;
    
    /**
     * Creates a new instance of MailLogic
     */
    public CommandLogic() {
    }
    
    /**
     * 引数からCronコマンドを生成する
     * 
     * @param eventStartDate
     * @param roopParam
     * @return 生成したCronコマンド
     */
    public String genCronCommand(Date eventStartDate, int roopParam) {
        
        String cronCommand = "";
        
        // 年月日時分秒を取得するためにイベントの開始日時をLocalDateTime型に変換する
        LocalDateTime ldtEventStartDate = LocalDateTime.ofInstant(eventStartDate.toInstant(), ZoneId.systemDefault());
        
        // "分"の生成
        int cronMinute = ldtEventStartDate.getMinute();
        
        // "時"の生成
        int cronHour = ldtEventStartDate.getHour();
        
        // "日"の生成
        int cronDay = ldtEventStartDate.getDayOfMonth();
        if (bitLogic.bitCheck(roopParam, erpConst.IS_EVERY_DAY_ROOP)) {
            // 日次ループの場合
            if (cronDay > 1) {
                // 
            }
            // 日次ループの場合
//            cronDay += "*";
        }
        
        return cronCommand;
    }
    
    /**
     * 引数からQuartz向けスケジュールを生成する
     * (QuartzのCron書式は秒からの定義)
     * 
     * @param eventStartDate
     * @param roopParam
     * @return 生成したCronコマンド
     */
    public String genCronCommandForQuartz(Date eventStartDate, int roopParam) {
        
        String cronCommand = "";
        
//        if 
//        
//        
        return cronCommand;
    }
        
}
