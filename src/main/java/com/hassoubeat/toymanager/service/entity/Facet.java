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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
@Table(name = "facet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facet.findAll", query = "SELECT f FROM Facet f")
    , @NamedQuery(name = "Facet.findById", query = "SELECT f FROM Facet f WHERE f.id = :id")
    , @NamedQuery(name = "Facet.findByName", query = "SELECT f FROM Facet f WHERE f.name = :name")
    , @NamedQuery(name = "Facet.findByFacetVersion", query = "SELECT f FROM Facet f WHERE f.facetVersion = :facetVersion")
    , @NamedQuery(name = "Facet.findByNote", query = "SELECT f FROM Facet f WHERE f.note = :note")
    , @NamedQuery(name = "Facet.findByRequirement", query = "SELECT f FROM Facet f WHERE f.requirement = :requirement")
    , @NamedQuery(name = "Facet.findByProgramPath", query = "SELECT f FROM Facet f WHERE f.programPath = :programPath")
    , @NamedQuery(name = "Facet.findByIsRelease", query = "SELECT f FROM Facet f WHERE f.isRelease = :isRelease")
    , @NamedQuery(name = "Facet.findByIsDeleted", query = "SELECT f FROM Facet f WHERE f.isDeleted = :isDeleted")
    , @NamedQuery(name = "Facet.findByCreateDate", query = "SELECT f FROM Facet f WHERE f.createDate = :createDate")
    , @NamedQuery(name = "Facet.findByEditDate", query = "SELECT f FROM Facet f WHERE f.editDate = :editDate")})
public class Facet implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facetId")
    private List<ToyFacet> toyFacetList;

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "facet_version")
    private double facetVersion;
    @Size(max = 200)
    @Column(name = "note")
    private String note;
    @Size(max = 600)
    @Column(name = "requirement")
    private String requirement;
    @Size(max = 500)
    @Column(name = "program_path")
    private String programPath;
    @Size(max = 500)
    @Column(name = "properties_path")
    private String propertiesPath;
    @Size(max = 500)
    @Column(name = "properties_edit_view_path")
    private String propertiesEditViewPath;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_release")
    private boolean isRelease;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facetId")
    private List<FacetEvent> facetEventList;

    public Facet() {
    }

    public Facet(Integer id) {
        this.id = id;
    }

    public Facet(Integer id, String name, double facetVersion, boolean isRelease, boolean isDeleted, Date createDate, Date editDate) {
        this.id = id;
        this.name = name;
        this.facetVersion = facetVersion;
        this.isRelease = isRelease;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.editDate = editDate;
    }
    
    @PrePersist
    public void prePersist(){
        // 公開フラグを入力する
        this.setIsRelease(false);
        // 削除フラグを入力する
        this.setIsDeleted(false);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFacetVersion() {
        return facetVersion;
    }

    public void setFacetVersion(double facetVersion) {
        this.facetVersion = facetVersion;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getProgramPath() {
        return programPath;
    }

    public void setProgramPath(String programPath) {
        this.programPath = programPath;
    }
    
    public String getPropertiesPath() {
        return propertiesPath;
    }

    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public String getPropertiesEditViewPath() {
        return propertiesEditViewPath;
    }

    public void setPropertiesEditViewPath(String propertiesEditViewPath) {
        this.propertiesEditViewPath = propertiesEditViewPath;
    }

    public boolean getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(boolean isRelease) {
        this.isRelease = isRelease;
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

    @XmlTransient
    public List<FacetEvent> getFacetEventList() {
        return facetEventList;
    }

    public void setFacetEventList(List<FacetEvent> facetEventList) {
        this.facetEventList = facetEventList;
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
        if (!(object instanceof Facet)) {
            return false;
        }
        Facet other = (Facet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.service.entity.Facet[ id=" + id + " ]";
    }

    @XmlTransient
    public List<ToyFacet> getToyFacetList() {
        return toyFacetList;
    }

    public void setToyFacetList(List<ToyFacet> toyFacetList) {
        this.toyFacetList = toyFacetList;
    }
    
}
