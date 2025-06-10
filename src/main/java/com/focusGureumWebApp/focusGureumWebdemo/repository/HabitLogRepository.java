package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.dto.DailyHabitLogCount;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Integer> {
    List<HabitLog> findAllByHabit(Habit habit);
    List<HabitLog> findByHabitAndDate(Habit habit, LocalDate date);
    List<HabitLog> findByHabitAndDateBetween(Habit habit, LocalDate startDate, LocalDate endDate);
    @Query(value = """
        SELECT hl.date AS logDate, COUNT(*) AS habitCount
        FROM habit_log hl
        JOIN habit h ON hl.habit_id = h.id
        JOIN app_user u ON h.user_id = u.id
        WHERE u.nickname = :nickname
          AND hl.date BETWEEN :startDate AND :endDate
        GROUP BY hl.date
        ORDER BY hl.date
        """, nativeQuery = true)
    List<DailyHabitLogCount> countCheckedHabitsWithinFrame(
            @Param("nickname") String nickname,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
/*
SELECT hl.date AS log_date, COUNT(*) AS habit_count FROM habit_log hl
JOIN habit h ON hl.habit_id = h.id
WHERE h.user_id = 1 AND hl.date BETWEEN '2025-03-01' AND '2025-03-31' GROUP BY hl.date ORDER BY hl.date;
 */