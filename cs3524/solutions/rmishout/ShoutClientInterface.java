package cs3524.solutions.rmishout;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ShoutClientInterface extends Remote {
    public String getUserName() throws RemoteException;
    public LinkedList<String> getInventoryFromUser(ShoutServerInterface serverHandle, ShoutClientInterface user) throws RemoteException;
    public boolean pick(ShoutServerInterface serverHandle, String object) throws RemoteException;
}
