/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * ToyTalkから取得する際のEventエンティティ
 * @author hassoubeat
 */
public class RestEvent {
    
    @Getter
    @Setter
    private int id;
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String content;
    
    @Getter
    @Setter
    private Date startDate;
    
    @Getter
    @Setter
    private Date endDate;
    
    @Getter
    @Setter
    private int roop;
    
    @Getter
    @Setter
    private Date roopEndDate;

    public RestEvent() {
        this.id = id;
        this.name = name;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roop = roop;
        this.roopEndDate = roopEndDate;
    }
    
    
    
    
    
    
    
}
