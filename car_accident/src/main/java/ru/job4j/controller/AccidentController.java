package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.model.Accident;
import ru.job4j.service.AccidentService;

@Controller
public class AccidentController {

    private final AccidentService accidentService;

    public AccidentController(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/create")
    public String create() {
        return "accident/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident) {
        accidentService.saveAccident(accident);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String getAccidentToUpdate(@RequestParam("id") int accidentId, Model model) {
        accidentService.getAccident(accidentId).ifPresentOrElse(
                acc -> model.addAttribute("accident", acc),
                () -> model.addAttribute("error", "Accident not found.")
        );
        return "accident/edit";
    }


    @PostMapping("/edit")
    public String edit(@ModelAttribute("accident") Accident accident) {
        accidentService.updateAccident(accident);
        return "redirect:/";
    }

}