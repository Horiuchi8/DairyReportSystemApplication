package com.techacademy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reports")
public class EmmployeeReports {

    @GetMapping()
    public String getIndex() {
        // list.htmlに画面遷移
        return "reports/list";
    }
}