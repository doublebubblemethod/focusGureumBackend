package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitScheduleRepository;
import org.springframework.stereotype.Service;


@Service
public class HabitScheduleService {
    private final HabitScheduleRepository habitScheduleRepository;


    public HabitScheduleService(HabitScheduleRepository habitScheduleRepository) {
        this.habitScheduleRepository = habitScheduleRepository;
    }
    public HabitSchedule getByHabitId(Integer userId, Integer habitId) throws IllegalAccessException {
        HabitSchedule schedule = habitScheduleRepository.findByHabit_Id(habitId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if (!schedule.getHabit().getUser().getId().equals(userId)) {
            throw new IllegalAccessException("User not authorized to access this habit's schedule");
        }

        return schedule;
    }

    public void updateFrequencyType(Integer userId, Integer habitId,
                                    String frequencyType,
                                    String daysOfWeek,
                                    String dayStepStr) throws IllegalAccessException {

        HabitSchedule schedule = habitScheduleRepository.findByHabit_Id(habitId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getHabit().getUser().getId().equals(userId)) {
            throw new IllegalAccessException("Unauthorized");
        }

        String type = frequencyType.toLowerCase().trim();

        switch (type) {
            case "step":
                if (dayStepStr == null) {
                    throw new IllegalArgumentException("dayStep is required for 'step' frequency type.");
                }

                int dayStep;
                try {
                    dayStep = Integer.parseInt(dayStepStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("dayStep must be an integer.");
                }

                if (dayStep < 1 || dayStep > 31) {
                    throw new IllegalArgumentException("dayStep must be between 1 and 31.");
                }

                schedule.setFrequencyType("step");
                schedule.setDayStep(dayStep);
                schedule.setDaysOfWeek(null); // clear opposite
                break;

            case "week":
                if (daysOfWeek == null || !daysOfWeek.matches("^[1-7](,[1-7])*$")) {
                    throw new IllegalArgumentException("daysOfWeek must be a comma-separated list of digits 1–7 (e.g., '1,3,5').");
                }

                schedule.setFrequencyType("week");
                schedule.setDaysOfWeek(daysOfWeek);
                schedule.setDayStep(null); // clear opposite
                break;

            default:
                throw new IllegalArgumentException("frequencyType must be 'week' or 'step'.");
        }

        habitScheduleRepository.save(schedule);
    }
}
