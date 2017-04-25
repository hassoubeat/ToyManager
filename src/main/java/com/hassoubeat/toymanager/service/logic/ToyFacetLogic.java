/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.FacetFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacetFacade;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.service.entity.FacetEvent;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.ToyFacet;
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
public class ToyFacetLogic{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    FacetFacade facetFacade;
    
    @EJB
    ToyFacetFacade toyFacetFacade;
    
    @EJB
    EventFacade eventFacade;

    public ToyFacetLogic() {
    }
    
    /**
     * ToyFacetの新規追加
     * @param addFacetId Toyに追加するFacetのID
     * @return 作成したToyFacet
     */
    public ToyFacet create(Integer addFacetId) {
        ToyFacet addToyFacet = new ToyFacet();
        Toy targetToy = toyFacade.find(sessionBean.getSelectedToyId());
        Facet addFacet = facetFacade.find(addFacetId);
        addToyFacet.setToyId(targetToy);
        addToyFacet.setFacetId(addFacet);
        addToyFacet.setFacetVersion(addFacet.getFacetVersion());
        // ToyFacetの登録
        ToyFacet createToyFacet = toyFacetFacade.create(addToyFacet);
        
        // FacetEventからイベントテーブルに複写
        for (FacetEvent facetEvent : addFacet.getFacetEventList()) {
            Event event = new Event();
            event.setName(facetEvent.getName());
            event.setContent(facetEvent.getContent());
            event.setStartDate(facetEvent.getStartDate());
            event.setEndDate(facetEvent.getEndDate());
            event.setColorCode(facetEvent.getColorCode());
            event.setRoop(facetEvent.getRoop());
            event.setRoopEndDate(facetEvent.getRoopEndDate());
            event.setPriority(facetEvent.getPriority());
            event.setIsTalking(facetEvent.getIsTalking());
            event.setToyFacetId(createToyFacet);
            Event createEvent = eventFacade.create(event);
            logger.info("{}.{} USER_ID:{} EVENT_ID:{}", MessageConst.SUCCESS_FACET_EVENT_COPY.getId(), MessageConst.SUCCESS_FACET_EVENT_COPY.getMessage(), sessionBean.getId(), createEvent.getId());
        }
        logger.info("{}.{} USER_ID:{} TOY_FACET_ID:{}", MessageConst.SUCCESS_TOY_FACET_CREATE.getId(), MessageConst.SUCCESS_TOY_FACET_CREATE.getMessage(), sessionBean.getId(), createToyFacet.getId());
        return createToyFacet;
    }
    
    /**
     * ToyFacetのアップデート
     * @param updateToyFacetId アップデートするToyFacetIdを取得する
     * @return 更新したToyFacet
     */
    public ToyFacet update(Integer updateToyFacetId) {
        // 先にToyFacetに紐付いているイベントを削除する
        ToyFacet updateToyFacet = toyFacetFacade.find(updateToyFacetId);
        // ToyFacetIDからイベントを削除する(いまのままだと紐付いているEventが全部消える)
        eventFacade.removeByToyFacetId(updateToyFacet);
//        for (Event removeEvent : updateToyFacet.getToyId().getEventList()) {
//            // TODO 
//            
//        }
        updateToyFacet.setFacetVersion(updateToyFacet.getFacetId().getFacetVersion());
        // ToyFacetの変更
        ToyFacet editToyFacet = toyFacetFacade.edit(updateToyFacet);
        // FacetEventからイベントテーブルに複写
        for (FacetEvent facetEvent : editToyFacet.getFacetId().getFacetEventList()) {
            Event event = new Event();
            event.setName(facetEvent.getName());
            event.setContent(facetEvent.getContent());
            event.setStartDate(facetEvent.getStartDate());
            event.setEndDate(facetEvent.getEndDate());
            event.setColorCode(facetEvent.getColorCode());
            event.setRoop(facetEvent.getRoop());
            event.setRoopEndDate(facetEvent.getRoopEndDate());
            event.setPriority(facetEvent.getPriority());
            event.setIsTalking(facetEvent.getIsTalking());
            event.setToyFacetId(updateToyFacet);
            Event createEvent = eventFacade.create(event);
            logger.info("{}.{} USER_ID:{} EVENT_ID:{}", MessageConst.SUCCESS_FACET_EVENT_COPY.getId(), MessageConst.SUCCESS_FACET_EVENT_COPY.getMessage(), sessionBean.getId(), createEvent.getId());
        }
        logger.info("{}.{} USER_ID:{} TOY_FACET_ID:{}", MessageConst.SUCCESS_TOY_FACET_UPDATE.getId(), MessageConst.SUCCESS_TOY_FACET_UPDATE.getMessage(), sessionBean.getId(), updateToyFacet.getId());
        return updateToyFacet;
    }
    
}