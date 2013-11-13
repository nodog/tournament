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


    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        logger.info("New match created between player " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber());
    }

    public void play() {
        logger.info("Playing match between player " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber());

        // player1 is always the master
        player2.play(player1, false);
        player1.play(player2, true);

        winner = player1.reportWinner();
    }

    public Player reportWinner() {
        logger.info("Winner of match between " + player1.getPlayerNumber()
                + " and player " + player2.getPlayerNumber() + " is player " + winner.getPlayerNumber());
        return winner;
    }

    @Override
    public void run() {
        // do nothing right now.
    }
}
