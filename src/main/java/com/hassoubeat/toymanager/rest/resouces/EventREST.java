/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resouces;

import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Event;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author hassoubeat
 */
@Stateless
@Path("events")
public class EventREST {
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @GET
    @Path("0.1/callenderEvents")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<RestCallenderEvent> fetchEventForCallender() {
        // TODO 本格的にREST API周りの実装を行う時、他の設計をあわせて実装を見直す
        // TODO SessionBeanの値がなかったらトップに戻す(インターセプター？)
        
        List<RestCallenderEvent> responseList = new ArrayList();
        // アカウントに紐づくイベントの取得
        for(Event event : eventFacade.findByAccountId(accountFacade.find(sessionBean.getId()))) {
            // 取得したイベントリストをREST用エンティティに詰め替える
            RestCallenderEvent rcEvent = new RestCallenderEvent();
            rcEvent.setId(event.getId());
            rcEvent.setTitle(event.getName());
            rcEvent.setStart(event.getStartDate());
            rcEvent.setEnd(event.getEndDate());
            
            List<String> eventType = new ArrayList();
            eventType.add("account-share");
            if (event.getRoop() > 0) {
                eventType.add("roop");
            }
            if (event.getIsTalking()) {
                eventType.add("is-toy-talk");
            }
            rcEvent.setClassName(eventType);
            responseList.add(rcEvent);
            
        }
        // Toyに紐づくイベントの取得
        for (Event event:eventFacade.findByToyId(toyFacade.find(sessionBean.getSelectedToyId()))){
            // TODO AccountIdが指定されているイベントの場合は、スルーする
            
            if(event.getAccountId() == null) {
                // アカウント共有イベントではなかった場合
                
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
                rcEvent.setClassName(eventType);
                responseList.add(rcEvent);

                System.out.println("event.title:" + event.getName());
            }
        }
        
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
