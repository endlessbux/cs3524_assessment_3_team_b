package cs3524.solutions.mud;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.LinkedList;

public class GameImplementation implements Serializable {
    // client application calling methods of the remote object
    static User gameUser; // = new User();
    private static String userName;
    public String gameFocus; // = gameUser.gameFocus;
    //public String userName; //= gameUser.userName;
    public static StubInterface serverHandle; // = gameUser.serverHandle;
    //public StubInterface serverHandle = StubInterface getServerHandle();


    public GameImplementation(User gameUser, String gameFocus, StubInterface serverHandle, String userName) {
        this.gameFocus = gameFocus;
        this.serverHandle = serverHandle;
        this.userName = userName;
        this.gameUser = gameUser;
    }

    public static void main(String args[]) throws RemoteException {

        try {
            do {
                gameUser = new User();
                serverHandle = gameUser.serverHandle;
                String gameName = chooseOrCreateGame(serverHandle);
                GameImplementation userGame = joinServer(serverHandle, gameName);
                runGame(serverHandle, userGame, gameName);
            } while(StubInterface.getUserInput("Do you want to join another game?\nInsert any key to exit, or [y] to join another game.").equals("y"));
        } catch (MalformedURLException e) {
            System.err.println("The provided URL is not valid.");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            // not sure what to print here apart from the exception message
            //System.err.println("There was an issue with the input.");
            System.err.println(e.getMessage());
        } catch (MUDGameNotFoundException e) {
            System.err.println("Your request could not be processed because the MUD is offline.\nJoin a different MUD.");
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
        // ask user to either join a game or create one
        String gameName = StubInterface.getUserInput("Insert the game you want to join, or insert a new name to create a MUD game");
        if(!StubInterface.getAvailableGames().contains(gameName)) {
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
        LinkedList<String> availableGames = StubInterface.getAvailableGames();

        if(availableGames.size() < 1) {
            System.out.println("[There are no online games currently]");
            return;
        }
        // print online players on each game
        for(String gameName: availableGames) {
            System.out.println("+ " + gameName);
        }
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
     * @param userGame
     * @param gameName
     * @throws IOException
     * @throws MUDGameNotFoundException
     */
    private static void runGame(StubInterface serverHandle, GameImplementation userGame, String gameName) throws IOException, MUDGameNotFoundException {
        System.out.println("Game started!");
        String chooseSomething;
        boolean gameOver = false;
        while(!gameOver) {
            chooseSomething = getUserGameOutput(serverHandle, userGame);
            String choice = StubInterface.getUserInput(chooseSomething);
            clearScreen();

            switch(choice) {
                case "q":
                    // quit game
                    System.out.println("Quitting game...");
                    serverHandle.disconnect(User.getUserName());
                    gameOver = true;
                    break;
                case "s":
                    //switch to another game
                    clearScreen();
                    printOpenGames(serverHandle);
                    // ask user to either join a game or create one
                    String gameName2 = StubInterface.getUserInput("Insert the game you want to join");
                    break;
                case "n":
                    //create new game
                    System.out.println("Creating new game...");
                    String username = userGame.getUserName();
                    String gameName3 = StubInterface.getUserInput("Insert the name of the game you want to create");
                    //Thread newgame = new GameThread(username, gameName3);
                    break;
                case "":
                    // refresh
                    System.out.println("Refreshing...");
                    break;
                case "h":
                    // print help message
                    System.out.println("Currently available actions:");
                    System.out.println(getPrintableActions(serverHandle, userGame));
                    break;
                default:
                    if(isActionAvailable(userGame, serverHandle, choice)) {
                        System.out.println(doAction(userGame, serverHandle, choice));
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
     * @param userGame
     * @param serverHandle
     * @param choice
     * @return the result message of the user action
     * @throws RemoteException,MUDGameNotFoundException
     */
    private static String doAction(GameImplementation userGame, StubInterface serverHandle, String choice) throws RemoteException, MUDGameNotFoundException {
        String actionResult = String.format("Internal error:\nCould not perform '%s'. Try again.", choice);
        String[] action = getAction(choice);
        String userName = userGame.getUserName();

        switch (action[0]) {
            case "move":
                if(serverHandle.move(action[1], userName)) {
                    actionResult = String.format("You move %s.", action[1]);
                }
                break;
            case "pick":
                if(serverHandle.pick(action[1], userName)) {
                    actionResult = String.format("%s picked.", action[1]);
                }
                break;
            case "show-inventory":
                LinkedList<String> inputUserInventory = serverHandle.getUserInventory(action[1]);
                actionResult = String.format("%s's inventory:\n", action[1]);
                actionResult += getPrintableInventory(inputUserInventory);
            case "show-online-players":
                String gameName = userGame.getGameName();
                actionResult = "Online players at " + gameName + ":";
                actionResult += getPrintablePlayersAtGame(serverHandle, gameName);
            case "show-user-location":
                String location = serverHandle.getUserLocation(userName);
                String game = userGame.getGameName();
                actionResult = "You are at " + location + " in "+ game + ".";
                actionResult += getPrintablePlayersAtGame(serverHandle, game);
        }
        return actionResult;
    }

    /**
     * Method to check whether the action inputted by the user is available
     * @param userGame
     * @param serverHandle
     * @param choice
     * @return true if the inputted action is available, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static boolean isActionAvailable(GameImplementation userGame, StubInterface serverHandle, String choice) throws RemoteException, MUDGameNotFoundException {
        boolean isActionAvailable = false;
        String[] action = getAction(choice);
        switch (action[0]) {
            case "move":
                isActionAvailable = isDirectionAvailable(userGame, serverHandle, action[1]);
                break;
            case "pick":
                isActionAvailable = isThingAvailable(userGame, serverHandle, action[1]);
                break;
            case "show-inventory":
                String userLocation = serverHandle.getUserLocation(userGame.getUserName());
                String inputUserLocation = serverHandle.getUserLocation(action[1]);
                isActionAvailable = userLocation.equals(inputUserLocation);
                break;
            case "show-online-players":
                isActionAvailable = true;
                break;
        }
        return isActionAvailable;
    }

    /**
     * @param userGame
     * @param serverHandle
     * @param toTestInput
     * @return true if toTestInput is an available direction, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static boolean isThingAvailable(GameImplementation userGame, StubInterface serverHandle, String toTestInput) throws RemoteException, MUDGameNotFoundException {
        boolean isPickable = false;
        String userName = userGame.getUserName();
        String[] pickableThings = serverHandle.getPickableThings(userName);
        for(String thing: pickableThings) {
            if(toTestInput.equals(thing)) {
                isPickable = true;
            }
        }
        return isPickable;
    }



    /**
     * @param userGame
     * @param serverHandle
     * @param toTestInput
     * @return true if toTestInput is an available direction, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static boolean isDirectionAvailable(GameImplementation userGame, StubInterface serverHandle, String toTestInput) throws RemoteException, MUDGameNotFoundException {
        boolean isAvailable = false;
        String[] availableDirections = serverHandle.getDirections(userGame.getUserName());
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
     * @param userGame
     * @return a printable output to display the available actions that the user can take
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static String getPrintableActions(StubInterface serverHandle, GameImplementation userGame) throws RemoteException, MUDGameNotFoundException {
        String printableActions = "";
        String userName = getUserName();
        String location = serverHandle.getUserLocation(userName);
        printableActions += getPrintableDirections(serverHandle.getDirections(userName));
        printableActions += getPrintableThings(serverHandle.getPickableThings(userName));
        printableActions += getPrintableUsers(serverHandle.getNearUsers(userName));
        printableActions += "<show-online-players>";
        printableActions += "<show-user-location>";
        return printableActions;
    }

  /*  *//**
     * Method to get server handle from RMI registry
     * @param port
     * @param hostName
     * @return the server handle
     *//*
    private static StubInterface getServerHandle(int port, String hostName) throws MalformedURLException, NotBoundException, RemoteException {
        // get server handle from RMI registry
        String registeredURL = String.format("rmi://%s:%d/ShoutService", hostName, port);
        System.out.println(String.format("Looking up %s", registeredURL));
        return (StubInterface) Naming.lookup(registeredURL);
    }*/

    /**
     * Procedure to make the client join the MUD game
     * @param serverHandle
     * @return the interface to control the client
     * @throws IOException
     */
    private static GameImplementation joinServer(StubInterface serverHandle, String gameName) throws IOException {
        // create user instance
        System.out.println("Logging in...");
        String userName = StubInterface.getUserInput("Insert username:");
        GameImplementation userGame = new GameImplementation(gameUser, gameName, serverHandle, userName);
        serverHandle.connect(
                userGame.getUserName(),
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
     * @param userGame
     * @return game output string
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */
    private static String getUserGameOutput(StubInterface serverHandle, GameImplementation userGame) throws RemoteException, MUDGameNotFoundException {
        String userName = User.getUserName();
        String message = serverHandle.getMessage(userName);
        return String.format(
                "%sMake a move:\n(type 'h' to show available commands, 'n' for a new game, 's' to switch games, or 'q' to quit the game.)",
                message
        );
    }

/*    *//**
     * Method to get user input String from CLI
     * @return String input by the user
     * @throws IOException
     *//*
    private static String getUserInput(String inputMessage) throws IOException {
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in)
        );
        System.out.println(inputMessage);
        return input.readLine();
    }*/

    /**
     * @return the client's user name
     */

    public static String getUserName() {
        return userName;
    }

    /**
     * @return the game the client is currently connected to
     */

    public String getGameName() {
        return gameFocus;
    }

    /**
     * @param serverHandle
     * @param user
     * @return the list of items in the specified user's inventory
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */

    public LinkedList<String> getInventoryFromUser(StubInterface serverHandle, UserInterface user) throws RemoteException, MUDGameNotFoundException {
        return serverHandle.getUserInventory(getUserName());
    }

    /**
     * method to pick an object at given user's location
     * @param serverHandle
     * @param object
     * @return true if the object was picked, false otherwise
     * @throws RemoteException
     * @throws MUDGameNotFoundException
     */

    public boolean pick(StubInterface serverHandle, String object) throws RemoteException, MUDGameNotFoundException {
        return serverHandle.pick(object, this.getUserName());
    }

}
