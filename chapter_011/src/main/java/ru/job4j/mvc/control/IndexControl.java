package ru.job4j.mvc.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.mvc.service.AccidentService;

@Controller
@ComponentScan("ru.job4j.mvc")
public class IndexControl {

    private final AccidentService accidentService;

    @Autowired
    public IndexControl(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("accidents", accidentService.getAccidents());
        return "index";
    }
}
