package client.admin.controller;

import client.admin.model.CreatedScheduleModel;
import client.admin.view.CreatedScheduleView;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreatedScheduleController {
    private CreatedScheduleView view;
    private CreatedScheduleModel model;

    public CreatedScheduleController(CreatedScheduleView view) {
        this.view = view;
        this.model = new CreatedScheduleModel();
        initController();
    }

    private void initController() {
        JDatePickerImpl datePicker = view.getDateChooser();
        datePicker.getModel().addChangeListener(e -> updateAvailableTimes());

        view.getStartTimeCombo().addActionListener(e -> updateEndTimeOptions());
        view.getCreateButton().addActionListener(e -> createScheduleSlot());
        view.getCancelButton().addActionListener(e -> view.dispose());

        updateAvailableTimes();
    }

    private void updateAvailableTimes() {
        Date selectedDate = (Date) view.getDateChooser().getModel().getValue();
        if (selectedDate == null) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(selectedDate);

        List<String> availableTimes = model.getAvailableStartTimes(dateStr, view.getTimeSlots());
        JComboBox<String> startCombo = view.getStartTimeCombo();
        startCombo.removeAllItems();

        for (String time : availableTimes) {
            startCombo.addItem(time);
        }

        if (startCombo.getItemCount() > 0) {
            startCombo.setSelectedIndex(0);
            updateEndTimeOptions();
        }
    }

    private void updateEndTimeOptions() {
        JComboBox<String> startCombo = view.getStartTimeCombo();
        JComboBox<String> endCombo = view.getEndTimeCombo();

        if (startCombo.getSelectedItem() == null) return;

        String selectedStartTime = startCombo.getSelectedItem().toString();
        int startIndex = Arrays.asList(view.getTimeSlots()).indexOf(selectedStartTime);

        if (startIndex >= 0 && startIndex < view.getTimeSlots().length - 1) {
            endCombo.removeAllItems();
            endCombo.addItem(view.getTimeSlots()[startIndex + 1]);
            endCombo.setSelectedIndex(0);
        }
    }

    private void createScheduleSlot() {
        try {
            Date selectedDate = (Date) view.getDateChooser().getModel().getValue();
            String startTime = view.getStartTimeCombo().getSelectedItem().toString();
            String endTime = view.getEndTimeCombo().getSelectedItem().toString();

            if (selectedDate == null) {
                view.showMessage("Please select a date.");
                return;
            }

            if (selectedDate.before(new Date())) {
                view.showMessage("Cannot create appointments for past dates.");
                return;
            }

            boolean success = model.createScheduleSlot(selectedDate, startTime, endTime);
            if (success) {
                view.showMessage("Schedule slot created successfully!");
                view.dispose();
            } else {
                view.showMessage("Failed to create schedule slot.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Error: " + e.getMessage());
        }
    }
}