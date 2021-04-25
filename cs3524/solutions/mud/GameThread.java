package cs3524.solutions.mud;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;


public class GameThread extends Thread{
    final int _MaxGames = 5;
    private final String user;
    private final String game;
    private int _numofGames = 0;


    public GameThread(String username, String gameName) {
        Socket _userSocket;
        this.user = username;
        this.game = gameName;
        GameImplementation gameThread = new GameImplementation(user, game);
    }

        public void run() {
            while (!gameNumNotMax() == true){
            try {
                //GameImplementation gameThread = new GameImplementation(user, game);


            }
            catch (Exception e){
                        e.printStackTrace();
                    }
                }
        }

        boolean gameNumNotMax() {
            if (_numofGames >= _MaxGames)
                return false;
            _numofGames++;
            return true;
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
