package com.lsmp.model;
// Generated 20 mars 2015 12:07:56 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TypeUtilisateur generated by hbm2java
 */
@Entity
@Table(name="type_utilisateur"
    ,catalog="lsmp"
)
public class TypeUtilisateur  implements java.io.Serializable {


     private Integer idTypeUtilisateur;
     private String libelle;
     private String role;
     private Set<Utilisateur> utilisateurs = new HashSet<Utilisateur>(0);

    public TypeUtilisateur() {
    }

	
    public TypeUtilisateur(String libelle) {
        this.libelle = libelle;
    }
    public TypeUtilisateur(String libelle, Set<Utilisateur> utilisateurs) {
       this.libelle = libelle;
       this.utilisateurs = utilisateurs;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id_type_utilisateur", unique=true, nullable=false)
    public Integer getIdTypeUtilisateur() {
        return this.idTypeUtilisateur;
    }
    
    public void setIdTypeUtilisateur(Integer idTypeUtilisateur) {
        this.idTypeUtilisateur = idTypeUtilisateur;
    }

    
    @Column(name="libelle", nullable=false, length=50)
    public String getLibelle() {
        return this.libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="typeUtilisateur")
    public Set<Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }
    
    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Column(name="role", nullable=false, length=30)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }




}


