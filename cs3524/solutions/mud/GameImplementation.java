package cs3524.solutions.mud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;



public class GameImplementation implements Serializable {
    // client application calling methods of the remote object

    public static void main(String args[]) throws RemoteException {
        // retrieve user input
        if(args.length < 2) {
            System.err.println("Usage:\njava GameImplementation <hostname> <port>");
            return;
        }
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);

        setSecurityPolicy("rmishout.policy");
        try {
            StubInterface serverHandle = StubInterface.initServerHandle(port, hostName);
            String userName = getUserInput("Insert your username:");
            String gameName = chooseOrCreateGame(serverHandle);
            User gameUser = new User(userName, gameName);
            do {
                GameImplementation userGame = joinServer(serverHandle, gameUser, gameName);
                runGame(serverHandle, gameUser);
            } while(getUserInput("Do you want to join another game?\nInsert any key to exit or [g] to play again.").equals("g"));///
        } catch (MalformedURLException e) {
            System.err.println("The provided URL is not valid.");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            // not sure what to print here apart from the exception message
            //System.err.println("There was an issue with the input.");
            System.err.println(e.getMessage());
        } catch (MUDGameNotFoundException e) {
            System.err.println("Your request could not be processed because the MUD is offline.\nJoin a different MUD.");
        } catch (NotBoundException e) {
            System.err.println("Something has gone wrong...");
            System.err.println(e.getMessage());
        } catch(IndexOutOfBoundsException e){
            System.err.println("This old mistake");
            System.err.println(e.getMessage());
        }
        System.out.println("GAME OVER.");
    }

    /**
     * Method for user to join or create a new MUD game
     * @param serverHandle
     * @return the chosen/created game name
     * @throws IOException
     */
    public static String chooseOrCreateGame(StubInterface serverHandle) throws IOException {
        clearScreen();
        printOpenGames(serverHandle);
        LinkedList currentGames = serverHandle.getAvailableGames();
        // ask user to either join a game or create one
        String gameName = getUserInput("Insert the game you want to join, or insert a new name to create a MUD game");
        if(!currentGames.contains(gameName)) {
            // if inserted game name doesn't exist create one
            System.out.println(
                    String.format(
                            "Generating MUD game '%s'...",
                            gameName
                    )
            );
            serverHandle.createNewGame(gameName);
        } else {
            System.out.println(
                    String.format(
                            "Joining %s...",
                            gameName
                    )
            );
        }
        return gameName;
    }

    /**
     * Method to print open games on the server and players online at each game
     * @param serverHandle
     * @throws RemoteException
     */
    private static void printOpenGames(StubInterface serverHandle) throws RemoteException {
        System.out.println("Open games:");
        // show open games on server
        LinkedList<String> availableGames = serverHandle.getAvailableGames();

        if(availableGames.size() < 1) {
            System.out.println("[There are no online games currently]");
            return;
        }
        // print online players on each game
        for(String gameName: availableGames) {
            System.out.println("+ " + gameName);
        }
    }

    static String getUserInput(String inputMessage) throws IOException {
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in)
        );
        System.out.println(inputMessage);
        return input.readLine();
    }

    /**
     * Method to print players that joined a given game
     * @param serverHandle
     * @param gameName
     * @throws RemoteException
     */
    private static void printOnlinePlayersAtGame(StubInterface serverHandle, String gameName) throws RemoteException {
        String[] onlinePlayers = serverHandle.getOnlinePlayersAtGame(gameName);
        for(String playerName: onlinePlayers) {
            System.out.println("+ " + playerName);
        }
    }

    /**
     * Method to handle in-game input and output
     * @param serverHandle
     * @param gameUser
     * @throws IOException
     * @throws MUDGameNotFoundException
     */
    private static void runGame(StubInterface serverHandle, User gameUser) throws IOException, MUDGameNotFoundException {
        System.out.println("Game started!");
        String chooseSomething;
        boolean gameOver = false;
        while(!gameOver) {
            chooseSomething = getUserGameOutput(serverHandle, gameUser);
            String choice = getUserInput(chooseSomething);
            clearScreen();

            switch(choice) {
                case "q":
                    // quit game
                    System.out.println("Quitting game...");
                    serverHandle.disconnect(gameUser);
                    gameOver = true;
                    break;
                case "s":
                    //switch to another game
                    clearScreen();
                    printOpenGames(serverHandle);
                    // ask user to either join a game or create one
                    String gameName2 = getUserInput("INSERT the name of the game you want to join");
                    serverHandle.connect(gameUser, gameName2);
                    break;
                case "n":
                    //create new game
                    String gameName3 = getUserInput("Insert the name of the game you want to create");
                    serverHandle.createNewGame(gameName3);
                    serverHandle.connect(gameUser, gameName3);
                    gameUser.addGameToPool(gameName3);
                    gameUser.switchGameFocus(gameName3);
                    System.out.println("Creating new game...");
                    break;
                case "":
                    // refresh
                    System.out.println("Refreshing...");
                    break;
                case "h":
                    // print help message
                    System.out.println("Currently available actions:");
                    System.out.println(getPrintableActions(serverHandle, gameUser));
                    break;
                default:
                    if(isActionAvailable(gameUser, serverHandle, choice)) {
                        System.out.println(doAction(gameUser, serverHandle, choice));
                    } else {
                        // user input is invalid. Print error message and retry with old message and directions
                        System.err.println(String.format("'%s' is an invalid action. Try again.", choice));
                    }
            }
        }
    }

    /**
     * Method to clear the command line interface
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Specifies the security policy and sets a security manager
     * @param policy String the security policy to be set
     */
    private static void setSecurityPolicy(String policy) {
        System.setProperty("java.security.policy", policy);
        System.setSecurityManager(new SecurityManager());
    }

    /**
     * @param choice user input
     * @return the user input split into an array which can be analysed
     */
    private static String[] getAction(String choice) {
        return choice.trim().split("\\s+");
    }

    /**
     * Method to perform the user action based on the input
     * @param gameUser
     * @param serverHandle
     * @param choice
     * @return the result message of the user action
     * @throws RemoteException,MUDGameNotFoundException
     */
    private static String doAction(User gameUser, StubInterface serverHandle, String choice) throws RemoteException, MUDGameNotFoundException {
        String actionResult = String.format("Internal error:\nCould not perform '%s'. Try again.", choice);
        String[] action = getAction(choice);
        String userName = gameUser.getUserName();

        switch (action[0]) {
            case "move":
                if(serverHandle.move(action[1], gameUser)) {
                    actionResult = String.format("You move %s.", action[1]);
                }
                break;
            case "pick":
                if(serverHandle.pick(action[1], gameUser)) {
                    actionResult = String.format("%s picked.", action[1]);
                }
                break;
            case "show-inventory":
                LinkedList<String> inputUserInventory = serverHandle.getUserInventory(gameUser);
                actionResult = String.format("%s's inventory:\n", action[1]);
                actionResult += getPrintableInventory(inputUserInventory);
            case "show-online-players":
                String gameName = gameUser.getGameFocus();
                actionResult = "Online players at " + gameName + ":";
                actionResult += getPrintablePlayersAtGame(serverHandle, gameName);
            case "show-user-location":
                String location = serverHandle.getUserLocation(gameUser);
                String game = gameUser.getGameFocus();
                actionResult = "You are at " + location + " in "+ game + ".";
                actionResult += getPrintablePlayersAtGame(serverHandle, game);
        }
        return actionResult;
    }

    private static void clearUser(User gameUser){
        gameUser.quitAllGames();
    }

    /**
     * Method to check whether the action inputted by the user is available
     * @param gameUser
     * @param serverHandle
     * @param choice
     * @return true if the inputted action is available, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static boolean isActionAvailable(User gameUser, StubInterface serverHandle, String choice) throws RemoteException, MUDGameNotFoundException {
        boolean isActionAvailable = false;
        String[] action = getAction(choice);
        switch (action[0]) {
            case "move":
                isActionAvailable = isDirectionAvailable(gameUser, serverHandle, action[1]);
                break;
            case "pick":
                isActionAvailable = isThingAvailable(gameUser, serverHandle, action[1]);
                break;
            case "show-inventory":
                String userLocation = serverHandle.getUserLocation(gameUser);
                String inputUserLocation = serverHandle.getUserLocation(gameUser);
                isActionAvailable = userLocation.equals(inputUserLocation);
                break;
            case "show-online-players":
                isActionAvailable = true;
                break;
        }
        return isActionAvailable;
    }

    /**
     * @param gameUser
     * @param serverHandle
     * @param toTestInput
     * @return true if toTestInput is an available direction, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static boolean isThingAvailable(User gameUser, StubInterface serverHandle, String toTestInput) throws RemoteException, MUDGameNotFoundException {
        boolean isPickable = false;
        String userName = gameUser.getUserName();
        String[] pickableThings = serverHandle.getPickableThings(gameUser);
        for(String thing: pickableThings) {
            if(toTestInput.equals(thing)) {
                isPickable = true;
            }
        }
        return isPickable;
    }



    /**
     * @param gameUser
     * @param serverHandle
     * @param toTestInput
     * @return true if toTestInput is an available direction, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static boolean isDirectionAvailable(User gameUser, StubInterface serverHandle, String toTestInput) throws RemoteException, MUDGameNotFoundException {
        boolean isAvailable = false;
        String[] availableDirections = serverHandle.getDirections(gameUser);
        for(String direction: availableDirections) {
            if(toTestInput.equals(direction)) {
                isAvailable = true;
                break;
            }
        }
        return  isAvailable;
    }

    /**
     * Method to convert a "raw" list of available directions to a printable output
     * @param directions String[] array of available directions
     * @return a printable output to display the available directions towards which a user can move
     */
    private static String getPrintableDirections(String[] directions) {
        String printableDirections = "";
        for(String direction: directions) {
            printableDirections += "<move " + direction + ">\n";
        }
        return printableDirections;
    }

    /**
     * Method to convert a "raw" list of pickable things to be printed
     * @param things String[] array of pickable things
     * @return a printable output to display the available things which can be picked by the user
     */
    private static String getPrintableThings(String[] things) {
        String printableObjects = "";
        for(String object: things) {
            printableObjects += "<pick " + object + ">\n";
        }
        return printableObjects;
    }

    /**
     * Method to convert a "raw" list of users to a printable list of commands to show their inventories
     * @param usersAtLocation String[] array of users which are in a given location
     * @return a printable output to display which users' inventories can be visualised
     */
    private static String getPrintableUsers(LinkedList<String> usersAtLocation) {
        String printableUsers = "";
        for(String user: usersAtLocation) {
            printableUsers += "<show-inventory " + user + ">\n";
        }
        return printableUsers;
    }

    private static String getPrintableInventory(LinkedList<String> inventory) {
        String printableInventory = "";
        if(inventory.size() < 1) {
            printableInventory = "[inventory is empty]";
        } else {
            for(String item: inventory) {
                printableInventory += "+ " + item + "\n";
            }
        }
        return printableInventory;
    }

    /**
     * Method to print a list of actions that the user can choose
     * @param serverHandle
     * @param gameUser
     * @return a printable output to display the available actions that the user can take
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static String getPrintableActions(StubInterface serverHandle, User gameUser) throws RemoteException, MUDGameNotFoundException {
        String printableActions = "";
        String userName = gameUser.getUserName();
        String location = serverHandle.getUserLocation(gameUser);
        printableActions += getPrintableDirections(serverHandle.getDirections(gameUser));
        printableActions += getPrintableThings(serverHandle.getPickableThings(gameUser));
        printableActions += getPrintableUsers(serverHandle.getNearUsers(gameUser));
        printableActions += "<show-online-players>";
        printableActions += "<show-user-location>";
        return printableActions;
    }



    /**
     * Procedure to make the client join the MUD game
     * @param serverHandle
     * @return the interface to control the client
     * @throws IOException
     */
    private static GameImplementation joinServer(StubInterface serverHandle, User gameUser, String gameName) throws IOException {
        // create user instance
        System.out.println("Logging in...");
        String userName = gameUser.getUserName();
        GameImplementation userGame = new GameImplementation();
        serverHandle.connect(
            gameUser,
            gameName
        );
        System.out.println(
            String.format(
                    "Logged in as '%s'.",
                    userName
            )
        );
        String onlinePlayers = getPrintablePlayersAtGame(serverHandle, gameName);
        System.out.println(
            String.format(
                    "Online players:%s",
                    onlinePlayers
            )
        );
        return userGame;
    }

    private static String getPrintablePlayers(StubInterface serverHandle) throws RemoteException {
        String onlinePlayers = "";
        String[] players = serverHandle.getOnlinePlayers();
        for(String player: players) {
            onlinePlayers += "\n+ " + player;
        }
        return onlinePlayers;
    }

    private static String getPrintablePlayersAtGame(StubInterface serverHandle, String gameName) throws RemoteException {
        String onlinePlayers = "";
        String[] players = serverHandle.getOnlinePlayersAtGame(gameName);
        for(String player: players) {
            onlinePlayers += "\n+ " + player;
        }
        return onlinePlayers;
    }

    /**
     * Method to get CLI output String for user to make a choice
     * @param serverHandle
     * @param gameUser
     * @return game output string
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static String getUserGameOutput(StubInterface serverHandle, User gameUser) throws RemoteException, MUDGameNotFoundException {
        String message = serverHandle.getMessage(gameUser);
        return String.format(
                "%sMake a move:\n(type 'h' to show available commands, 'n' for a new game, 's' to switch games, or 'q' to quit the game.)",
                message
        );
    }

    /**
     * @param serverHandle
     * @param gameUser
     * @return the list of items in the specified user's inventory
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */

    public LinkedList<String> getInventoryFromUser(StubInterface serverHandle, User gameUser) throws RemoteException, MUDGameNotFoundException {
        return serverHandle.getUserInventory(gameUser);
    }

    /**
     * method to pick an object at given user's location
     * @param serverHandle
     * @param object
     * @return true if the object was picked, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */

    public boolean pick(StubInterface serverHandle, String object, User gameUser) throws RemoteException, MUDGameNotFoundException {
        return serverHandle.pick(object, gameUser);
    }

}
