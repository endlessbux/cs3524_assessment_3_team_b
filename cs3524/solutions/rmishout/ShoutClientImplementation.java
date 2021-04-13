package cs3524.solutions.rmishout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
            String makeAMoveMessage;
            while(true) {
                makeAMoveMessage = getUserGameOutput(serverHandle, user);
                String choice = getUserInput(makeAMoveMessage);

                if(choice.equals("q")) {
                    // quit game
                    System.out.println("Quitting game...");
                    serverHandle.disconnect(user.getUserName());
                    break;
                } else if(choice.equals("\n")) {
                    // refresh message
                    System.out.println("Refreshing...");
                    continue;
                } else if(isDirectionAvailable(choice, serverHandle.getDirections(user.getUserName()))) {
                    // invoke the server to move the user to given direction
                    boolean response = serverHandle.move(choice, user.getUserName());
                    // check if user was moved successfully
                    if(!response) {
                        // user input is valid but the user could not be moved
                        System.err.println(String.format("Internal error: the user could not be moved to %s. Please try again.", choice));
                    }
                } else {
                    // user input is invalid. Print error message and retry with old message and directions
                    System.err.println(String.format("'%s' is an invalid move. Try again.", choice));
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
     * Method to check whether the direction inserted by the user is compatible with a list of available directions
     * @param toTestInput String command inserted by the user
     * @param availableDirections String[] array of available directions
     * @return true if the inserted choice is available, false otherwise
     */
    private static boolean isDirectionAvailable(String toTestInput, String[] availableDirections) {
        boolean isAvailable = false;
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
        for(int i=0; i < directions.length; i++) {
            printableDirections += "Move " + directions[i] + '\n';
        }
        return printableDirections;
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

    private static ShoutClientInterface joinServer(ShoutServerInterface serverHandle) throws IOException {
        // create user instance
        System.out.println("Logging in...");
        String userName = getUserInput("Insert username:");
        ShoutClientInterface user = new ShoutClientImplementation(userName);
        serverHandle.connect(user.getUserName(), ""); // TODO: Allow users to join different games (CGS B)
        System.out.println(String.format("Logged in as '%s'.", userName));
        return user;
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
                "%sChoose one:\n%s\n(or type 'q' to quit the game.)",
                serverHandle.getMessage(userName),
                getPrintableDirections(
                        serverHandle.getDirections(userName)
                )
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

}
