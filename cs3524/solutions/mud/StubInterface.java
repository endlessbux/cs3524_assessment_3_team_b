package cs3524.solutions.mud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;



public interface StubInterface extends Remote {

    // interface of the remote object to access MUD games





    // create a new MUD Game
    public boolean createNewGame(String gameName) throws RemoteException;

    // connect client to given MUD game
    public boolean connect(User gameUser, String gameName) throws RemoteException;

    // disconnect client from currently connected server
    public void disconnect(User gameUser) throws RemoteException;

    // get user message
    public String getMessage(User gameUser) throws RemoteException;

    // get user available directions
    public String[] getDirections(User gameUser) throws RemoteException;

    // get a list of things that can be picked
    public String[] getPickableThings(User gameUser) throws RemoteException;

    // move the user towards a given direction
    public boolean move(String direction, User gameUser) throws RemoteException;

    // user picks an object at location
    public boolean pick(String object, User gameUser) throws RemoteException;

    public LinkedList<String> getUserInventory(User gameUser) throws RemoteException;

    public String getUserLocation(User gameUser) throws RemoteException;

    public LinkedList<String> getNearUsers(User gameUser) throws RemoteException;

    public String[] getOnlinePlayersAtGame(String gameName) throws RemoteException;

    public String[] getOnlinePlayers() throws RemoteException;

    static StubInterface initServerHandle(int port, String hostName) throws MalformedURLException, NotBoundException, RemoteException {
        // get server handle from RMI registry
        String registeredURL = String.format("rmi://%s:%d/ShoutService", hostName, port);
        System.out.println(String.format("Looking up %s", registeredURL));
        StubInterface serverHandle = (StubInterface) Naming.lookup(registeredURL);
        return serverHandle;
    }

    public LinkedList<String> getAvailableGames() throws RemoteException;




    //public String getUserName() throws RemoteException;
    //public String getGameName() throws RemoteException;

}
