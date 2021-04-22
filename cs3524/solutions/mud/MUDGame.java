package cs3524.solutions.mud;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class MUDGame {
    private MUD mud;
    // hashmap of users locations - Key: username, Value: location
    private HashMap<String,String> locations = new HashMap<>();
    // hashmap of users inventories - Key: username, Value: things
    private HashMap<String, LinkedList<String>> inventories = new HashMap<>();

    public MUDGame() {
        // the remote MUD game
        String edgesFile = "assets/mymud.edg";
        String messagesFile = "assets/mymud.msg";
        String thingsFile = "assets/mymud.thg";
        this.mud = new MUD(
                edgesFile,
                messagesFile,
                thingsFile
        );
    }

    /**
     * Connects a user to the specified server
     * @param userName
     * @return true if the user is successfully connected to the MUDGame, false otherwise.
     */
    public boolean connect(String userName) {
        try {
            this.mud.addThing(mud.startLocation(), userName);
            this.locations.put(userName, this.mud.startLocation());
            this.inventories.put(userName, new LinkedList<>());
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * disconnects the user from the game
     * @param userName
     */
    public void disconnect(String userName) {
        this.mud.delThing(this.locations.get(userName), userName);
        this.locations.remove(userName);
        this.inventories.remove(userName);
    }

    /**
     * @param userName
     * @return the message to be printed to the given user based on its location
     */
    public String getMessage(String userName) {
        return this.mud.locationInfo(
            this.locations.get(userName)
        );
    }

    /**
     * @param userName
     * @return an array of directions towards which the user can move
     */
    public String[] getDirections(String userName) {
        return this.mud.getDirections(
            this.locations.get(userName)
        );
    }

    /**
     * @param userName
     * @return things available at given user's location
     * @throws RemoteException
     */
    public String[] getPickableThings(String userName) {
        Set<String> users = this.locations.keySet();
        LinkedList<String> pickableThings = new LinkedList<>();
        String[] things = this.mud.getThingsAtLocation(this.locations.get(userName));
        for(String thing: things) {
            if(!users.contains(thing)) {
                pickableThings.add(thing);
            }
        }
        return pickableThings.toArray(new String[pickableThings.size()]);
    }

    /**
     * Move a user towards a direction
     * @param direction the direction towards which the user will be moved
     * @param userName the user to be moved
     * @return true if the user was moved successfully, false otherwise
     */
    public boolean move(String direction, String userName) {
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

    /**
     * Pick a thing at user location
     * @param thing
     * @param userName
     * @return true if the specified thing was picked, false otherwise
     * @throws RemoteException
     */
    public boolean pick(String thing, String userName) {
        boolean isObjectPicked = false;
        try {
            String location = this.locations.get(userName);
            if(this.mud.isThingAtLocation(thing, location)) {
                // add thing to user inventory
                LinkedList<String> userInventory = this.getUserInventory(userName);
                userInventory.add(thing);
                // update user inventory
                this.inventories.replace(userName, userInventory);
                // remove thing from mud location
                this.mud.delThing(location, thing);
                isObjectPicked = true;
            }
        } catch (Exception e) {
            System.err.println(
                    String.format("The thing '%s' could not be picked.", thing)
            );
            System.err.println(e.getMessage());
        }
        return isObjectPicked;
    }

    /**
     * @param userName
     * @return inventory of the specified user
     * @throws RemoteException
     */
    public LinkedList<String> getUserInventory(String userName) {
        return (LinkedList<String>)this.inventories.get(userName).clone();
    }

    /**
     * @param userName
     * @return the location of given user in the MUD
     * @throws RemoteException
     */
    public String getUserLocation(String userName) {
        return this.locations.get(userName);
    }

    /**
     * @param inputLocation
     * @return a linked list with the usernames at given location
     * @throws RemoteException
     */
    public LinkedList<String> getUsersAtLocation(String inputLocation) {
        LinkedList<String> usersAtLocation = new LinkedList<>();
        this.locations.forEach((userName, userLocation) -> {
            if(userLocation.equals(inputLocation)) {
                usersAtLocation.add(userName);
            }
        });
        return usersAtLocation;
    }

    /**
     * @return an array of player's usernames connected to the MUD
     * @throws RemoteException
     */
    public String[] getOnlinePlayers() {
        return this.locations.keySet().toArray(new String[0]);
    }
}
