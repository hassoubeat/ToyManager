/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.phaselisner;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
public class PhaseLisner implements PhaseListener{
    
    @Inject
    Logger logger;
    
    long beforeTime;
    long afterTime;
    
    @Override
    public void beforePhase(PhaseEvent event){
        // フェイズ開始時刻の保存
        beforeTime = System.nanoTime();
        logger.info("{}", event.getPhaseId() + "PHASE_START");
        
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        // フェイズ開始時刻の保存
        afterTime = System.nanoTime();
        logger.info("{} : {}", event.getPhaseId() + "PHASE_RUN_TIME", (afterTime - beforeTime) + "nsecs.");
        
        logger.info("{}", event.getPhaseId() + "PHASE_END");
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
}
