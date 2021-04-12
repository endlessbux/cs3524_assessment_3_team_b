package cs3524.solutions.rmishout;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ShoutServerInterface extends Remote {
    // interface of the remote object

    // show a list of all available servers
    public String[] showServers() throws RemoteException;

    // connect client to given server
    public boolean connect(String userName, String serverName) throws RemoteException;

    // disconnect client from currently connected server
    public void disconnect(String userName) throws RemoteException;

    // get user message
    public String getMessage(String userName) throws RemoteException;

    // get user available directions
    public String[] getDirections(String userName) throws RemoteException;

    // move the user towards a given direction
    public boolean move(String direction, String userName) throws RemoteException;
}
