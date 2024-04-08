package com.techacademy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

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
    public String create(@AuthenticationPrincipal UserDetail userDetail,Report report, Model model) {
//        System.out.println(userDetails.getEmployee().getName());
        model.addAttribute("username", userDetail.getEmployee().getName());
        model.addAttribute("employee", userDetail.getEmployee());
        return "reports/new";
    }

    //日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, Model model,@AuthenticationPrincipal UserDetail userDetail) {

        // 入力チェック
        if (res.hasErrors()) {
            return create(userDetail, report, model);
        }

        model.addAttribute("employee", userDetail.getEmployee());
        report.setEmployee(userDetail.getEmployee());

        reportService.saveReport(report);

        return "redirect:/reports";
    }

    // 日報詳細画面
    @GetMapping(value = "/{ID}/")
    public String detail(@PathVariable String ID, Model model) {

        model.addAttribute("report", reportService.getReport(ID));
        return "reports/detail";
    }

    //日報更新画面
    @GetMapping(value = "/{ID}/update")
    public String edit(@PathVariable String ID, Model model, Report report) {

        //codeがnullじゃない場合は詳細画面から遷移しているので、ModelにはserviceのIDをセットする。
        if(ID != null) {
        model.addAttribute("report", reportService.getReport(ID));
        }

        return "reports/edit";
    }

    //日報更新処理
    @PostMapping(value = "/{ID}/update")
    public String update(@PathVariable String ID, @Validated Report report, BindingResult res, Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return edit(null, model, report);
        }

        reportService.updateSave(report, ID);

        //更新ができたら一覧にリダイレクト
        return "redirect:/reports";
    }

    //日報削除処理
    @PostMapping(value = "/{ID}/delete")
    public String delete(@PathVariable String ID, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        reportService.delete(ID, userDetail);

        return "redirect:/reports";
    }
}