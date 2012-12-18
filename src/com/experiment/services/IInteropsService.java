package com.experiment.services;

import com.experiment.models.Line;
import com.experiment.models.Matchup;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/10/12
 * Time: 8:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IInteropsService {

//    ID	 Name
//    1	 American Football
//    3	 Athletics Track and Field
//    4	 Baseball
//    5	 Basketball
//    6	 Boxing
//    7	 Casino Special
//    8	 Cricket
//    10	 Cycling
//    9	 Entertainment
//    13	 Golf
//    14	 Handball
//    15	 Horse Racing
//    16	 Ice Hockey
//    11	 Motorsports
//    17	 Non Sporting
//    18	 Other Sports
//    19	 Poker Bets
//    20	 Politics
//    21	 Rugby
//    22	 Skiing
//    23	 Snooker
//    12	 Soccer
//    30	 Stock Market
//    24	 Strike it Rich
//    27	 Tabletennis
//    26	 Tennis
//    25	 Tennis Ladies
//    2	 US Major League Soccer
//    28	 Volleyball
//    29	 Weatherbetting

           Line getLine(Matchup matchup);
}
