package cs3524.solutions.rmishout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.LinkedList;

public class ShoutClientImplementation implements ShoutClientInterface, Serializable {
    // client application calling methods of the remote object
    private String userName;

    public ShoutClientImplementation(String userName) {
        this.userName = userName;
    }

    public static void main(String args[]) throws RemoteException {
        // retrieve user input
        if(args.length < 2) {
            System.err.println("Usage:\njava ShoutClientImplementation <hostname> <port>");
            return;
        }
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);

        setSecurityPolicy("rmishout.policy");

        try {
            // get server handle
            ShoutServerInterface serverHandle = getServerHandle(port, hostName);
            ShoutClientInterface user = joinServer(serverHandle);
            String chooseSomething;
            boolean gameOver = false;
            while(!gameOver) {
                chooseSomething = getUserGameOutput(serverHandle, user);
                String choice = getUserInput(chooseSomething);

                switch(choice) {
                    case "q":
                        // quit game
                        System.out.println("Quitting game...");
                        serverHandle.disconnect(user.getUserName());
                        gameOver = true;
                        break;
                    case "":
                        // refresh
                        System.out.println("Refreshing...");
                        break;
                    case "h":
                        // print help message
                        System.out.println(getPrintableActions(serverHandle, user));
                        break;
                    default:
                        if(isActionAvailable(user, serverHandle, choice)) {
                            System.out.println(doAction(user, serverHandle, choice));
                        } else {
                            // user input is invalid. Print error message and retry with old message and directions
                            System.err.println(String.format("'%s' is an invalid action. Try again.", choice));
                        }
                }
            }

            System.out.println("GAME OVER.");

        } catch (MalformedURLException e) {
            System.err.println("The provided URL is not valid.");
            System.err.println(e.getMessage());
        } catch (NotBoundException e) {
            System.err.println("The looked up URL has no associated binding.");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            // not sure what to print here apart from the exception message
            //System.err.println("There was an issue with the input.");
            System.err.println(e.getMessage());
        }
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
     * @param user
     * @param serverHandle
     * @param choice
     * @return the result message of the user action
     * @throws RemoteException
     */
    private static String doAction(ShoutClientInterface user, ShoutServerInterface serverHandle, String choice) throws RemoteException {
        String actionResult = String.format("Internal error:\nCould not perform '%s'. Try again.", choice);
        String[] action = getAction(choice);
        String userName = user.getUserName();

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
                actionResult = getPrintableInventory(inputUserInventory);
        }
        return actionResult;
    }

    private static boolean isActionAvailable(ShoutClientInterface user, ShoutServerInterface serverHandle, String choice) throws RemoteException {
        boolean isActionAvailable = false;
        String[] action = getAction(choice);
        switch (action[0]) {
            case "move":
                isActionAvailable = isDirectionAvailable(user, serverHandle, action[1]);
                break;
            case "pick":
                isActionAvailable = isThingAvailable(user, serverHandle, action[1]);
                break;
            case "show-inventory":
                String userLocation = serverHandle.getUserLocation(user.getUserName());
                String inputUserLocation = serverHandle.getUserLocation(action[1]);
                isActionAvailable = userLocation.equals(inputUserLocation);
        }
        return isActionAvailable;
    }

    /**
     * @param user
     * @param serverHandle
     * @param toTestInput
     * @return true if toTestInput is an available direction, false otherwise
     */
    private static boolean isThingAvailable(ShoutClientInterface user, ShoutServerInterface serverHandle, String toTestInput) throws RemoteException {
        boolean isPickable = false;
        String userName = user.getUserName();
        String[] pickableThings = serverHandle.getPickableThings(userName);
        for(String thing: pickableThings) {
            if(toTestInput.equals(thing)) {
                isPickable = true;
            }
        }
        return isPickable;
    }



    /**
     * @param user
     * @param serverHandle
     * @param toTestInput
     * @return true if toTestInput is an available direction, false otherwise
     * @throws RemoteException
     */
    private static boolean isDirectionAvailable(ShoutClientInterface user, ShoutServerInterface serverHandle, String toTestInput) throws RemoteException {
        boolean isAvailable = false;
        String[] availableDirections = serverHandle.getDirections(user.getUserName());
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
            printableDirections += "move " + direction + '\n';
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
            printableObjects += "pick " + object + '\n';
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
            printableUsers += "show-inventory " + user;
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
     * @param user
     * @return a printable output to display the available actions that the user can take
     */
    private static String getPrintableActions(ShoutServerInterface serverHandle, ShoutClientInterface user) throws RemoteException {
        String printableActions = "";
        String userName = user.getUserName();
        String location = serverHandle.getUserLocation(userName);
        printableActions += getPrintableDirections(serverHandle.getDirections(userName));
        printableActions += getPrintableThings(serverHandle.getDirections(userName));
        printableActions += getPrintableUsers(serverHandle.getUsersAtLocation(location));
        return printableActions;
    }

    /**
     * Method to get server handle from RMI registry
     * @param port
     * @param hostName
     * @return the server handle
     */
    private static ShoutServerInterface getServerHandle(int port, String hostName) throws MalformedURLException, NotBoundException, RemoteException {
        // get server handle from RMI registry
        String registeredURL = String.format("rmi://%s:%d/ShoutService", hostName, port);
        System.out.println(String.format("Looking up %s", registeredURL));
        return (ShoutServerInterface) Naming.lookup(registeredURL);
    }

    /**
     * Procedure to make the client join the MUD game
     * @param serverHandle
     * @return the interface to control the client
     * @throws IOException
     */
    private static ShoutClientInterface joinServer(ShoutServerInterface serverHandle) throws IOException {
        // create user instance
        System.out.println("Logging in...");
        String userName = getUserInput("Insert username:");
        ShoutClientInterface user = new ShoutClientImplementation(userName);
        serverHandle.connect(
                user.getUserName(),
                ""
        ); // TODO: Allow users to join different games (CGS B)
        System.out.println(
                String.format(
                        "Logged in as '%s'.",
                        userName
                )
        );
        String onlinePlayers = getOnlinePlayers(serverHandle);
        System.out.println(
                String.format(
                        "Online players:%s",
                        onlinePlayers
                )
        );
        return user;
    }

    private static String getOnlinePlayers(ShoutServerInterface serverHandle) throws RemoteException {
        String onlinePlayers = "";
        String[] players = serverHandle.getOnlinePlayers();
        for(String player: players) {
            onlinePlayers += "\n+ " + player;
        }
        return onlinePlayers;
    }

    /**
     * Method to get CLI output String for user to make a choice
     * @param serverHandle
     * @param user
     * @return game output string
     * @throws RemoteException
     */
    private static String getUserGameOutput(ShoutServerInterface serverHandle, ShoutClientInterface user) throws RemoteException {
        String userName = user.getUserName();
        return String.format(
                "%sMake a move:\n(type 'h' to show available commands or 'q' to quit the game.)",
                serverHandle.getMessage(userName)
        );
    }

    /**
     * Method to get user input String from CLI
     * @return String input by the user
     * @throws IOException
     */
    private static String getUserInput(String inputMessage) throws IOException {
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in)
        );
        System.out.println(inputMessage);
        return input.readLine();
    }

    /**
     * @return the client's user name
     */
    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public LinkedList<String> getInventoryFromUser(ShoutServerInterface serverHandle, ShoutClientInterface user) throws RemoteException {
        return serverHandle.getUserInventory(user.getUserName());
    }

    @Override
    public boolean pick(ShoutServerInterface serverHandle, String object) throws RemoteException {
        return serverHandle.pick(object, this.getUserName());
    }

}
