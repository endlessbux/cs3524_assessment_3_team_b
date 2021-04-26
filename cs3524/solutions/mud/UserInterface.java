package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface UserInterface extends Remote {
    // the interface to the client's application

    public String getUserName() throws RemoteException;
    public String getGameName() throws RemoteException;
    //public String getUserInput() throws IOException;
    //public String getServerHandle() throws RemoteException;
    //public LinkedList<String> getInventoryFromUser(StubInterface serverHandle, UserInterface user) throws RemoteException, MUDGameNotFoundException;
    //public boolean pick(StubInterface serverHandle, String object) throws RemoteException, MUDGameNotFoundException;
}
