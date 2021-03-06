package com.lsmp.model;
// Generated 20 mars 2015 12:07:56 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Infrastructure generated by hbm2java
 */
@Entity
@Table(name="infrastructure"
    ,catalog="lsmp"
)
public class Infrastructure  implements java.io.Serializable {


     private Integer idInfrastructure;
     private String libelleInfrastructure;
     private String descriptionInfrastructure;
     private Date dateCreationInfrastructure;
     private Set<Mission> missions = new HashSet<Mission>(0);

    public Infrastructure() {
    }

	
    public Infrastructure(String libelleInfrastructure, String descriptionInfrastructure, Date dateCreationInfrastructure) {
        this.libelleInfrastructure = libelleInfrastructure;
        this.descriptionInfrastructure = descriptionInfrastructure;
        this.dateCreationInfrastructure = dateCreationInfrastructure;
    }
    public Infrastructure(String libelleInfrastructure, String descriptionInfrastructure, Date dateCreationInfrastructure, Set<Mission> missions) {
       this.libelleInfrastructure = libelleInfrastructure;
       this.descriptionInfrastructure = descriptionInfrastructure;
       this.dateCreationInfrastructure = dateCreationInfrastructure;
       this.missions = missions;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id_infrastructure", unique=true, nullable=false)
    public Integer getIdInfrastructure() {
        return this.idInfrastructure;
    }
    
    public void setIdInfrastructure(Integer idInfrastructure) {
        this.idInfrastructure = idInfrastructure;
    }

    
    @Column(name="libelle_infrastructure", nullable=false, length=50)
    public String getLibelleInfrastructure() {
        return this.libelleInfrastructure;
    }
    
    public void setLibelleInfrastructure(String libelleInfrastructure) {
        this.libelleInfrastructure = libelleInfrastructure;
    }

    
    @Column(name="description_infrastructure", nullable=false)
    public String getDescriptionInfrastructure() {
        return this.descriptionInfrastructure;
    }
    
    public void setDescriptionInfrastructure(String descriptionInfrastructure) {
        this.descriptionInfrastructure = descriptionInfrastructure;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_creation_infrastructure", nullable=false, length=19)
    public Date getDateCreationInfrastructure() {
        return this.dateCreationInfrastructure;
    }
    
    public void setDateCreationInfrastructure(Date dateCreationInfrastructure) {
        this.dateCreationInfrastructure = dateCreationInfrastructure;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="infrastructure_mission", catalog="lsmp", joinColumns = { 
        @JoinColumn(name="infrastructure_id", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="mission_id", nullable=false, updatable=false) })
    public Set<Mission> getMissions() {
        return this.missions;
    }
    
    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }




}


