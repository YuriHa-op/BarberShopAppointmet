package server.callback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminCB extends Remote {
    // Fixed method name from "notifAdmin" to "notifyAdmin" for consistency
    void notifyAdmin(String message) throws RemoteException;
}