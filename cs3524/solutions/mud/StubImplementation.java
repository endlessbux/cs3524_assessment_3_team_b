package cs3524.solutions.mud;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class StubImplementation implements StubInterface {
    // hashmap of open MUD games - Key: game name, Value: MUD game
    private HashMap<String, MUDGame> games;
    // hashmap containing connected users per MUD game - Key: username, Value: game name
    private HashMap<String, String> userNameToMUDGameName;

    public StubImplementation() {
        this.games = new HashMap<>();
        this.userNameToMUDGameName = new HashMap<>();
    }

    /**
     * @param userName
     * @return the MUDGame object to which the user is connected
     * @throws RemoteException
     */
    private MUDGame getGameFromUserName(String userName) throws RemoteException, MUDGameNotFoundException {
        String gameName = this.userNameToMUDGameName.get(userName);
        MUDGame game = this.games.get(gameName);
        if(game == null) {
            throw new MUDGameNotFoundException();
        }
        return game;
    }

    /**
     * @return set of game names which can be joined
     * @throws RemoteException
     */
    @Override
    public LinkedList<String> getAvailableGames() throws RemoteException {
        return new LinkedList<>(this.games.keySet());
    }

    /**
     * @param gameName
     * @return true if the MUD game was created, false otherwise
     * @throws RemoteException
     */
    @Override
    public boolean createNewGame(String gameName) throws RemoteException {
        if(!this.getAvailableGames().contains(gameName)) {
            this.games.put(gameName, new MUDGame());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Connects a user to the specified server
     * @param userName
     * @param gameName
     * @return true if the user is successfully connected, false otherwise.
     * @throws RemoteException
     */
    @Override
    public boolean connect(String userName, String gameName) throws RemoteException {
        MUDGame game = this.games.get(gameName);
        if(game != null) {
            if(game.connect(userName)) {
                this.userNameToMUDGameName.put(userName, gameName);
                return true;
            }
        }
        return false;
    }

    /**
     * Disconnects the user from the game to which it's connected
     * @param userName
     * @throws RemoteException
     */
    @Override
    public void disconnect(String userName) throws RemoteException {
        try {
            MUDGame game = this.getGameFromUserName(userName);
            this.userNameToMUDGameName.remove(userName);
            game.disconnect(userName);
        } catch (MUDGameNotFoundException e) {}
    }

    /**
     * @param userName
     * @return the message to be printed to the user based on its location
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String getMessage(String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.getMessage(userName);
    }

    /**
     * @param userName
     * @return an array of directions towards which the user can move
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String[] getDirections(String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.getDirections(userName);
    }

    /**
     * @param userName
     * @return things available at given user's location
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String[] getPickableThings(String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.getPickableThings(userName);
    }

    /**
     * Move a user towards a direction
     * @param direction the direction towards which the user will be moved
     * @param userName the user to be moved
     * @return true if the user was moved successfully, false otherwise
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public boolean move(String direction, String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.move(direction, userName);
    }

    /**
     * Pick a thing at user location
     * @param thing
     * @param userName
     * @return true if the specified thing was picked, false otherwise
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public boolean pick(String thing, String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.pick(thing, userName);
    }

    /**
     * @param userName
     * @return inventory of the specified user
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public LinkedList<String> getUserInventory(String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.getUserInventory(userName);
    }

    /**
     * @param userName
     * @return the location of given user in the MUD
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String getUserLocation(String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.getUserLocation(userName);
    }

    /**
     * @param userName the username of the user at location which will be checked
     * @return a list of usernames at the same location as specified user
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public LinkedList<String> getNearUsers(String userName) throws RemoteException, MUDGameNotFoundException {
        MUDGame game = this.getGameFromUserName(userName);
        return game.getUsersAtLocation(
                game.getUserLocation(userName)
        );
    }

    /**
     * @param gameName
     * @return an array of player's usernames connected to the given MUDGame name
     * @throws RemoteException
     */
    @Override
    public String[] getOnlinePlayersAtGame(String gameName) throws RemoteException {
        MUDGame game = this.games.get(gameName);
        if(game != null) {
            return game.getOnlinePlayers();
        }
        return null;
    }

    public String[] getOnlinePlayers() throws  RemoteException {
        LinkedList<String> onlinePlayers = new LinkedList<>();
        this.games.forEach((gameName, game) -> {
            LinkedList<String> onlinePlayersAtGame = new LinkedList<>(
                Arrays.asList(
                        game.getOnlinePlayers()
                )
            );
            onlinePlayersAtGame.forEach((player) -> {
                onlinePlayers.add(player);
            });
        });
        return onlinePlayers.toArray(new String[0]);
    }
}
