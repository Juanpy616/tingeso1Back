package com.tgs.JPkarts.repositories;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, Long> {
    AnalyticsEntity findByMonthAndYear(Month month, int year);
}

