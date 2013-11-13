package net.konfuzo.tournament;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User: nodog
 * Date: 2013-10-28
 * Time: 23:10
 */
public class Round {
    private int roundNumber;
    private int nMatches;
    private ArrayList<Thread> matchThreads;
    private static Logger logger = LogManager.getLogger();
    int[] winnersArray;
    ArrayList<Player> players;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
        logger.info("Round " + roundNumber + " initialized.");
        matchThreads = new ArrayList<Thread>();
    }

    public void initializeMatches(ArrayList<Player> players) {
        logger.info("Initializing matchThreads for round " + roundNumber + ".");
        this.players = players;
        nMatches = players.size()/2;
        // winners all init-ed to -1
        winnersArray = new int[nMatches];
        for (int i = 0; i < nMatches; i++) {
            winnersArray[i] = -1;
        }
        // set up a round robin position array
        int[] positions = new int[players.size()];
        // one player always stays in the same position
        positions[0] = 0;
        // all the others rotate through the rest of the positions each round.
        for (int i = 1; i < positions.length; i++) {
            positions[i] = (i + roundNumber) % (players.size() - 1) + 1;
        }
        // make matchThreads according to position array
        for (int i = 0; i < nMatches; i++) {
            //logger.info("match number " + i);
            Player player1 = players.get(positions[i]);
            Player player2 = players.get(positions[players.size() - 1 - i]);
            matchThreads.add(new Thread(new Match(player1, player2, winnersArray, i)));
        }
    }

    public void playMatches() {
        logger.info("Playing matchThreads.");
        for(Thread aMatchThread : matchThreads) {
            aMatchThread.start();
        }
    }

    public Set<Player> reportWinners() {
        logger.info("Reporting winners.");
        Set<Player> winners = new HashSet<Player>();
        int totalWinners = 0;

        // repeat infinitely until all matchThreads report a winner
        while (totalWinners < nMatches) {
            totalWinners = 0;
            for (int i = 0; i < nMatches; i++) {
                if (winnersArray[i] != -1) {
                    totalWinners++;
                }
                logger.info("Round totalWinners = " + totalWinners);
            }
            // sleep 500 msec and try again.
            try {
                Thread.sleep(500l);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        for (int i = 0; i < nMatches; i++) {
            logger.info("Round adding player " + players.get(winnersArray[i]).getPlayerNumber() + " as a winner.");
            winners.add(players.get(winnersArray[i]));
        }
        return winners;
    }
}
