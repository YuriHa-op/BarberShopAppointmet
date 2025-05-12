package server.Implementation;

import common.model.Request;
import server.Interface.MainInterface;
import server.callback.ClientCB;
import server.callback.ServerCB;
import server.controller.BusinessLogic;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import common.model.Response;

public class MainIMPL extends UnicastRemoteObject implements MainInterface {

    private BusinessLogic businessLogic;

    public MainIMPL() throws RemoteException {
        super();
        try {
            this.businessLogic = new BusinessLogic();
        } catch (RemoteException e) {
            throw e;
        }
    }

    @Override
    public Response processRequest(Request request) throws RemoteException {
        return businessLogic.process(request);
    }

    @Override
    public Response handleLogin(String data) throws RemoteException, Exception {
        return businessLogic.handleLogin(data);
    }

    @Override
    public Response handleRegister(String data) throws RemoteException, Exception {
        return businessLogic.handleRegister(data);
    }

    @Override
    public Response handleViewAppointments() throws RemoteException, Exception {
        return businessLogic.handleViewAppointments();
    }

    @Override
    public Response handleViewFinishedAppointments() throws RemoteException, Exception {
        return businessLogic.handleViewFinishedAppointments();
    }

    @Override
    public Response handleBookAppointment(String data) throws RemoteException, Exception {
        return businessLogic.handleBookAppointment(data);
    }

    @Override
    public Response handleCancelAppointment(String data) throws RemoteException, Exception {
        return businessLogic.handleCancelAppointment(data);
    }

    @Override
    public Response handleCreateSchedule(String data) throws RemoteException, Exception {
        return businessLogic.handleCreateSchedule(data);
    }

    @Override
    public Response handleMarkFinishedAppointment(String data) throws RemoteException, Exception {
        return businessLogic.handleMarkFinishedAppointment(data);
    }

    @Override
    public Response handleViewCancelledAppointments() throws RemoteException, Exception {
        return businessLogic.handleViewCancelledAppointments();
    }
    @Override
    public Response handleDeleteCancelledAppointment(String data) throws RemoteException, Exception {
        return businessLogic.handleDeleteCancelledAppointment(data);
    }
}
