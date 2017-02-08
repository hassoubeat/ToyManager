/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author hassoubeat
 */
@Named(value = "topBean")
@RequestScoped
public class TopBean {

    @Inject
    SessionBean sessionBean;
    
    /**
     * Creates a new instance of TopBean
     */
    public TopBean() {
    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable() {
        // セッション情報でログイン済であった場合、トップ画面へリダイレクトする
        
        
    }
    
}
