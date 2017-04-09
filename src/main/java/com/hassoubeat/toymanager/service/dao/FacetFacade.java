/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.service.entity.Facet_;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class FacetFacade extends AbstractFacade<Facet> {

    @PersistenceContext(unitName = "com.hassoubeat_ToyManager_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacetFacade() {
        super(Facet.class);
    }
    
    /**
     * ファセット名の重複チェック
     * @param facetName
     * @return 重複していた場合 True 
     */
    public boolean isFacetNameOverrap(String facetName) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Facet> root = cq.from(Facet.class);
        cq.select(builder.count(root));
        
        Predicate facetNameEquals = builder.equal(root.get(Facet_.name), facetName);
        cq.where(facetNameEquals);
        
        Query query = getEntityManager().createQuery(cq);
        
        long count = ((Long) query.getSingleResult()).intValue();
        return count > 0;
    }
    
}
