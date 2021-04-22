package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public interface StubInterface extends Remote {
    // interface of the remote object to access MUD games

    // show available MUD Games which can be joined
    public LinkedList<String> getAvailableGames() throws RemoteException;

    // create a new MUD Game
    public boolean createNewGame(String gameName) throws RemoteException;

    // connect client to given MUD game
    public boolean connect(String userName, String gameName) throws RemoteException;

    // disconnect client from currently connected server
    public void disconnect(String userName) throws RemoteException, MUDGameNotFoundException;

    // get user message
    public String getMessage(String userName) throws RemoteException, MUDGameNotFoundException;

    // get user available directions
    public String[] getDirections(String userName) throws RemoteException, MUDGameNotFoundException;

    // get a list of things that can be picked
    public String[] getPickableThings(String userName) throws RemoteException, MUDGameNotFoundException;

    // move the user towards a given direction
    public boolean move(String direction, String userName) throws RemoteException, MUDGameNotFoundException;

    // user picks an object at location
    public boolean pick(String object, String userName) throws RemoteException, MUDGameNotFoundException;

    public LinkedList<String> getUserInventory(String userName) throws RemoteException, MUDGameNotFoundException;

    public String getUserLocation(String userName) throws RemoteException, MUDGameNotFoundException;

    public LinkedList<String> getNearUsers(String userName) throws RemoteException, MUDGameNotFoundException;

    public String[] getOnlinePlayersAtGame(String gameName) throws RemoteException;

    public String[] getOnlinePlayers() throws RemoteException;
}
