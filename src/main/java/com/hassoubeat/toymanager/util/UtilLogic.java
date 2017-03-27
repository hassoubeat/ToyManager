/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;


import java.util.Calendar;
import java.util.Date;
import javax.ejb.Singleton;

/**
 * 汎用的なロジックを定義するクラス
 * @author hassoubeat
 */
@Singleton
public class UtilLogic {
        
    /**
     * Creates a new instance of MailLogic
     */
    public UtilLogic() {
    }
    
    /**
     * 引数で渡された日付から日付の計算を実施する
     * @param targetDate 計算を行うDate型
     * @param calParam 計算する値
     * @param calDateType 計算するタイプ(日/週/月/年単位で指定)
     * @return 
     */
    public Date calDate(Date targetDate, int calParam, int calDateType) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(targetDate);
        switch (calDateType) {
            case Calendar.DAY_OF_MONTH:
                // 日
                calender.add(Calendar.DAY_OF_MONTH, calParam);
                break;
            case Calendar.WEEK_OF_MONTH:
                // 週
                calender.add(Calendar.WEEK_OF_MONTH, calParam);
                break;
            case Calendar.MONTH:
                // 月
                calender.add(Calendar.MONTH, calParam);
                break;
            case Calendar.YEAR:
                // 年
                calender.add(Calendar.YEAR, calParam);
                break;
        }
        return calender.getTime();
    }
    
    /**
     * 引数で渡された日付の曜日を返却する
     * @param date 曜日を求めたい日付
     * @return 曜日(日曜日：1, 月曜日:2, 火曜日:3, 水曜日:4, 木曜日:5, 金曜日6, 土曜日:7)
     */
    public int getDayOfWeek(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        return calender.get(Calendar.DAY_OF_WEEK);     
    }
        
}
