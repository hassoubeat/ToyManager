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
@Table(name = "facet")
@NamedQueries({
    @NamedQuery(name = "Facet.findAll", query = "SELECT f FROM Facet f")
    , @NamedQuery(name = "Facet.findById", query = "SELECT f FROM Facet f WHERE f.id = :id")
    , @NamedQuery(name = "Facet.findByName", query = "SELECT f FROM Facet f WHERE f.name = :name")
    , @NamedQuery(name = "Facet.findByNote", query = "SELECT f FROM Facet f WHERE f.note = :note")
    , @NamedQuery(name = "Facet.findByRequirement", query = "SELECT f FROM Facet f WHERE f.requirement = :requirement")
    , @NamedQuery(name = "Facet.findByCreateDate", query = "SELECT f FROM Facet f WHERE f.createDate = :createDate")
    , @NamedQuery(name = "Facet.findByEditDate", query = "SELECT f FROM Facet f WHERE f.editDate = :editDate")})
public class Facet implements Serializable {

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
    @Size(max = 600)
    @Column(name = "requirement")
    private String requirement;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facetId")
    private List<ToyFacet> toyFacetList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facetId")
    private List<FacetProgram> facetProgramList;
    @JoinColumn(name = "facet_event_id", referencedColumnName = "id")
    @ManyToOne
    private FacetEvent facetEventId;
    @JoinColumn(name = "facet_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FacetType facetTypeId;
    @JoinColumn(name = "facet_program_id", referencedColumnName = "id")
    @ManyToOne
    private FacetProgram facetProgramId;

    public Facet() {
    }

    public Facet(Integer id) {
        this.id = id;
    }

    public Facet(Integer id, String name, Date createDate, Date editDate) {
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

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
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

    public List<FacetEvent> getFacetEventList() {
        return facetEventList;
    }

    public void setFacetEventList(List<FacetEvent> facetEventList) {
        this.facetEventList = facetEventList;
    }

    public List<ToyFacet> getToyFacetList() {
        return toyFacetList;
    }

    public void setToyFacetList(List<ToyFacet> toyFacetList) {
        this.toyFacetList = toyFacetList;
    }

    public List<FacetProgram> getFacetProgramList() {
        return facetProgramList;
    }

    public void setFacetProgramList(List<FacetProgram> facetProgramList) {
        this.facetProgramList = facetProgramList;
    }

    public FacetEvent getFacetEventId() {
        return facetEventId;
    }

    public void setFacetEventId(FacetEvent facetEventId) {
        this.facetEventId = facetEventId;
    }

    public FacetType getFacetTypeId() {
        return facetTypeId;
    }

    public void setFacetTypeId(FacetType facetTypeId) {
        this.facetTypeId = facetTypeId;
    }

    public FacetProgram getFacetProgramId() {
        return facetProgramId;
    }

    public void setFacetProgramId(FacetProgram facetProgramId) {
        this.facetProgramId = facetProgramId;
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
    
}
