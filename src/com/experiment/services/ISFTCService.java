package com.experiment.services;

import com.experiment.models.Matchup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 9/20/12
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISFTCService {

    public List<Matchup> getMatchups(int year, int month, int day);
}
