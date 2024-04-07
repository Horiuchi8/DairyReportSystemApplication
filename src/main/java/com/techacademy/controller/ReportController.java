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
    public String add(@Validated Report report, Model model,BindingResult res,@AuthenticationPrincipal UserDetail userDetail) {
        model.addAttribute("employee", userDetail.getEmployee());
        report.setEmployee(userDetail.getEmployee());

        // 入力チェック
        if (res.hasErrors()) {
            return create(userDetail, report, model);
        }
        ErrorKinds result = reportService.saveReport(report);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return create(userDetail, report, model);
        }

//        reportService.saveReport(report);

        return "redirect:/reports";
    }

    // 日報詳細画面
    @GetMapping(value = "/{code}/")
    public String detail(@PathVariable String code, Model model) {

        model.addAttribute("report", reportService.getReport(code));
        return "reports/detail";
    }

    //日報更新画面
    @GetMapping(value = "/{code}/update")
    public String edit(@PathVariable String code, Model model,@ModelAttribute Report report) {


        if(code != null) {
        model.addAttribute("report", reportService.getReport(code));
        }

        return "reports/edit";
    }

    //日報更新処理
    @PostMapping(value = "/{code}/update")
    public String update(@PathVariable String code, @Validated @ModelAttribute Report report, BindingResult res, Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return edit(null, model, report);
        }

        //サービスにあるupdateSaveのreportに登録される内容をresultに格納
        ErrorKinds result = reportService.updateSave(report, code);
        //エラーが含まれるならエラーメッセージと共に日報更新画面に戻る
        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return edit(code, model, report);
    }
        //更新ができたら一覧にリダイレクト
        return "redirect:/reports";
    }

    //日報削除処理
    @PostMapping(value = "/{code}/delete")
    public String delete(@PathVariable String code, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        reportService.delete(code, userDetail);

        return "redirect:/reports";
    }
}