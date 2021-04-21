package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface GameInterface extends Remote {
    // the interface to the client's application

    public String getUserName() throws RemoteException;
    public LinkedList<String> getInventoryFromUser(StubInterface serverHandle, GameInterface user) throws RemoteException;
    public boolean pick(StubInterface serverHandle, String object) throws RemoteException;
}
