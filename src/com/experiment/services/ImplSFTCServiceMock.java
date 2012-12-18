package com.experiment.services;

import com.experiment.models.Entry;
import com.experiment.models.Matchup;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 9/20/12
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImplSFTCServiceMock implements ISFTCService {

    private final String OPPONENT = "Opponent";
    private final String RESULT = "Result";

    private final Random _random = new Random();

    @Override
    public List<Matchup> getMatchups(int year, int month, int day)
    {
        List<Matchup> matchups = new ArrayList<Matchup>();

        for(int i = 0; i < 15; i++)
        {
            matchups.add(getMatchup(year, month, day));
        }

        return matchups;
    }

    private Matchup getMatchup(int year, int month, int day)
    {
        Matchup m = new Matchup();
        m.setCategory(Matchup.Sport.NFL);
        m.setEntry1(getEntry(true));
        m.setEntry2(getEntry(false));
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(year, month, day));
        m.setDate(c);

        return m;
    }

    private Entry getEntry(boolean isWinner)
    {
        Entry e = new Entry();
        e.setOpponent(OPPONENT + " " + _random.nextInt());
        e.setPopularity(_random.nextInt(100) + "%");
        e.setResult(RESULT + " " + _random.nextInt());
        e.setWinner(isWinner);

        return e;
    }
}
