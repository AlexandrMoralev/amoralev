package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.controller.dto.AccidentDto;
import ru.job4j.model.AccidentType;
import ru.job4j.model.Rule;
import ru.job4j.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Controller
public class AccidentController {

    private final AccidentService accidentService;

    // TODO cache this
    private final static List<AccidentType> ACCIDENT_TYPES = List.of(
            AccidentType.of(1, "Две машины"),
            AccidentType.of(2, "Машина и человек"),
            AccidentType.of(3, "Машина и велосипед")
    );

    private final static List<Rule> ACCIDENT_RULES = List.of(
            Rule.of(1, "Статья. 1"),
            Rule.of(2, "Статья. 2"),
            Rule.of(3, "Статья. 3")
    );

    private final static String[] EMPTY = new String[]{};

    public AccidentController(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", ACCIDENT_TYPES);
        model.addAttribute("rules", ACCIDENT_RULES);
        return "accident/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AccidentDto accident, HttpServletRequest req) {
        String[] ids = ofNullable(req.getParameterValues("rIds")).orElse(EMPTY);
        accidentService.saveAccident(accident.toEntity(ACCIDENT_TYPES, getRules(ids)));
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String getAccidentToUpdate(@RequestParam("id") int accidentId, Model model) {
        accidentService.getAccident(accidentId).ifPresentOrElse(
                acc -> {
                    model.addAttribute("accident", acc);
                    model.addAttribute("rIds", acc.getRules().stream().map(Rule::getId).collect(Collectors.toList()));
                },
                () -> model.addAttribute("error", "Accident not found.")
        );
        return "accident/edit";
    }


    @PostMapping("/edit")
    public String edit(@ModelAttribute("accident") AccidentDto accident, HttpServletRequest req) {
        String[] ids = getRuleIds(req);
        accidentService.updateAccident(accident.toEntity(ACCIDENT_TYPES, getRules(ids)));
        return "redirect:/";
    }

    // TODO cleanup while making accident rules updatable
    private String[] getRuleIds(HttpServletRequest req) {
        return ofNullable(req.getParameter("rIds"))
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::strip)
                        .map(v -> v.replaceAll("\\[", "").replaceAll("\\]", ""))
                        .collect(Collectors.toList())
                        .toArray(new String[]{})
                )
                .orElse(EMPTY);
    }

    private List<Rule> getRules(String[] ids) {
        return Arrays.stream(ids)
                .map(Integer::valueOf)
                .filter(v -> v >= 0 && v < ACCIDENT_RULES.size())
                .flatMap(id -> ACCIDENT_RULES.stream().filter(r -> id.equals(r.getId())).findFirst().stream())
                .collect(Collectors.toList());
    }

}