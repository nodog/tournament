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
    private ArrayList<Match> matches;
    private static Logger logger = LogManager.getLogger();

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
        logger.info("Round " + roundNumber + " initialized.");
        matches = new ArrayList<Match>();
    }

    public void initializeMatches(ArrayList<Player> players) {
        logger.info("Initializing matches for round " + roundNumber + ".");
        nMatches = players.size()/2;
        // set up a round robin position array
        int[] positions = new int[players.size()];
        // one player always stays in the same position
        positions[0] = 0;
        // all the others rotate through the rest of the positions each round.
        for (int i = 1; i < positions.length; i++) {
            positions[i] = (i + roundNumber) % (players.size() - 1) + 1;
        }
        // make matches according to position array
        for (int i = 0; i < nMatches; i++) {
            //logger.info("match number " + i);
            Player player1 = players.get(positions[i]);
            Player player2 = players.get(positions[players.size() - 1 - i]);
            matches.add(new Match(player1, player2));
        }
    }

    public void playMatches() {
        logger.info("Playing matches.");
        for(Match aMatch : matches) {
            aMatch.play();
        }
    }

    public Set<Player> reportWinners() {
        logger.info("Reporting winners.");
        Set<Player> winners = new HashSet<Player>();
        for (Match aMatch : matches) {
            winners.add(aMatch.reportWinner());
        }
        return winners;
    }
}
