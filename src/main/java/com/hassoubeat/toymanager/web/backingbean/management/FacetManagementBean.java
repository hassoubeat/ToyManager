/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.management;

import com.hassoubeat.toymanager.annotation.AuthManagerInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.dao.FacetFacade;
import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import lombok.Getter;

/**
 *
 * @author hassoubeat
 */
@Named(value = "facetManagementBean")
@RequestScoped
public class FacetManagementBean {

    @Inject
    SessionBean sessionBean;
    @EJB
    FacetFacade facetFacade;
    
    @Getter
    List<Facet> facetList;
    
    /**
     * Creates a new instance of TopBean
     */
    public FacetManagementBean() {
    }
    
    @ErrorInterceptor
    @AuthManagerInterceptor
    @LogInterceptor
    public void bookmarkable() {
        // ファセット一覧の取得
        facetList = facetFacade.findAll();
    }
    
}
