/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author TiDy
 */
public class GeneralManager {
    
    Session session;
    
    public GeneralManager(){
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }
    
}
