package com.focusGureumWebApp.focusGureumWebdemo.services;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitScheduleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
    /*
    function that finds a habit and checks if the user is authorised
     */
    public Habit getHabitForUser(Integer habitId, String nickname) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (!habit.getUser().getNickname().equals(nickname)) {
            throw new AccessDeniedException("User not authorized to access this habit");
        }

        return habit;
    }

    public List<Habit> findByUserNickname(String nickname) {
        return habitRepository.findByUserNickname(nickname);
    }

    public Habit getById(Integer id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
    }
    public void deleteHabit(Habit habit) {
        //jpa must delete related habit schedule and logs
        //@OneToMany(mappedBy = "habit", cascade = CascadeType.REMOVE, orphanRemoval = true)
        habitRepository.delete(habit);
    }
    public Habit renameHabit(Habit habit, String newName) {
        habit.setName(newName);
        return habitRepository.save(habit);
    }
    public void toggleHabit(Habit habit) {
        habit.setActive(!habit.isActive());
        habitRepository.save(habit);
    }
}
