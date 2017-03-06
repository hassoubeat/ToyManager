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
@Table(name = "toy_version")
@NamedQueries({
    @NamedQuery(name = "ToyVersion.findAll", query = "SELECT t FROM ToyVersion t")
    , @NamedQuery(name = "ToyVersion.findByVersion", query = "SELECT t FROM ToyVersion t WHERE t.version = :version")
    , @NamedQuery(name = "ToyVersion.findByUpdateNote", query = "SELECT t FROM ToyVersion t WHERE t.updateNote = :updateNote")
    , @NamedQuery(name = "ToyVersion.findByUpdateFileUrl", query = "SELECT t FROM ToyVersion t WHERE t.updateFileUrl = :updateFileUrl")
    , @NamedQuery(name = "ToyVersion.findByIsDeleted", query = "SELECT t FROM ToyVersion t WHERE t.isDeleted = :isDeleted")
    , @NamedQuery(name = "ToyVersion.findByCreateDate", query = "SELECT t FROM ToyVersion t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "ToyVersion.findByEditDate", query = "SELECT t FROM ToyVersion t WHERE t.editDate = :editDate")})
public class ToyVersion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private Double version;
    @Size(max = 1000)
    @Column(name = "update_note")
    private String updateNote;
    @Size(max = 300)
    @Column(name = "update_file_url")
    private String updateFileUrl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;
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
    @OneToMany(mappedBy = "toyVersionId")
    private List<Toy> toyList;

    public ToyVersion() {
    }

    public ToyVersion(double version) {
        this.version = version;
    }

    public ToyVersion(double version, boolean isDeleted, Date createDate, Date editDate) {
        this.version = version;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.editDate = editDate;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getUpdateNote() {
        return updateNote;
    }

    public void setUpdateNote(String updateNote) {
        this.updateNote = updateNote;
    }

    public String getUpdateFileUrl() {
        return updateFileUrl;
    }

    public void setUpdateFileUrl(String updateFileUrl) {
        this.updateFileUrl = updateFileUrl;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public List<Toy> getToyList() {
        return toyList;
    }

    public void setToyList(List<Toy> toyList) {
        this.toyList = toyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (version != null ? version.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ToyVersion)) {
            return false;
        }
        ToyVersion other = (ToyVersion) object;
        if ((this.version == null && other.version != null) || (this.version != null && !this.version.equals(other.version))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.ToyVersion[ version=" + version + " ]";
    }
    
}
