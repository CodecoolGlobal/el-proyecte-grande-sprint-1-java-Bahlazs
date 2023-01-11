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

@Service
public class GrannyService {

    private final Random random = new Random();
    private final GrannyRepository grannyRepository;

    @Autowired
    public GrannyService(GrannyRepository grannyRepository) {
        this.grannyRepository = grannyRepository;
    }

    public void visitGranny(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        checkOnGranny(granny);
        granny.setLastVisit(LocalDateTime.now());
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
        Stat randomStat = getRandomStatFromGranny(granny);
        randomStat.decrementStat();
    }
    private Stat getRandomStatFromGranny(Granny granny){
        List<Stat> grannyStats = granny.getStats();
        return grannyStats.get(random.nextInt(3));
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

    public void feedPie(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        Stat health = granny.getHealth();
        health.incrementStat();
    }

    public void playMahjong(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        Stat mood = granny.getMood();
        mood.incrementStat();
    }

    public void cleanHouse(int id) {
        Granny granny = grannyRepository.findGrannyById(id);
        Stat environment = granny.getEnvironment();
        environment.incrementStat();
    }

}
