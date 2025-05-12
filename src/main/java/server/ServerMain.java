package server;

import server.Implementation.MainIMPL;
import server.Interface.MainInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.InetAddress;

public class ServerMain {
    private static final int RMI_PORT = 7770;
    private static final String SERVICE_NAME = "RemoteService";
    private static final Logger LOGGER = Logger.getLogger(ServerMain.class.getName());

    public static void main(String[] args) {
        try {
            MainInterface service = new MainIMPL();
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.rebind(SERVICE_NAME, service);

            // Get server's IP address and hostname
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            String hostname = localHost.getHostName();

            System.out.println("RMI Server is running on port " + RMI_PORT);
            System.out.println("Server IP Address: " + ipAddress);
            System.out.println("Server Hostname: " + hostname);

        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Remote exception during server startup", e);
            System.exit(1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during server startuimport java.net.InetAddress;p", e);
            System.exit(1);
        }
    }
}