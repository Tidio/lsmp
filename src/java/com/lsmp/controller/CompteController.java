/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.controller;

import com.lsmp.manager.TypeUtilisateurDao;
import com.lsmp.manager.UtilisateurDao;
import com.lsmp.model.Utilisateur;
import com.lsmp.model.Infrastructure;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author TiDy
 */
@Controller
@SessionAttributes(value = "utilisateur", types = {Utilisateur.class})
public class CompteController {

    @Autowired
    TypeUtilisateurDao typeUtilisateurManager;

    @Autowired
    UtilisateurDao utilisateurManager;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {

        ModelAndView model = new ModelAndView();
        model.setViewName("/account/login");

        return model;

    }
    
    @RequestMapping(value = "/account/horray")
    public ModelAndView created() {

        ModelAndView model = new ModelAndView();
        model.setViewName("/account/horray");

        return model;

    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/logged")
    public ModelAndView logged(HttpSession session) {

        ModelAndView model = new ModelAndView();
        model.setViewName("/account/logged");
        System.out.println("======================= "+session+" ================================");
        return model;

    }

    @ModelAttribute("utilisateur")
    public Utilisateur addAttributes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Utilisateur currentUser = utilisateurManager.findByUserName(name);
        if (currentUser == null){
            currentUser = new Utilisateur();
        }
        return currentUser;
    }

    @RequestMapping(value = "/account/inscription", method = RequestMethod.GET)
    public ModelAndView inscriptionForm() {
        ModelAndView mv = new ModelAndView("account/inscription");
        mv.addObject("newUtilisateur", new Utilisateur());
        mv.addObject("typeUtilisateurs", typeUtilisateurManager.findAllTypeUtilisateur());
        return mv;
    }

    @RequestMapping(value = "/account/inscription", method = RequestMethod.POST)
    public String inscriptionSubmit(@ModelAttribute Utilisateur newUtilisateur) {
        String generatedSecuredPasswordHash = BCrypt.hashpw(newUtilisateur.getMdpUtilisateur(), BCrypt.gensalt(10));
        newUtilisateur.setMdpUtilisateur(generatedSecuredPasswordHash);
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
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/account/horray");
        return "redirect:/account/horray";
    }
}
