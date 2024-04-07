package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報一覧表示処理(全件検索)
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索して返す
    public Report getReport(String employee_code) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(employee_code);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // 日報保存
    @Transactional
    public ErrorKinds saveReport(Report report) {

        //Reportテーブルにそれぞれの値（従業員テーブルの情報、削除フラグの設定、作成日時、更新日時）をセット
        report.setEmployee(report.getEmployee());
        report.setDelete_flg(false);
        LocalDateTime now = LocalDateTime.now();
        report.setCreated_at(now);
        report.setUpdated_at(now);

        //reportリポジトリ―に内容を保存
        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新保存
    @Transactional
    public ErrorKinds updateSave(Report report, String code) {
        //reportリポジトリ―にあるcodeを検索して内容をupdateEmpに格納
        Report updateEmp = reportRepository.findById(code).get();

        //updateEmpに入力フォームのそれぞれの値(日付、タイトル、内容、更新日時)をセット
        updateEmp.setReportDate(report.getReportDate());
        updateEmp.setTitle(report.getTitle());
        updateEmp.setContent(report.getContent());
        LocalDateTime now = LocalDateTime.now();
        updateEmp.setUpdated_at(now);

        //reportリポジトリ―に内容を保存
        reportRepository.save(updateEmp);

        return ErrorKinds.SUCCESS;

}
    // 日報削除
    @Transactional
    public ErrorKinds delete(String code, UserDetail userDetail) {

        // 自分を削除しようとした場合はエラーメッセージを表示
        if (code.equals(userDetail.getEmployee().getCode())) {
            return ErrorKinds.LOGINCHECK_ERROR;
        }

        Report report = getReport(code);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdated_at(now);
        report.setDelete_flg(true);

        return ErrorKinds.SUCCESS;
    }
}