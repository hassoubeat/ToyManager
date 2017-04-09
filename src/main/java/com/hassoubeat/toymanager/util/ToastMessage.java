/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hassoubeat
 */
public class ToastMessage {
    @Getter
    @Setter
    private boolean isRender;
    @Getter
    @Setter
    private String heading;
    @Getter
    @Setter
    private String text;
    @Getter
    @Setter
    private String showHideTransition;
    @Getter
    @Setter
    private String icon;
    @Getter
    @Setter
    private int hideAfter;
    @Getter
    @Setter
    private String position;

    // 初期値を定義する
    public ToastMessage() {
        isRender = false;
        heading = "";
        text = "";
        showHideTransition = "slide";
        icon = "info";
        hideAfter = 3000;
        position = "bottom-right";
    }
    
    // 各パラメータをカンマ区切りしたパラメータを返却する
    public String genList() {
        String response = "";
        response = isRender + "," + heading + "," + text + "," + showHideTransition + "," + icon + "," + String.valueOf(hideAfter) + "," + position;
        return response;
    }
    
    
}
