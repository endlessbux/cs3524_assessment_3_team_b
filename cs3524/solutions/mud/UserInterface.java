package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface UserInterface extends Remote {
    // the interface to the local user data

    public boolean addGameToPool(String gameName);
    public boolean switchGameFocus(String gameName);
    public boolean quitGame(String oldGame, String newGame);
    public void quitAllGames();
    public String getUserName();
    public String getGameFocus();
    public boolean canCreateAnotherGame();
}
