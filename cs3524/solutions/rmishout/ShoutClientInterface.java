package cs3524.solutions.rmishout;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ShoutClientInterface extends Remote {
    public String getUserName() throws RemoteException;
}
