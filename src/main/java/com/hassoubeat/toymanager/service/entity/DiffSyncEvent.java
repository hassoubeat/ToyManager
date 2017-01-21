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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "diff_sync_event")
@NamedQueries({
    @NamedQuery(name = "DiffSyncEvent.findAll", query = "SELECT d FROM DiffSyncEvent d")
    , @NamedQuery(name = "DiffSyncEvent.findById", query = "SELECT d FROM DiffSyncEvent d WHERE d.id = :id")
    , @NamedQuery(name = "DiffSyncEvent.findByCreateDate", query = "SELECT d FROM DiffSyncEvent d WHERE d.createDate = :createDate")
    , @NamedQuery(name = "DiffSyncEvent.findByEditDate", query = "SELECT d FROM DiffSyncEvent d WHERE d.editDate = :editDate")})
public class DiffSyncEvent implements Serializable {

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
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Event eventId;
    @JoinColumn(name = "method_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MethodType methodTypeId;
    @JoinColumn(name = "toy_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Toy toyId;

    public DiffSyncEvent() {
    }

    public DiffSyncEvent(Integer id) {
        this.id = id;
    }

    public DiffSyncEvent(Integer id, Date createDate, Date editDate) {
        this.id = id;
        this.createDate = createDate;
        this.editDate = editDate;
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

    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }

    public MethodType getMethodTypeId() {
        return methodTypeId;
    }

    public void setMethodTypeId(MethodType methodTypeId) {
        this.methodTypeId = methodTypeId;
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
        if (!(object instanceof DiffSyncEvent)) {
            return false;
        }
        DiffSyncEvent other = (DiffSyncEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.web.backingbean.admin.DiffSyncEvent[ id=" + id + " ]";
    }
    
}
