package com.codecool.grannymanager.service;

import com.codecool.grannymanager.model.Granny;
import com.codecool.grannymanager.model.grannyproperties.Environment;
import com.codecool.grannymanager.model.grannyproperties.Health;
import com.codecool.grannymanager.model.grannyproperties.Mood;
import com.codecool.grannymanager.model.grannyproperties.Stat;
import com.codecool.grannymanager.repository.GrannyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Service
public class GrannyService {

    private final Random random = new Random();
    private final GrannyRepository grannyRepository;

    @Autowired
    public GrannyService(GrannyRepository grannyRepository) {
        this.grannyRepository = grannyRepository;
    }

    public void createGranny(int userId, String name) {
        grannyRepository.createGranny(userId, name);
    }

    public Granny visitGranny(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        checkOnGranny(granny);
        granny.setLastVisit(LocalDateTime.now());

        return granny;
    }

    private void checkOnGranny(Granny granny) {
        decrementGrannyStatByNotVisitedDays(granny);
        shouldGrannyRetire(granny);
    }

    private void decrementGrannyStatByNotVisitedDays(Granny granny) {
        LocalDateTime lastVisit = granny.getLastVisit();
        int daysSinceLastVisit = extractDaysSinceLastVisit(lastVisit);

        while (daysSinceLastVisit > 0) {
            daysSinceLastVisit--;
            decrementRandomStat(granny);
        }
    }

    private int extractDaysSinceLastVisit(LocalDateTime lastVisit) {
        int hoursSinceLastVisit = extractHoursSinceLastVisit(lastVisit);
        return Math.floorDiv(hoursSinceLastVisit, 24);
    }

    private int extractHoursSinceLastVisit(LocalDateTime lastVisit) {
        return (int) ChronoUnit.HOURS.between(lastVisit, LocalDateTime.now());
    }

    private void decrementRandomStat(Granny granny) {
        Consumer<Granny> healthDecrease = this::decrementHealthStat;
        Consumer<Granny> moodDecrease = this::decrementMoodStat;
        Consumer<Granny> environmentDecrease = this::decrementEnvironmentStat;
        Consumer<Granny>[] events = new Consumer[]{healthDecrease, moodDecrease, environmentDecrease};
        Consumer<Granny> event = events[random.nextInt(events.length)];
        event.accept(granny);
    }
    private void decrementHealthStat(Granny granny){
        Stat health = granny.getHealth();
        Stat decrementedStat = health.decrementStat();
        granny.setHealth(decrementedStat);
    }
    private void decrementMoodStat(Granny granny){
        Stat mood = granny.getMood();
        Stat decrementedStat = mood.decrementStat();
        granny.setMood(decrementedStat);
    }
    private void decrementEnvironmentStat(Granny granny){
        Stat environment = granny.getEnvironment();
        Stat decrementedStat = environment.decrementStat();
        granny.setEnvironment(decrementedStat);
    }

    private void shouldGrannyRetire(Granny granny) {
        if (checkGrannyStatsIfLowest(granny)) {
            granny.setRetired(true);
        }
    }

    private boolean checkGrannyStatsIfLowest(Granny granny) {
        Stat mood = granny.getMood();
        Stat health = granny.getHealth();
        Stat environment = granny.getEnvironment();
        return mood == Mood.GRUMPY && health == Health.SICK && environment == Environment.IN_RUINS;
    }

    public Granny feedPie(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        Stat health = granny.getHealth();
        Stat changedStat = health.incrementStat();
        granny.setHealth(changedStat);

        return granny;
    }

    public Granny playMahjong(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        Stat mood = granny.getMood();
        Stat changedStat = mood.incrementStat();
        granny.setMood(changedStat);

        return granny;
    }

    public Granny cleanHouse(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        Stat environment = granny.getEnvironment();
        Stat changedStat = environment.incrementStat();
        granny.setEnvironment(changedStat);

        return granny;
    }

    public void jumpOneDay(int id){
        Granny granny = grannyRepository.findGrannyById(id);
        LocalDateTime lastVisit = granny.getLastVisit();
        granny.setLastVisit(lastVisit.minusDays(1));

    }

}
