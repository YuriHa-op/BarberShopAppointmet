package server.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCB extends Remote {
    // Fixed method name from "notifClient" to "notifyClient" for consistency
    void notifyClient(String message) throws RemoteException;
}