package com.example.ElectionP.controller;

import com.example.ElectionP.entity.Candidature;
import com.example.ElectionP.service.CandidatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CandidatureController {

    @Autowired
    private CandidatureService candidatureService;

    @GetMapping("/candidature/all")
    public String viewAllCandidatures(Model model) {
        List<Candidature> candidatures = candidatureService.getAllCandidatures();
        model.addAttribute("candidatures", candidatures);
        return "candidature_list";
    }

    @PostMapping("/candidature/add")
    public String addCandidature(@RequestParam long userId, Model model) {
        try {
            candidatureService.addCandidature(userId);
            model.addAttribute("message", "Candidacy submitted successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/candidature/all";
    }

    @PostMapping("/candidature/delete")
    public String deleteCandidature(@RequestParam long userId, Model model) {
        try {
            candidatureService.deleteCandidature(userId);
            model.addAttribute("message", "Candidacy withdrawn successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/candidature/all";
    }
}