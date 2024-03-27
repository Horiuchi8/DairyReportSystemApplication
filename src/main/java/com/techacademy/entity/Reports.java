package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Reports {

    // ID
    @Id
    @Column
    @NotEmpty
    @Length
    private int id;

    //日付
    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    //タイトル
    @Column(length = 100, nullable = false)
    @NotEmpty
    @Length(max = 100)
    private String title;

    //内容
    @Column(columnDefinition="LONGTEXT", nullable = false)
    @NotEmpty
    private String content;

    // 社員番号
    @Id
    @Column(length = 10)
    @NotEmpty
    @Length(max = 10)
    private String employee_code;

    // 削除フラグ(論理削除を行う)
    @Column(columnDefinition="TINYINT", nullable = true)
    private boolean delete_flg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime created_at;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updated_at;

}
