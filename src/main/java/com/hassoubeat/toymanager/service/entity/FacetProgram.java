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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "facet_program")
@NamedQueries({
    @NamedQuery(name = "FacetProgram.findAll", query = "SELECT f FROM FacetProgram f")
    , @NamedQuery(name = "FacetProgram.findById", query = "SELECT f FROM FacetProgram f WHERE f.id = :id")
    , @NamedQuery(name = "FacetProgram.findByName", query = "SELECT f FROM FacetProgram f WHERE f.name = :name")
    , @NamedQuery(name = "FacetProgram.findByNote", query = "SELECT f FROM FacetProgram f WHERE f.note = :note")
    , @NamedQuery(name = "FacetProgram.findByProgramUrl", query = "SELECT f FROM FacetProgram f WHERE f.programUrl = :programUrl")
    , @NamedQuery(name = "FacetProgram.findByCreateDate", query = "SELECT f FROM FacetProgram f WHERE f.createDate = :createDate")
    , @NamedQuery(name = "FacetProgram.findByEditDate", query = "SELECT f FROM FacetProgram f WHERE f.editDate = :editDate")})
public class FacetProgram implements Serializable {

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
    @Size(max = 400)
    @Column(name = "note")
    private String note;
    @Size(max = 300)
    @Column(name = "program_url")
    private String programUrl;
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
    @JoinColumn(name = "facet_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Facet facetId;
    @OneToMany(mappedBy = "facetProgramId")
    private List<Facet> facetList;

    public FacetProgram() {
    }

    public FacetProgram(Integer id) {
        this.id = id;
    }

    public FacetProgram(Integer id, String name, Date createDate, Date editDate) {
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

    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
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

    public Facet getFacetId() {
        return facetId;
    }

    public void setFacetId(Facet facetId) {
        this.facetId = facetId;
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
        if (!(object instanceof FacetProgram)) {
            return false;
        }
        FacetProgram other = (FacetProgram) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hassoubeat.toymanager.web.backingbean.admin.FacetProgram[ id=" + id + " ]";
    }
    
}
