package cs3524.solutions.rmishout;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ShoutServerMainline {
    // the registrar, registers remote objects with the RMI registry
    public static void main(String args[]) {
        if(args.length < 2) {
            System.out.println("Usage:\njava ShoutServerMainline <registryport> <serverport>");
            return;
        }

        try {
            String hostName = (InetAddress.getLocalHost()).getCanonicalHostName();

            // specify port at which the rmiregistry will be listening for requests
            int registryPort = Integer.parseInt(args[0]);

            // specify port for the shout service
            int servicePort = Integer.parseInt(args[1]);

            /*
                give the JVM permission to contact DNS servers and
                interact with other JVM via internet protocols.
             */
            System.setProperty("java.security.policy", "rmishout.policy");
            System.setSecurityManager(new RMISecurityManager());

            // generate remote objects that will reside on this server
            ShoutServerImplementation shoutService = new ShoutServerImplementation();

            // export shout service to specified port and register the corresponding stub
            ShoutServerInterface shoutStub = (ShoutServerInterface) UnicastRemoteObject.exportObject(shoutService, servicePort);

            // create URL to uniquely identify the registered service
            String registeredURL = String.format("rmi://%s:%d/ShoutService", hostName, registryPort);
            System.out.println(String.format("Registering %s...", registeredURL));

            // register the stub of the remote object with the rmiregistry
            Naming.rebind(registeredURL, shoutStub);
        } catch (UnknownHostException e) {
            System.err.println("Cannot get local hostname.");
            System.err.println(e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Failed to register.");
            System.err.println(e.getMessage());
        } catch (MalformedURLException e) {
            System.err.println("The provided URL is not valid.");
            System.err.println(e.getMessage());
        }
    }
}
