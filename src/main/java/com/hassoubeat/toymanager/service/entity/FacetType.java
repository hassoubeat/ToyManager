/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "facet_type")
@NamedQueries({
    @NamedQuery(name = "FacetType.findAll", query = "SELECT f FROM FacetType f")
    , @NamedQuery(name = "FacetType.findById", query = "SELECT f FROM FacetType f WHERE f.id = :id")
    , @NamedQuery(name = "FacetType.findByName", query = "SELECT f FROM FacetType f WHERE f.name = :name")
    , @NamedQuery(name = "FacetType.findByNote", query = "SELECT f FROM FacetType f WHERE f.note = :note")
    , @NamedQuery(name = "FacetType.findByCreateDate", query = "SELECT f FROM FacetType f WHERE f.createDate = :createDate")
    , @NamedQuery(name = "FacetType.findByEditDate", query = "SELECT f FROM FacetType f WHERE f.editDate = :editDate")})
public class FacetType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "note")
    private String note;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facetTypeId")
    private List<Facet> facetList;

    public FacetType() {
    }

    public FacetType(Integer id) {
        this.id = id;
    }

    public FacetType(Integer id, String name, Date createDate, Date editDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.editDate = editDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public List<Facet> getFacetList() {
        return facetList;
    }

    public void setFacetList(List<Facet> facetList) {
        this.facetList = facetList;
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
        if (!(object instanceof FacetType)) {
            return false;
        }
        FacetType other = (FacetType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.web.backingbean.admin.FacetType[ id=" + id + " ]";
    }
    
}
