/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "toy_facet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ToyFacet.findAll", query = "SELECT t FROM ToyFacet t")
    , @NamedQuery(name = "ToyFacet.findById", query = "SELECT t FROM ToyFacet t WHERE t.id = :id")
    , @NamedQuery(name = "ToyFacet.findByCreateDate", query = "SELECT t FROM ToyFacet t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "ToyFacet.findByEditDate", query = "SELECT t FROM ToyFacet t WHERE t.editDate = :editDate")
    , @NamedQuery(name = "ToyFacet.findByFacetVersion", query = "SELECT t FROM ToyFacet t WHERE t.facetVersion = :facetVersion")})
public class ToyFacet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "edit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "facet_version")
    private double facetVersion;
    @JoinColumn(name = "facet_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Facet facetId;
    @JoinColumn(name = "toy_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Toy toyId;

    public ToyFacet() {
    }

    public ToyFacet(Integer id) {
        this.id = id;
    }

    public ToyFacet(Integer id, Date createDate, Date editDate, double facetVersion) {
        this.id = id;
        this.createDate = createDate;
        this.editDate = editDate;
        this.facetVersion = facetVersion;
    }
    
    @PrePersist
    public void prePersist(){
        // 登録日時と更新日時に現在日時を設定する
        Date now = new Date();
        this.setCreateDate(now);
        this.setEditDate(now);
    }
    
    @PreUpdate
    public void preUpdate(){
        // 更新日時を更新する
        Date now = new Date();
        this.setEditDate(now);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public double getFacetVersion() {
        return facetVersion;
    }

    public void setFacetVersion(double facetVersion) {
        this.facetVersion = facetVersion;
    }

    public Facet getFacetId() {
        return facetId;
    }

    public void setFacetId(Facet facetId) {
        this.facetId = facetId;
    }

    public Toy getToyId() {
        return toyId;
    }

    public void setToyId(Toy toyId) {
        this.toyId = toyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ToyFacet)) {
            return false;
        }
        ToyFacet other = (ToyFacet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.ToyFacet[ id=" + id + " ]";
    }
    
}
