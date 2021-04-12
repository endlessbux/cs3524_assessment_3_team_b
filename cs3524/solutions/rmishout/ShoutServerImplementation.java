package cs3524.solutions.rmishout;

import cs3524.solutions.mud.MUD;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoutServerImplementation implements ShoutServerInterface {
    // the remote object
    private final String edgesfile = "assets/mymud.edg";
    private final String messagesfile = "assets/mymud.msg";
    private final String thingsfile = "assets/mymud.thg";
    private MUD mud;
    // hashmap of user locations - Key: username, Value: location
    private HashMap<String,String> locations = new HashMap<>();

    public ShoutServerImplementation() {
        this.mud = new MUD(this.edgesfile, this.messagesfile, this.thingsfile);
    }

    @Override
    public String[] showServers() throws RemoteException {
        return null;
    }

    @Override
    public boolean connect(ShoutClientInterface client, String serverName) throws RemoteException {
        // TODO: allow client to join different servers
        try {
            this.mud.addThing(mud.startLocation(), client.getUserName());
            this.locations.put(client.getUserName(), this.mud.startLocation());
            return true;
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        } finally {
            return false;
        }
    }

    @Override
    public boolean disconnect(String userName) throws RemoteException {
        return false;
    }

    @Override
    public String getMessage(String userName) throws RemoteException {
        return this.mud.locationInfo(this.locations.get(userName));
    }

    @Override
    public String[] getDirections(String userName) throws RemoteException {
        return this.mud.getDirections(this.locations.get(userName));
    }

    @Override
    public boolean move(String direction, String userName) throws RemoteException {
        // TODO
        return false;
    }
}
