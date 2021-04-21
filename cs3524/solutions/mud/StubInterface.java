package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

public interface StubInterface extends Remote {
    // interface of the remote MUD game

    // connect client to given server
    public boolean connect(String userName, String serverName) throws RemoteException;

    // disconnect client from currently connected server
    public void disconnect(String userName) throws RemoteException;

    // get user message
    public String getMessage(String userName) throws RemoteException;

    // get user available directions
    public String[] getDirections(String userName) throws RemoteException;

    // get a list of things that can be picked
    public String[] getPickableThings(String userName) throws RemoteException;

    // move the user towards a given direction
    public boolean move(String direction, String userName) throws RemoteException;

    // user picks an object at location
    public boolean pick(String object, String userName) throws RemoteException;

    public LinkedList<String> getUserInventory(String userName) throws RemoteException;

    public String getUserLocation(String userName) throws RemoteException;

    public LinkedList<String> getUsersAtLocation(String location) throws RemoteException;

    public String[] getOnlinePlayers() throws RemoteException;
}
