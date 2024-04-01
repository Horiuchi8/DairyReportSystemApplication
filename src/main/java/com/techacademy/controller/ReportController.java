package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    //日報一覧画面
    @GetMapping()
    public String getlist(Model model) {

        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());
        // list.htmlに画面遷移
        return "reports/list";

    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String add(@ModelAttribute Report code) {

        return "reports/new";
    }

    // 日報詳細画面
    @GetMapping(value = "/{code}/")
    public String detail(@PathVariable String code, Model model) {

        model.addAttribute("report", reportService.getReport(code));
        return "reports/detail";
    }

    //従業員更新画面
    @GetMapping(value = "/{code}/update")
    public String edit(@PathVariable String code, Model model, Employee employee) {

        if(code != null) {
        model.addAttribute("employee", reportService.getReport(code));
        }

        return "reports/edit";
    }
}