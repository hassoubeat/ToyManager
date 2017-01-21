/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.phaselisner;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author hassoubeat
 */
public class PhaseLisner implements PhaseListener{
    @Override
    public void beforePhase(PhaseEvent event){
        
        System.out.println(event.getPhaseId() + "PhaseBefore");
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        System.out.println(event.getPhaseId() + "PhaseAfter");
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
}
