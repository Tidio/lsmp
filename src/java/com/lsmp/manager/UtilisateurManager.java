/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.Utilisateur;
import java.util.List;

/**
 *
 * @author TiDy
 */
public class UtilisateurManager extends GeneralManager{
    
    public List <Utilisateur> findAllUsers(){
        List<Utilisateur> result = null;
        try {
            session.beginTransaction();
            result = session.createQuery("from Utilisateur").list();
            session.getTransaction().commit();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
}
