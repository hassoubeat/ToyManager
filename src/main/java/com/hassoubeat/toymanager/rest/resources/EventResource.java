/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.rest.resources.entity.RestCalenderEvent;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.rest.resources.entity.RestEvent;
import com.hassoubeat.toymanager.rest.resources.logic.RestCalenderEventLogic;
import com.hassoubeat.toymanager.rest.resources.logic.RestEventLogic;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.dao.ToyWebapiAccessFilterFacade;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.util.BitLogic;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    
    @EJB
    RestCalenderEventLogic restCalenderEventLogic;
    
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
    ToyWebapiAccessFilterFacade toyWebapiAccessFilterFacade;
    
    /**
     * FullCalenderのイベント取得用のRESTAPI
     * ※ 他のAPIと異なり、ToyManager利用中でのみ実行することができるAPI
     * @param startDateStr
     * @param endDateStr
     * @param isFetchFacetEvent
     * @return 
     */
    @GET
    @Path("0.1/calenderEvents")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @LogInterceptor
    @AuthGeneralInterceptor
    public List<RestCalenderEvent> fetchEventForCalender(@QueryParam("start") String startDateStr, @QueryParam("end") String endDateStr, @QueryParam("isFetchAccountEvent") boolean isFetchAccountEvent, @QueryParam("isFetchFacetEvent") boolean isFetchFacetEvent) {  
        // 日付形式のパラメータでなかった場合、(変換した時にparseExceptionが出た時にはBadRequestを返却する
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(startDateStr);
            endDate = formatter.parse(endDateStr);
        } catch (NullPointerException | ParseException ex) {
            // 日付型に合致しないデータだった場合、リクエスト不備として返却する
            throw new BadRequestException(ex);
        }
        return restCalenderEventLogic.fetchCalenderEvent(startDate, endDate, isFetchAccountEvent, isFetchFacetEvent);
//        List<RestCalenderEvent> restCalnderEventList = ;
//        for (int index = 0; index <= restCalnderEventList.size(); index++) {
//            RestCalenderEvent item = restCalnderEventList.get(index);
//             if (item.getStart().compareTo());
//        }
    }
    
    /**
     * Toyに紐づく全イベントを取得する
     * @param header ヘッダー情報
     * @param startDateStr
     * @param endDateStr
     * @return 
     */
    @GET
    @Path("0.1/events")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @LogInterceptor
//    @RestAuth TODO うまくフィルターが働かず、全リソースに適応されてしまうやむなしで実行前チェックメソッドを手動呼び出し
    public List<RestEvent> fetchAllEvents(@Context HttpHeaders header, @QueryParam("start") String startDateStr, @QueryParam("end") String endDateStr) {
        
        // 日付形式のパラメータでなかった場合、(変換した時にparseExceptionが出た時にはBadRequestを返却する
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fetchStartDate = null;
        Date fetchEndDate = null;
        try {
            fetchStartDate = formatter.parse(startDateStr);
            fetchEndDate = formatter.parse(endDateStr);
        } catch (NullPointerException | ParseException ex) {
            // 日付型に合致しないデータだった場合、リクエスト不備として返却する
            throw new BadRequestException(ex);
        }
        
        // ヘッダから認可情報の取得
        String rotNumStr = header.getHeaderString("rotNum");
        String accessToken = header.getHeaderString("authorication");
        String macAddress = header.getHeaderString("macAddress");
        
        // 実行前チェック
        restEventLogic.RestAuthorization(rotNumStr, accessToken, macAddress);
        
        Toy toy = toyFacade.findByRotNumber(Integer.parseInt(rotNumStr));
        
        List<RestEvent> responseList = new ArrayList();
        responseList.addAll(restEventLogic.fetchEvent(toy, fetchStartDate, fetchEndDate));
        
        return responseList;
                
    }
    
//    /**
//     * Toyのイベント取得完了を通知する
//     * @param header ヘッダー情報
//     */
//    @GET
//    @Path("0.1/events/notice")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    @LogInterceptor
//    public void fetchEventsCompleteNotice(@Context HttpHeaders header) {
//        // ヘッダから認可情報の取得
//        String rotNumStr = header.getHeaderString("rotNum");
//        String accessToken = header.getHeaderString("authorication");
//        String macAddress = header.getHeaderString("macAddress");
//        
//        // 実行前チェック
//        restEventLogic.RestAuthorization(rotNumStr, accessToken, macAddress);
//        
//        
//        
//    }
}
