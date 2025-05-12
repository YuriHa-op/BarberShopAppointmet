package server.Interface;

import common.model.Request;
import common.model.Response;
import server.callback.ClientCB;
import common.rmi.RemoteService;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainInterface extends RemoteService {
    Response processRequest(Request request) throws RemoteException;


    Response handleLogin(String data) throws RemoteException, Exception;
    Response handleRegister(String data) throws RemoteException, Exception;
    Response handleViewAppointments() throws RemoteException, Exception;
    Response handleViewFinishedAppointments() throws RemoteException, Exception;
    Response handleBookAppointment(String data) throws RemoteException, Exception;
    Response handleCancelAppointment(String data) throws RemoteException, Exception;
    Response handleCreateSchedule(String data) throws RemoteException, Exception;
    Response handleViewCancelledAppointments() throws RemoteException, Exception;
}
