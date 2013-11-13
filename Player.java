package net.konfuzo.tournament;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * User: nodog
 * Date: 2013-10-28
 * Time: 22:54
 */
public class Player implements Runnable {

    private int playerNumber;
    private HashSet<Player> opponents;
    private boolean isBuyPlayer; // always loses
    private static Logger logger = LogManager.getLogger();
    private Player currentWinner;

    @Override
    public void run() {
        // do nothing for now.
    }

    // this is a Roshambo player
    private enum Move { ROCK("ROCK"), PAPER("PAPER"), SCISSORS ("SCISSORS");

        String label;
        Move(String label) {
            this.label = label;
        }
        public static Move getRandomMove() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }

        public int roshamboCompareTo(Move opponentMove) {
            if (this.equals(opponentMove)) {
                return 0;
            }
            if (((this == ROCK) && (opponentMove == SCISSORS)) ||
                ((this == PAPER) && (opponentMove == ROCK)) ||
                ((this == SCISSORS) && (opponentMove == PAPER))) {
                return 1;
            }
            return -1;
        }

        public String display() {
            return label;
        }
    }

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        logger.info("Player " + playerNumber + " initialized.");
        isBuyPlayer = false;
        opponents = new HashSet<Player>();
    }

    public void addOpponent(Player opponent) {
        opponents.add(opponent);
    }

    public Set<Player> getOpponents() {
        return opponents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (playerNumber != player.playerNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setBuyPlayer() {
        logger.info("Player " + playerNumber + " is a buy player.");
        isBuyPlayer = true;
    }

    public boolean isBuyPlayer() {
        return isBuyPlayer;
    }

    public Player reportWinner() {
        logger.info("Player " + currentWinner.getPlayerNumber() + " wins.");
        return currentWinner;
    }

    public void play(Player opponent, boolean isMaster) {
        addOpponent(opponent);
        if (isMaster) {
            if (isBuyPlayer()) {
                currentWinner = opponent;
            } else if (opponent.isBuyPlayer()) {
                currentWinner = this;
            } else {
                Move opponentMove;
                Move myMove;
                do {
                    opponentMove = opponent.chooseMove();
                    logger.info("Opponent's move is " + opponentMove);
                    myMove = this.chooseMove();
                    logger.info("Master's move is " + myMove);
                } while (myMove.roshamboCompareTo(opponentMove) == 0);

                if (myMove.roshamboCompareTo(opponentMove) > 0) {
                    currentWinner = this;
                    logger.info(myMove + " wins.");
                } else {
                    currentWinner = opponent;
                    logger.info(opponentMove + " wins.");
                }
            }
        }
    }

    private Move chooseMove() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(100) + 100);
        } catch (InterruptedException e) {}
        return Move.getRandomMove();
    }
}
