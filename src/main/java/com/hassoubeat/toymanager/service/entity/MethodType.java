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
@Table(name = "method_type")
@NamedQueries({
    @NamedQuery(name = "MethodType.findAll", query = "SELECT m FROM MethodType m")
    , @NamedQuery(name = "MethodType.findById", query = "SELECT m FROM MethodType m WHERE m.id = :id")
    , @NamedQuery(name = "MethodType.findByMethod", query = "SELECT m FROM MethodType m WHERE m.method = :method")
    , @NamedQuery(name = "MethodType.findByCreateDate", query = "SELECT m FROM MethodType m WHERE m.createDate = :createDate")
    , @NamedQuery(name = "MethodType.findByEditDate", query = "SELECT m FROM MethodType m WHERE m.editDate = :editDate")})
public class MethodType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "method")
    private String method;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "methodTypeId")
    private List<DiffSyncEvent> diffSyncEventList;

    public MethodType() {
    }

    public MethodType(Integer id) {
        this.id = id;
    }

    public MethodType(Integer id, String method, Date createDate, Date editDate) {
        this.id = id;
        this.method = method;
        this.createDate = createDate;
        this.editDate = editDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public List<DiffSyncEvent> getDiffSyncEventList() {
        return diffSyncEventList;
    }

    public void setDiffSyncEventList(List<DiffSyncEvent> diffSyncEventList) {
        this.diffSyncEventList = diffSyncEventList;
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
        if (!(object instanceof MethodType)) {
            return false;
        }
        MethodType other = (MethodType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.MethodType[ id=" + id + " ]";
    }
    
}
