package client.admin.controller;

import client.admin.model.CancelledAppointmentsModel;
import client.admin.view.CancelledAppointmentsView;
import common.service.AppointmentService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;

public class CancelledAppointmentsController {
    private CancelledAppointmentsView view;
    private CancelledAppointmentsModel model;
    private AppointmentService appointmentService;

    public CancelledAppointmentsController(CancelledAppointmentsView view, CancelledAppointmentsModel model) {
        this.view = view;
        this.model = model;
        this.appointmentService = AppointmentService.getInstance();

        initController();
        refreshAppointmentsTable();
    }

    private void initController() {
        view.getRefreshButton().addActionListener(this::refreshButtonClicked);
        view.getCloseButton().addActionListener(e -> view.dispose());
        view.getDeleteButton().addActionListener(this::deleteSelectedAppointments);
        view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchAppointments();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchAppointments();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchAppointments();
            }

        });
    }

    private void refreshButtonClicked(ActionEvent e) {
        refreshAppointmentsTable();
    }

    private void refreshAppointmentsTable() {
        try {
            String[][] data = model.getCancelledAppointmentsData();
            view.refreshTable(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                "Error loading cancelled appointments: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


   private void deleteSelectedAppointments(ActionEvent e) {
       int[] selectedRows = view.getAppointmentsTable().getSelectedRows();

       if (selectedRows.length == 0) {
           JOptionPane.showMessageDialog(view,
                   "Please select at least one appointment to delete",
                   "No Selection", JOptionPane.WARNING_MESSAGE);
           return;
       }

       int confirm = JOptionPane.showConfirmDialog(view,
               "Are you sure you want to permanently delete " + selectedRows.length + " appointment(s)?",
               "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

       if (confirm == JOptionPane.YES_OPTION) {
           try {
               for (int i = selectedRows.length - 1; i >= 0; i--) {
                   int modelRow = view.getAppointmentsTable().convertRowIndexToModel(selectedRows[i]);
                   String appointmentId = (String) view.getTableModel().getValueAt(modelRow, 0);
                   appointmentService.deleteCancelledAppointment(appointmentId);
               }
               refreshAppointmentsTable();
               JOptionPane.showMessageDialog(view,
                       "Selected appointments successfully deleted",
                       "Success", JOptionPane.INFORMATION_MESSAGE);
           } catch (Exception ex) {
               JOptionPane.showMessageDialog(view,
                       "Error deleting appointments: " + ex.getMessage(),
                       "Error", JOptionPane.ERROR_MESSAGE);
           }
       }
   }

    private void searchAppointments() {
        String text = view.getSearchField().getText();
        appointmentService.searchAppointments(view.getSorter(), text);
    }
}