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
            // get server handle from RMI registry
            String registeredURL = String.format("rmi://%s:%d/ShoutService", hostName, port);
            System.out.println(String.format("Looking up %s", registeredURL));
            ShoutServerInterface server = (ShoutServerInterface) Naming.lookup(registeredURL);

            // prepare input stream
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            // create user instance
            System.out.println("Logging in...");
            System.out.println("Insert username:");
            String userName = in.readLine();
            ShoutClientInterface user = new ShoutClientImplementation(userName);
            server.connect(user.getUserName(), ""); // TODO: Allow users to join different games (CGS B)
            System.out.println(String.format("Logged in as '%s'.", userName));

            /*
                provide user position and available moves
                e.g.   "A You are in a wood.
                        Move towards a direction:
                        A north B through the woods;
                        A east C through the woods."
             */
            String message = server.getMessage(user.getUserName());
            String directions[] = server.getDirections(user.getUserName());
            String choice;
            while(true) {
                System.out.println(String.format("%sChoose one:\n%s\n(or type 'q' to quit the game.)", message, getPrintableDirections(directions)));
                choice = in.readLine();

                // insert break statement
                if(choice.equals("q")) {
                    System.out.println("Quitting game...");
                    server.disconnect(userName);
                    break;
                }
                // check if choice is available
                if(isChoiceAvailable(choice, directions)) {
                    // invoke the server to move the user to new direction
                    boolean response = server.move(choice, user.getUserName());
                    // check if user was moved successfully
                    if(response) {
                        // get new message and available directions
                        message = server.getMessage(user.getUserName());
                        directions = server.getDirections(user.getUserName());
                    } else {
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
     * Method to check whether the choice inserted by the user is compatible with a list of available choices
     * @param toTestChoice String command inserted by the user
     * @param availableChoices String[] array of available choices
     * @return true if the inserted choice is available, false otherwise
     */
    private static boolean isChoiceAvailable(String toTestChoice, String[] availableChoices) {
        boolean isAvailable = false;
        for(String choice: availableChoices) {
            if(toTestChoice.equals(choice)) {
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

    private static String processInput() {
        return null;
    }

    /**
     * @return the client's user name
     */
    @Override
    public String getUserName() {
        return this.userName;
    }

}
