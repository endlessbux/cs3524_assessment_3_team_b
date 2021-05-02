package cs3524.solutions.mud;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class StubImplementation implements StubInterface {
    // hashmap of open MUD games - Key: game name, Value: MUD game
    private HashMap<String, MUDGame> openGames;
    // hashmap containing connected users per MUD game - Key: username, Value: game name
    //private HashMap<String, String> userNameToMUDGameName;
    private static int maxMudGames = 8;

    public StubImplementation() {
        this.openGames = new HashMap<>();
    }

    /**
     * @param gameUser
     * @return the MUDGame object to which the user is connected
     * @throws RemoteException
     */
    public static LinkedList getGamesFromUserName(User gameUser) throws RemoteException, MUDGameNotFoundException {
        LinkedList gameNames = gameUser.getUserGamePool();
        if(gameNames == null) {
            throw new MUDGameNotFoundException();
        }
        return gameNames;
    }


    /**
     * Checks if max mud games has been reached and checks to see if game already exists before creating game
     * @param gameName
     * @return true if the MUD game was created, false otherwise
     * @throws RemoteException
     */
    @Override
    public boolean createNewGame(String gameName) throws RemoteException {
        StubImplementation stubImp = new StubImplementation();
        LinkedList<String> currentGames = stubImp.getAvailableGames();
        if (currentGames.size() < this.maxMudGames) {
            if(!currentGames.contains(gameName)) {
                this.openGames.put(gameName, new MUDGame());
                return true;
            }
        }
        return false;
    }

    /**
     * Connects a user to the specified server
     * @param gameUser
     * @param gameName
     * @return true if the user is successfully connected, false otherwise.
     * @throws RemoteException
     */
    @Override
    public boolean connect(User gameUser, String gameName) throws RemoteException {
        MUDGame game = this.openGames.get(gameName);
        String userName = gameUser.getUserName();
        if(game != null) {
            return game.connect(userName);
        }
        return false;
    }

    /**
     * Disconnects the user from all the games it's connected to
     * @param gameUser
     * @throws RemoteException
     */
    @Override
    public void disconnect(User gameUser) throws RemoteException, MUDGameNotFoundException {
        // disconnect user from all MUDGame objects
        for(MUDGame game: this.openGames.values()) {
            game.disconnect(gameUser.getUserName());
        }
        gameUser.quitAllGames();
    }

        /**
     * @param gameUser
     * @return the message to be printed to the user based on its location
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String getMessage(User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame gameMessage = this.openGames.get(game);
        return gameMessage.getMessage(gameUser.getUserName());
    }

    /**
     * @param gameUser
     * @return an array of directions towards which the user can move
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String[] getDirections(User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.getDirections(gameUser.getUserName());
    }

    /**
     * @param gameUser
     * @return things available at given user's location
     * @throws RemoteException,MUDGameNotFoundException
     */

    public String[] getPickableThings(User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.getPickableThings(gameUser.getUserName());
    }

    /**
     * Move a user towards a direction
     * @param direction the direction towards which the user will be moved
     * @param gameUser the user to be moved
     * @return true if the user was moved successfully, false otherwise
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public boolean move(String direction, User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.move(direction, gameUser.getUserName());
    }

    /**
     * Pick a thing at user location
     * @param thing
     * @param gameUser
     * @return true if the specified thing was picked, false otherwise
     * @throws RemoteException,MUDGameNotFoundException
     */

    public boolean pick(String thing, User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.pick(thing, gameUser.getUserName());
    }

    /**
     * @param gameUser
     * @return inventory of the specified user
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public LinkedList<String> getUserInventory(User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.getUserInventory(gameUser.getUserName());
    }

    /**
     * @param gameUser
     * @return the location of given user in the MUD
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public String getUserLocation(User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.getUserLocation(gameUser.getUserName());
    }

    /**
     * @param gameUser the username of the user at location which will be checked
     * @return a list of usernames at the same location as specified user
     * @throws RemoteException,MUDGameNotFoundException
     */
    @Override
    public LinkedList<String> getNearUsers(User gameUser) throws RemoteException, MUDGameNotFoundException {
        String game = gameUser.getGameFocus();
        MUDGame mudGame = this.openGames.get(game);
        return mudGame.getUsersAtLocation(
                mudGame.getUserLocation(gameUser.getUserName())
        );
    }


    /**
     * @return set of game names which can be joined
     * @throws RemoteException
     */

    public LinkedList<String> getAvailableGames() throws RemoteException {
        LinkedList<String> currentGames = new LinkedList<>();
        this.openGames.forEach((gameName, game) -> {
            currentGames.add(gameName);
        });
        return currentGames;
    }
    /**
     * @param gameName
     * @return an array of player's usernames connected to the given MUDGame name
     * @throws RemoteException
     */

    public String[] getOnlinePlayersAtGame(String gameName) throws RemoteException {
        MUDGame game = this.openGames.get(gameName);
        if(game != null) {
            return game.getOnlinePlayers();
        }
        return null;
    }

    public String[] getOnlinePlayers() throws  RemoteException {
        LinkedList<String> onlinePlayers = new LinkedList<>();
        this.openGames.forEach((gameName, game) -> {
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

    //returns the maximum number of Mud games
    public Integer getMudMax () {
        return maxMudGames;
    }
}
