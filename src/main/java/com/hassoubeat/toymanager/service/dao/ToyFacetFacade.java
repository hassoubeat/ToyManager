/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.ToyFacet;
import com.hassoubeat.toymanager.service.entity.ToyFacet_;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class ToyFacetFacade extends AbstractFacade<ToyFacet> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ToyFacetFacade() {
        super(ToyFacet.class);
    }
    
    /**
     * Toyに紐づくToyFacetを削除する
     * 
     * @param toyId 
     */
    public void removeByToyId(Toy toyId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<ToyFacet> delete = builder.createCriteriaDelete(ToyFacet.class);
        Root<ToyFacet> root = delete.from(ToyFacet.class);

        delete.where(builder.equal(root.get(ToyFacet_.toyId), toyId));

        Query q = getEntityManager().createQuery(delete);
        q.executeUpdate();
        
        this.flush();
        this.clear();
    }
    
}
