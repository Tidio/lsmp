/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.TypeUtilisateur;
import java.util.List;
import org.hibernate.Hibernate;

/**
 *
 * @author TiDy
 */
public class TypeUtilisateurManager extends GeneralManager {

    public List<TypeUtilisateur> findAllUsers() {
        List<TypeUtilisateur> result = null;
        try {
            session.beginTransaction();
            result = session.createQuery("from TypeUtilisateur").list();
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public TypeUtilisateur find(int id){
        TypeUtilisateur result = null;
        try {
            //session.beginTransaction();
            result = (TypeUtilisateur)session.get(TypeUtilisateur.class, id);
            Hibernate.initialize(result);
            //session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

}
