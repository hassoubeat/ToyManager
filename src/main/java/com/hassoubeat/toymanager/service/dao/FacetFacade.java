/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.dao;

import com.hassoubeat.toymanager.service.entity.Facet;
import com.hassoubeat.toymanager.service.entity.Facet_;
import java.util.List;
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
     * 公開済のファセット全件を取得する
     * @return 公開済のファセット一覧
     */
    public List<Facet> findAllByReleasedFacet() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<Facet> root = cq.from(Facet.class);
        cq.select(root);
        
        // WHERE条件の型と検索条件名(Entityの要素名)を指定する
        Predicate isReleasedEqual = builder.equal(root.get(Facet_.isRelease), true);
        cq.where(isReleasedEqual);
        
        return getEntityManager().createQuery(cq).getResultList();
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
