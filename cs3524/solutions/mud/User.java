package cs3524.solutions.mud;

import java.io.*;
import java.util.*;


public class User implements UserInterface, Serializable {
    private final int maxGames = 5;
    private String userName;
    private LinkedList<String> gamePool;
    private int gameFocus;
    //private String gameFocus;

    public User(String userName, String gameName) {
        this.userName = userName;
        this.gamePool = new LinkedList<>();
        this.addGameToPool(gameName);
        this.gameFocus = 0;
    }

    /**
     * Method to add a game to the game pool
     * @param gameName the name of the game to be added
     * @return true if the inserted game name was added to the pool, false otherwise
     */
    @Override
    public boolean addGameToPool(String gameName) {
        if(this.gamePool.size() < this.maxGames) {
            if(!gamePool.contains(gameName)) {
                return this.gamePool.add(gameName);
            }
        }
        return false;
    }

    @Override
    public boolean switchGameFocus(String gameName) {
        if(this.gamePool.contains(gameName)) {
            this.gameFocus = this.gamePool.indexOf(gameName);
            return true;
        }
        return false;
    }

    @Override
    public boolean quitGame(String oldGame, String newGame) {
        if (switchGameFocus(newGame)) {
            if (!gamePool.contains(oldGame)) {
                return this.gamePool.remove(oldGame);
            }
        }
            return false;
    }

    @Override
    public void quitAllGames() {
        for(int i = 0; i < this.gamePool.size(); i++) {
            this.gamePool.remove(i);
        }
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    public LinkedList<String> getUserGamePool() {
        return this.gamePool;
    }

    @Override
    public String getGameFocus() {
        return this.gamePool.get(this.gameFocus);
    }

    @Override
    public boolean canCreateAnotherGame() {
        return this.maxGames >= this.gamePool.size();
    }

}