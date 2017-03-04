/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lombok.Getter;

/**
 * ビット演算のロジック
 * @author hassoubeat
 */
@Stateless
public class BitLogic {
    
    @Inject
    @Getter
    EventRoopParamConst erpConst;
    
    /**
     * 引数として渡されたパラメータでビットAnd演算を実施する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のand演算結果
     */
    public int bitAnd(int param1, int param2) {
        return param1 & param2;
    }
    
    /**
     * 引数として渡されたパラメータでビットOr演算を実施する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のOr演算結果
     */
    public int bitOr(int param1, int param2) {
        return param1 | param2;
    }
    
    /**
     * 引数として渡されたパラメータでビットXOr演算を実施する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のXor演算結果
     */
    public int bitXor(int param1,int param2) {
        return param1 ^ param2;
    }
    
    /**
     * 引数として渡されたパラメータをビット反転する
     * 
     * @param param
     * @return paramのビット反転結果
     */
    public int bitNot(int param) {
        return ~param;
    }
    
    /**
     * 引数として渡されたパラメータでビットが立っていた場合Trueを返却する
     * 
     * @param param1
     * @param param2
     * @return param1とparam2のand演算結果
     */
    public boolean bitCheck(int param1, int param2) {
        if ((param1 & param2) > 0) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 引数として渡されたパラメータでループ曜日フラグが一つでも立っていた場合、trueを返す
     * @param param
     * @return 
     */
    public boolean dayOfTheWeekCheck(int param) {
        boolean isSundayCheck = this.bitCheck(param, erpConst.IS_ROOP_SUNDAY);
        boolean isMondayCheck = this.bitCheck(param, erpConst.IS_ROOP_MONDAY);
        boolean isTuesdayCheck = this.bitCheck(param, erpConst.IS_ROOP_TUESDAY);
        boolean isWednesdayCheck = this.bitCheck(param, erpConst.IS_ROOP_WEDNESDAY);
        boolean isThursdayCheck = this.bitCheck(param, erpConst.IS_ROOP_THURSDAY);
        boolean isFridayCheck = this.bitCheck(param, erpConst.IS_ROOP_FRIDAY);
        boolean isSaturdayCheck = this.bitCheck(param, erpConst.IS_ROOP_SATURDAY);
        
        return isSundayCheck | isMondayCheck | isTuesdayCheck | isWednesdayCheck | isThursdayCheck | isFridayCheck | isSaturdayCheck;
    }
    
    
}
