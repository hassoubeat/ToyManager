/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.logic;

import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.rest.resources.entity.RestEvent;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.ToyFacet;
import com.hassoubeat.toymanager.util.BitLogic;
import com.hassoubeat.toymanager.util.UtilLogic;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;



/**
 * RestEventに関するLogicクラス
 * @author hassoubeat
 */

@Stateless
public class RestEventLogic extends AbstractRestLogic {
    
    @Inject
    Logger logger;
    
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
    
    public RestEventLogic() {
    }
    
    /**
     * RestEventを取得する
     * @param targetToy
     * @param fetchStartDate
     * @param fetchEndDate
     * @return 
     */
    public List<RestEvent> fetchEvent(Toy targetToy, Date fetchStartDate, Date fetchEndDate) {
        List<RestEvent> responseList = new ArrayList();
        
        // Toyイベントの取得
        responseList.addAll(fetchToyEvent(targetToy, fetchStartDate, fetchEndDate));
        // アカウントイベントの取得
        responseList.addAll(fetchAccountEvent(targetToy.getAccountId(), fetchStartDate, fetchEndDate));
        // ファセットイベントの取得
        // TODO 一回のSQL発行で取得できないか検討する
        for (ToyFacet toyFacet : targetToy.getToyFacetList()) {
            responseList.addAll(fetchFacetEvent(toyFacet, fetchStartDate, fetchEndDate));
        }
        return responseList;
    }
    
    /**
     * Toyに紐付いているイベントを取得する
     * @param toyId
     * @return 
     */
    private List<RestEvent> fetchToyEvent(Toy toyId, Date fetchStartDate, Date fetchEndDate) {
        List<Event> convertList = new ArrayList();
        List<RestEvent> responseList = new ArrayList();
        convertList.addAll(eventFacade.findStandardEventByToyIdForToyTalk(toyId, fetchStartDate, fetchEndDate));
        convertList.addAll(eventFacade.findRoopEventByToyIdForToyTalk(toyId, fetchStartDate));
        for(Event toyEvent : convertList) {
            responseList.add(convertEvent(toyEvent));
        }
        for (RestEvent item: responseList) {
            logger.debug(item.toString());
        }
        return responseList;
    }
    
    /**
     * Accountに紐付いているイベントを取得する
     * @param accountId
     * @param fetchStartDate
     * @param fetchEndDate
     * @return 
     */
    private List<RestEvent> fetchAccountEvent(Account accountId, Date fetchStartDate, Date fetchEndDate) {
        List<Event> convertList = new ArrayList();
        List<RestEvent> responseList = new ArrayList();
        convertList.addAll(eventFacade.findStandardEventByAccountIdForToyTalk(accountId, fetchStartDate, fetchEndDate));
        convertList.addAll(eventFacade.findRoopEventByAccountIdForToyTalk(accountId, fetchStartDate));
        for(Event toyEvent : convertList) {
            responseList.add(convertEvent(toyEvent));
        }
        for (RestEvent item: responseList) {
            logger.debug(item.toString());
        }
        return responseList;
    }
    
    /**
     * Facetに紐付いているイベントを取得する
     * @param facetId
     * @param fetchStartDate
     * @param fetchEndDate
     * @return 
     */
    private List<RestEvent> fetchFacetEvent(ToyFacet toyFacetId, Date fetchStartDate, Date fetchEndDate) {
        List<Event> convertList = new ArrayList();
        List<RestEvent> responseList = new ArrayList();
        convertList.addAll(eventFacade.findStandardEventByToyFacetIdForToyTalk(toyFacetId, fetchStartDate, fetchEndDate));
        convertList.addAll(eventFacade.findRoopEventByToyFacetIdForToyTalk(toyFacetId, fetchStartDate));
        for(Event toyEvent : convertList) {
            responseList.add(convertEvent(toyEvent));
        }
        for (RestEvent item: responseList) {
            logger.debug(item.toString());
        }
        return responseList;
    }
    
    /**
     * EventをRestEventに変換して返却する
     * @return 
     */
    private RestEvent convertEvent(Event targetEvent) {
        // イベントをREST用エンティティに詰め替える
        RestEvent rEvent = new RestEvent();
        rEvent.setId(targetEvent.getId());
        rEvent.setName(targetEvent.getName());
        rEvent.setContent(targetEvent.getContent());
        rEvent.setStartDate(targetEvent.getStartDate());
        rEvent.setEndDate(targetEvent.getEndDate());
        rEvent.setRoop(targetEvent.getRoop());
        rEvent.setRoopEndDate(targetEvent.getRoopEndDate());
        if (targetEvent.getToyId() != null) {
            rEvent.setToyId(targetEvent.getToyId().getId());
        }
        if (targetEvent.getAccountId() != null) {
            rEvent.setAccountId(targetEvent.getAccountId().getId());
        }
        if (targetEvent.getToyFacetId() != null) {
            rEvent.setToyFacetId(targetEvent.getToyFacetId().getId());
            rEvent.setFacetVersion(targetEvent.getToyFacetId().getFacetVersion());
            rEvent.setFacetProgramPath(targetEvent.getToyFacetId().getFacetId().getProgramPath());
        }
        
        return rEvent;
    }
    
    
    
}
