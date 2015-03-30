/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.controller;

import com.lsmp.manager.TypeUtilisateurManager;
import com.lsmp.manager.UtilisateurManager;
import com.lsmp.model.TypeUtilisateur;
import com.lsmp.model.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author TiDy
 */
@Controller
public class CompteController {

    @RequestMapping(value = "/account/inscription", method = RequestMethod.GET)
    public ModelAndView inscriptionForm() {
        ModelAndView mv = new ModelAndView("account/inscription");
        TypeUtilisateurManager typeUtilisateurManager = new TypeUtilisateurManager();
        mv.addObject("newUtilisateur", new Utilisateur());
        mv.addObject("typeUtilisateurs", typeUtilisateurManager.findAllUsers());
        return mv;
    }

    @RequestMapping(value = "/account/inscription", method = RequestMethod.POST)
    public ModelAndView inscriptionSubmit(@ModelAttribute Utilisateur newUtilisateur) {
        UtilisateurManager utilisateurManager = new UtilisateurManager();
        System.out.println(newUtilisateur.getIdentifiantUtilisateur());
        System.out.println(newUtilisateur.getMailUtilisateur());
        System.out.println(newUtilisateur.getPrenomUtilisateur());
        System.out.println(newUtilisateur.getNomUtilisateur());
        System.out.println(newUtilisateur.getTypeUtilisateur());

//        TypeUtilisateurManager typeUtilisateurManager = new TypeUtilisateurManager();
//        TypeUtilisateur typeUtilisateur = typeUtilisateurManager.find(Integer.parseInt(newUtilisateur.getTypeUtilisateur().toString()));
//        System.out.println(typeUtilisateur.getIdTypeUtilisateur());
//        System.out.println(typeUtilisateur.getLibelle());
//        newUtilisateur.setTypeUtilisateur(typeUtilisateur);
        utilisateurManager.saveUtilisateur(newUtilisateur);
        ModelAndView mv = new ModelAndView("account/created");
        return mv;
    }
}
