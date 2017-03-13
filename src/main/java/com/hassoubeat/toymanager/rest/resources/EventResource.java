/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources;

import com.hassoubeat.toymanager.rest.resources.entity.RestCallenderEvent;
import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.rest.resources.entity.RestEvent;
import com.hassoubeat.toymanager.rest.resources.logic.RestEventLogic;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.dao.ToyWebapiAccessFilterFacade;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@RequestScoped
@Path("events")
public class EventResource {
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @Inject
    RestEventLogic restEventLogic;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    ToyWebapiAccessFilterFacade toyWebapiAccessFilterFacade;
    
    
    
    @GET
    @Path("0.1/callenderEvents")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @AuthGeneralInterceptor
    @ErrorInterceptor
    @LogInterceptor
    public List<RestCallenderEvent> fetchEventForCallender() {
        // TODO 本格的にREST API周りの実装を行う時、他の設計をあわせて実装を見直す
        // TODO SessionBeanの値がなかったらトップに戻す(インターセプター？)
        
//        boolean test = true;
//        Account targetAccount = accountFacade.find(sessionBean.getUserId());
//        for(Toy toy:targetAccount.getToyList()) {
//            if (Objects.equals(toy.getId(), sessionBean.getSelectedToyId())) {
//                test = false;
//            }
//        } 
//        
//        if (test) {
//            return null;
//        }
        
        List<RestCallenderEvent> responseList = new ArrayList();
        // アカウントに紐づくイベントの取得
        for(Event event : eventFacade.findByAccountId(accountFacade.find(sessionBean.getId()))) {
            // 取得したイベントリストをREST用エンティティに詰め替える
            RestCallenderEvent rcEvent = new RestCallenderEvent();
            rcEvent.setId(event.getId());
            rcEvent.setTitle(event.getName());
            rcEvent.setStart(event.getStartDate());
            rcEvent.setEnd(event.getEndDate());
            rcEvent.setColor(event.getColorCode());
            
            List<String> eventType = new ArrayList();
            eventType.add("account-share");
            if (event.getRoop() > 0) {
                eventType.add("roop");
            }
            if (event.getIsTalking()) {
                eventType.add("is-toy-talk");
            }
            if (event.getIsDeleted()) {
                eventType.add("is-event-deleted");
            }
            rcEvent.setClassName(eventType);
            responseList.add(rcEvent);
            
        }
        // Toyに紐づくイベントの取得
        for (Event event:eventFacade.findByToyId(toyFacade.find(sessionBean.getSelectedToyId()))){
            
            // 取得したイベントリストをREST用エンティティに詰め替える
            RestCallenderEvent rcEvent = new RestCallenderEvent();
            rcEvent.setId(event.getId());
            rcEvent.setTitle(event.getName());
            rcEvent.setStart(event.getStartDate());
            rcEvent.setEnd(event.getEndDate());
            rcEvent.setColor(event.getColorCode());
            List<String> eventType = new ArrayList();
            if (event.getRoop() > 0) {
                eventType.add("roop");
            }
            if (event.getIsTalking()) {
                eventType.add("is-toy-talk");
            }
            if (event.getIsDeleted()) {
                eventType.add("is-event-deleted");
            }
            rcEvent.setClassName(eventType);
            responseList.add(rcEvent);

            System.out.println("event.title:" + event.getName());
            
        }
        
        // TODO ファセットに紐づくイベントの取得
        
        return responseList;
    }
    
    /**
     * Toyに紐づく全イベントを取得する
     * @param header ヘッダー情報
     * @return 
     */
    @GET
    @Path("0.1/events")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @LogInterceptor
    public List<RestEvent> fetchAllEvents(@Context HttpHeaders header) {
//        RestEventLogic restEventLogic = new RestEventLogic();        
        restEventLogic.RestAuthorization(header);
        Toy toy = toyFacade.findByRotNumber(Integer.parseInt(header.getHeaderString("rotNum")));
        
        List<RestEvent> responseList = new ArrayList();
        
        // アカウントに紐づくイベントの取得
        for (Event accountEvent :eventFacade.findByAccountId(toy.getAccountId())) {
            // 取得したイベントリストをREST用エンティティに詰め替える
            RestEvent rcEvent = new RestEvent();
            rcEvent.setId(accountEvent.getId());
            rcEvent.setName(accountEvent.getName());
            rcEvent.setContent(accountEvent.getContent());
            rcEvent.setStartDate(accountEvent.getStartDate());
            rcEvent.setEndDate(accountEvent.getEndDate());
            rcEvent.setRoop(accountEvent.getRoop());
            responseList.add(rcEvent);
        }
        
        // Toyに紐づくイベントの取得
        for (Event toyEvent : toy.getEventList()) {
            // 取得したイベントリストをREST用エンティティに詰め替える
            RestEvent rcEvent = new RestEvent();
            rcEvent.setId(toyEvent.getId());
            rcEvent.setName(toyEvent.getName());
            rcEvent.setContent(toyEvent.getContent());
            rcEvent.setStartDate(toyEvent.getStartDate());
            rcEvent.setEndDate(toyEvent.getEndDate());
            rcEvent.setRoop(toyEvent.getRoop());
            responseList.add(rcEvent);
        }
        
        // TODO ファセットに紐づくイベントの取得
        return responseList;
    }
    
//    @POST
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void create(Event entity) {
//        
//    }
//
//    @PUT
//    @Path("{id}")
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void edit(@PathParam("id") Integer id, Event entity) {
//
//    }
//
//    @DELETE
//    @Path("{id}")
//    public void remove(@PathParam("id") Integer id) {
//        
//    }
//
//    @GET
//    @Path("{id}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public Event find(@PathParam("id") Integer id) {
//        
//    }
//
//    @GET
//    
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Event> findAll() {
//        
//    }
//
//    @GET
//    @Path("{from}/{to}")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public List<Event> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Path("count")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String countREST() {
//        return String.valueOf(super.count());
//    }
//
//    @Override
//    protected EntityManager getEntityManager() {
//        return em;
//    }
//    
}
