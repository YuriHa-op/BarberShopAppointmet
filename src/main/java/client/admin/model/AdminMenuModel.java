package client.admin.model;

import client.admin.view.CancelledAppointmentsView;
import client.admin.view.ViewAppointmentsView;
import client.admin.view.CreatedAppointmentsView;
import client.admin.view.FinishedAppointments;

public class AdminMenuModel {
    private ViewAppointmentsView viewAppointmentsView;
    private CreatedAppointmentsView createdAppointmentsView;
    private FinishedAppointments finishedAppointmentsView;
    private CancelledAppointmentsView cancelledAppointmentsView;

    public ViewAppointmentsView getViewAppointmentsView() {
        return viewAppointmentsView;
    }

    public void setViewAppointmentsView(ViewAppointmentsView viewAppointmentsView) {
        this.viewAppointmentsView = viewAppointmentsView;
    }

    public CreatedAppointmentsView getCreatedAppointmentsView() {
        return createdAppointmentsView;
    }

    public void setCreatedAppointmentsView(CreatedAppointmentsView createdAppointmentsView) {
        this.createdAppointmentsView = createdAppointmentsView;
    }

    public FinishedAppointments getFinishedAppointmentsView() {
        return finishedAppointmentsView;
    }

    public void setFinishedAppointmentsView(FinishedAppointments finishedAppointmentsView) {
        this.finishedAppointmentsView = finishedAppointmentsView;
    }
    public CancelledAppointmentsView getCancelledAppointmentsView() {
        return cancelledAppointmentsView;
    }

    public void setCancelledAppointmentsView(CancelledAppointmentsView cancelledAppointmentsView) {
        this.cancelledAppointmentsView = cancelledAppointmentsView;
    }
}
