package cs3524.solutions.rmishout;

import cs3524.solutions.mud.MUD;

import javax.management.modelmbean.RequiredModelMBean;
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

    /**
     * TODO
     * @return a list of the available servers to which it is possible to connect
     */
    @Override
    public String[] showServers() throws RemoteException {
        return null;
    }

    /**
     * Connects a user to the specified server
     * @param userName
     * @param serverName
     * @return true if the user is successfully connected, false otherwise.
     */
    @Override
    public boolean connect(String userName, String serverName) throws RemoteException {
        // TODO: allow client to join different servers
        try {
            this.mud.addThing(mud.startLocation(), userName);
            this.locations.put(userName, this.mud.startLocation());
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return false;
        }
    }

    /**
     * disconnects the user from the game
     * @param userName
     */
    @Override
    public void disconnect(String userName) throws RemoteException {
        this.mud.delThing(this.locations.get(userName), userName);
        this.locations.remove(userName);
    }

    /**
     * @param userName
     * @return the message to be printed to the user based on its location
     */
    @Override
    public String getMessage(String userName) throws RemoteException {
        return this.mud.locationInfo(this.locations.get(userName));
    }

    /**
     * @param userName
     * @return an array of directions towards which the user can move
     */
    @Override
    public String[] getDirections(String userName) throws RemoteException {
        return this.mud.getDirections(this.locations.get(userName));
    }

    /**
     * Move a user towards a direction
     * @param direction the direction towards which the user will be moved
     * @param userName the user to be moved
     * @return true if the user was moved successfully, false otherwise
     */
    @Override
    public boolean move(String direction, String userName) throws RemoteException {
        boolean isUserMoved = false;
        try {
            String location = this.locations.get(userName);
            String newLocation = this.mud.moveThing(location, direction, userName);
            this.locations.replace(userName, newLocation);
            isUserMoved = this.mud.locationInfo(newLocation).contains(userName);
        } catch (Exception e) {
            System.err.println(String.format("The user %s could not be moved.", userName));
            System.err.println(e.getMessage());
        }
        return isUserMoved;
    }
}
