package net.konfuzo.tournament;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.lang.IllegalArgumentException;

/**
 * User: nodog
 * Date: 2013-10-28
 * Time: 22:47
 */
public class Tournament {
    private int nPlayers;
    private int nRounds;
    private boolean isOddNPlayers;
    private int[] playerWins;
    private ArrayList<Player> players;
    private static Logger logger = LogManager.getLogger();

    //public Tournament() {
    //}

    public void setnPlayers(int nPlayers) throws IllegalArgumentException {
        if (nPlayers < 1) {
            throw new IllegalArgumentException("Number of players cannot be less than 1.");
        }
        if (nPlayers % 2 == 0) {
            this.nPlayers = nPlayers;
            isOddNPlayers = false;
        } else {
            this.nPlayers = nPlayers + 1;
            isOddNPlayers = true;
        }
        nRounds = nPlayers - 1;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public int getnRealPlayers() {
        if (isOddNPlayers) {
            return nPlayers - 1;
        } else {
            return nPlayers;
        }
    }

    public int getnRounds() {
        return nRounds;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private void initializePlayers() {
        players = new ArrayList<Player>();
        for (int i = 0; i < nPlayers; i++) {
            players.add(new Player(i));
        }
        // need to make the last player always lose if oddNPlayers
        if (isOddNPlayers) {
            players.get(nPlayers - 1).setBuyPlayer();
        }
        playerWins = new int[getnRealPlayers()];
    }

    private Player determineWinner() {
        List<Player> winners = new ArrayList<Player>();
        int maxWins = 0;
        for (int i = 0; i < getnRealPlayers(); i++) {
            if (playerWins[i] > maxWins) {
                winners.clear();
                maxWins = playerWins[i];
                winners.add(players.get(i));
            } else if (playerWins[i] == maxWins) {
                winners.add(players.get(i));
            }
        }
        int winnerIndex = 0;
        if (winners.size() > 1) {
            logger.info(winners.size() + " players tied: " + winners);
            logger.info("By coin toss");
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(winners.size());
            winnerIndex = randomInt;
        }
        // currently random player with winning score wins
        return winners.get(winnerIndex);
    }

    public int[] getPlayerWins() {
        return playerWins;
    }

    public static void main(String[] args) {

        logger.info("Let us play a round robin tournament.");
        logger.info("Starting a timer.");
        long startTime = System.currentTimeMillis();
        Tournament tournament = new Tournament();
        try {
            tournament.setnPlayers(5);
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
            return;
        }
        logger.info("Let there be " + tournament.getnPlayers() + " players.");
        tournament.initializePlayers();

        logger.info("There will be " + tournament.getnRounds() + " rounds.");
        for (int i = 0; i < tournament.getnRounds(); i++) {
            Round aRound = new Round(i);
            aRound.initializeMatches(tournament.getPlayers());
            aRound.playMatches();
            Set<Player> winners = aRound.reportWinners();
            for (Player winner : winners) {
                tournament.getPlayerWins()[winner.getPlayerNumber()]++;
            }
        }
        logger.info("player wins = " + Arrays.toString(tournament.getPlayerWins()));
        Player winner = tournament.determineWinner();
        logger.info("Player " + winner.getPlayerNumber() + " wins!");
        long endTime = System.currentTimeMillis();
        logger.info("That took " + (endTime - startTime) / 1000.0 + " seconds.");
    }
}
