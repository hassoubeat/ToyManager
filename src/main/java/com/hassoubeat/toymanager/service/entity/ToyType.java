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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hassoubeat
 */
@Entity
@Table(name = "toy_type")
@NamedQueries({
    @NamedQuery(name = "ToyType.findAll", query = "SELECT t FROM ToyType t")
    , @NamedQuery(name = "ToyType.findById", query = "SELECT t FROM ToyType t WHERE t.id = :id")
    , @NamedQuery(name = "ToyType.findByName", query = "SELECT t FROM ToyType t WHERE t.name = :name")
    , @NamedQuery(name = "ToyType.findByNote", query = "SELECT t FROM ToyType t WHERE t.note = :note")
    , @NamedQuery(name = "ToyType.findByIsAccessible", query = "SELECT t FROM ToyType t WHERE t.isAccessible = :isAccessible")
    , @NamedQuery(name = "ToyType.findByCreateDate", query = "SELECT t FROM ToyType t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "ToyType.findByEditDate", query = "SELECT t FROM ToyType t WHERE t.editDate = :editDate")})
@XmlRootElement
public class ToyType implements Serializable {

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
    @Size(max = 600)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_accessible")
    private boolean isAccessible;
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
    @OneToMany(mappedBy = "toyTypeId")
    private List<Toy> toyList;

    public ToyType() {
    }

    public ToyType(Integer id) {
        this.id = id;
    }

    public ToyType(Integer id, String name, boolean isAccessible, Date createDate, Date editDate) {
        this.id = id;
        this.name = name;
        this.isAccessible = isAccessible;
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

    public boolean getIsAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(boolean isAccessible) {
        this.isAccessible = isAccessible;
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

    @XmlTransient
    public List<Toy> getToyList() {
        return toyList;
    }

    public void setToyList(List<Toy> toyList) {
        this.toyList = toyList;
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
        if (!(object instanceof ToyType)) {
            return false;
        }
        ToyType other = (ToyType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.web.backingbean.admin.ToyType[ id=" + id + " ]";
    }
    
}
