package common.rmi;

import common.model.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteService extends Remote {
    Response handleLogin(String data) throws RemoteException, Exception;
    Response handleRegister(String data) throws RemoteException, Exception;
    Response handleViewAppointments() throws RemoteException, Exception;
    Response handleViewFinishedAppointments() throws RemoteException, Exception;
    Response handleViewCancelledAppointments() throws RemoteException, Exception;
    Response handleBookAppointment(String data) throws RemoteException, Exception;
    Response handleCancelAppointment(String data) throws RemoteException, Exception;
    Response handleCreateSchedule(String data) throws RemoteException, Exception;
    Response handleMarkFinishedAppointment(String data) throws RemoteException, Exception;
    Response handleDeleteCancelledAppointment(String data) throws RemoteException, Exception;
}