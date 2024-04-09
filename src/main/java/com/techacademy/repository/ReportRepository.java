package com.techacademy.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.techacademy.entity.Report;


public interface ReportRepository extends JpaRepositoryImplementation<Report, String> {
}