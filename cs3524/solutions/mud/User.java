package cs3524.solutions.mud;

import java.io.*;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;


public class User implements StubInterface{
    public static String userName;
    public String gameFocus;
    public StubInterface serverHandle;
    private int liveGames = 0;
    private int maxGames = 5;
    LinkedList userGames = new LinkedList();
    //private String gameFocus;



    public User() {
        //String gameFocus;
    }
        public static void main (String args[]) throws RemoteException, MalformedURLException, NotBoundException {
            // retrieve user input
            if (args.length < 2) {
                System.err.println("Usage:\njava GameImplementation <hostname> <port>");
                return;
            }
            String hostName = args[0];
            int port = Integer.parseInt(args[1]);

            setSecurityPolicy("rmishout.policy");

            try {
                String userName = StubInterface.getUserInput("Insert username:");
                    // get server handle
                StubInterface serverHandle = StubInterface.setServerHandle(port, hostName);

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
            } //catch (MUDGameNotFoundException e) {
                //System.err.println("Your request could not be processed because the MUD is offline.\nJoin a different MUD.");


            //System.out.println("GAME OVER.");


    }


/*    public void run() {
            while (!gameNumNotMax() == true){
            try {
                //GameImplementation gameThread = new GameImplementation(user, game);


            }
            catch (Exception e){
                        e.printStackTrace();
                    }
                }
        }*/

     /*   boolean gameNumNotMax() {
            if (_numofGames >= _MaxGames)
                return false;
            _numofGames++;
            return true;
        }*/




    /**
     * Specifies the security policy and sets a security manager
     * @param policy String the security policy to be set
     */
    private static void setSecurityPolicy(String policy) {
        System.setProperty("java.security.policy", policy);
        System.setSecurityManager(new SecurityManager());

    }


    /**
     * @return the client's user name
     */

    public static String getUserName() {
        return userName;
    }


    public String getGameName() {
        return gameFocus;
    }
    /**
     * Method to get user input String from CLI
     * @return String input by the user
     * @throws IOException
     */


    @Override
    public boolean createNewGame(String gameName) throws RemoteException {
        return false;
    }

    @Override
    public boolean connect(String userName, String gameName) throws RemoteException {
        return false;
    }

    @Override
    public void disconnect(String userName) throws RemoteException, MUDGameNotFoundException {

    }

    @Override
    public String getMessage(String userName) throws RemoteException, MUDGameNotFoundException {
        return null;
    }

    @Override
    public String[] getDirections(String userName) throws RemoteException, MUDGameNotFoundException {
        return new String[0];
    }

    @Override
    public String[] getPickableThings(String userName) throws RemoteException, MUDGameNotFoundException {
        return new String[0];
    }

    @Override
    public boolean move(String direction, String userName) throws RemoteException, MUDGameNotFoundException {
        return false;
    }

    @Override
    public boolean pick(String object, String userName) throws RemoteException, MUDGameNotFoundException {
        return false;
    }

    @Override
    public LinkedList<String> getUserInventory(String userName) throws RemoteException, MUDGameNotFoundException {
        return null;
    }

    @Override
    public String getUserLocation(String userName) throws RemoteException, MUDGameNotFoundException {
        return null;
    }

    @Override
    public LinkedList<String> getNearUsers(String userName) throws RemoteException, MUDGameNotFoundException {
        return null;
    }

    @Override
    public String[] getOnlinePlayersAtGame(String gameName) throws RemoteException {
        return new String[0];
    }

    @Override
    public String[] getOnlinePlayers() throws RemoteException {
        return new String[0];
    }
}
    // serverHandle.connect(
//                user.getUserName(),
//                gameName
//        );
//        System.out.println(
//                String.format(
//                        "Logged in as '%s'.",
//                        userName
//                )
