/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;

import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.PropertyConst;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * アプリケーションレベルのプロパティを保持するBean(環境によって変わる値など)
 * @author hassoubeat
 */
@Named(value = "appPropetiesBean")
@ApplicationScoped
public class AppPropetiesBean {

    @Getter
    private final String HOST_PATH;
    @Getter
    private final String RESOURCES_URL;
    @Getter
    private final String RESOURCES_ABSOLUTE_PATH;
    
    /**
     * Creates a new instance of AppPropetiesBean
     */
    public AppPropetiesBean() {
        HOST_PATH = PropertyConst.HOST_PATH;
        RESOURCES_URL = PropertyConst.RESOURCES_URL;
        RESOURCES_ABSOLUTE_PATH = PropertyConst.RESOURCES_ABSOLUTE_PATH;
    }
    
}
