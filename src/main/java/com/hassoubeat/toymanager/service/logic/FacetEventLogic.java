/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.FacetEvent;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class FacetEventLogic{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    EventLogic eventLogic;

    public FacetEventLogic() {
    }
    
    /**
     * 受け取ったFacetEventエンティティからどういったイベントであるかを論理的な文字列で返却するメソッド
     * @param facetEvent
     * @return 
     */
    public String genFacetEventInfo(FacetEvent facetEvent) {
        Event event = new Event();
        event.setStartDate(facetEvent.getStartDate());
        event.setRoop(facetEvent.getRoop());
        event.setRoopEndDate(facetEvent.getRoopEndDate());
        return eventLogic.genEventInfo(event);
    }
    
}
