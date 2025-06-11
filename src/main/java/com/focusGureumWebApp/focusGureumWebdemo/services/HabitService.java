package com.focusGureumWebApp.focusGureumWebdemo.services;
import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitRequest;
import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitResponse;
import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.repository.AppUserRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitScheduleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final AppUserRepository appUserRepository;
    public HabitService(HabitRepository habitRepository, AppUserRepository appUserRepository) {
        this.habitRepository = habitRepository;
        this.appUserRepository = appUserRepository;
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
    /*
    I need a separate function that sends only active logs
     */
    public List<HabitResponse> findActiveHabitsByNickname(String nickname) {
        List<Habit> activeHabits = habitRepository.findByUserNicknameAndActive(nickname, true);

        return activeHabits.stream()
                .map(habit -> new HabitResponse(
                        habit.getId(),
                        habit.getName(),
                        habit.getCreatedAt(),
                        habit.isActive(),
                        habit.getUser().getId()
                ))
                .collect(Collectors.toList());
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
    public Habit createHabit(HabitRequest habitRequest, String nickname) {
        // Find user by nickname
        AppUser user = appUserRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Habit habit = new Habit();
        habit.setName(habitRequest.getName());
        habit.setCreatedAt(habitRequest.getCreatedAt() != null ? habitRequest.getCreatedAt() : LocalDateTime.now());
        habit.setActive(habitRequest.isActive());
        habit.setUser(user);

        // Save habit and return
        return habitRepository.save(habit);
    }

}
