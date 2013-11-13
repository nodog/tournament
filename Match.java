package net.konfuzo.tournament;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: nodog
 * Date: 2013-10-28
 * Time: 23:25
 */
public class Match implements Runnable {

    private Player player1;
    private Player player2;
    private Player winner;
    private static Logger logger = LogManager.getLogger();
    private int[] winnersArray;
    private int matchNumber;


    public Match(Player player1, Player player2, int[] winnersArray, int matchNumber) {
        this.player1 = player1;
        this.player2 = player2;
        this.winnersArray = winnersArray;
        this.matchNumber = matchNumber;
        logger.info("New match number " + matchNumber + " created between player " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber());
    }

    public void play() {
        logger.info("Playing match number " + matchNumber + " between player " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber());

        // player1 is always the master
        player2.play(player1, false);
        player1.play(player2, true);

        winner = player1.reportWinner();

    }

    public Player reportWinner() {
        logger.info("Winner of match number " + matchNumber + " between " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber() + " is player " + winner.getPlayerNumber());
        return winner;
    }

    @Override
    public void run() {
        logger.info("Playing match number " + matchNumber + " between player " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber());

        // player1 is always the master
        player2.play(player1, false);
        player1.play(player2, true);

        winner = player1.reportWinner();
        logger.info("match number " + matchNumber + " won by player " + winner.getPlayerNumber());
        winnersArray[matchNumber] = winner.getPlayerNumber();
    }

}
