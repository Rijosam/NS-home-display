package com.home.traininfo.admin;

import com.home.traininfo.application.TrainDepartureService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.Arrays;

import static com.home.traininfo.admin.Station.getStationCodeFromDisplayName;

@Route("admin")
public class AdminView extends VerticalLayout {

    private final TrainDepartureService trainDepartureService;

    public AdminView(TrainDepartureService trainDepartureService) {
        this.trainDepartureService = trainDepartureService;
        ComboBox<String> comboBox = new ComboBox<>("Select the station");
        comboBox.setItems(Arrays.stream(Station.values())
                .map(Station::getDisplayName).toList());

        comboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                callFunction(event.getValue());
            }
        });
        add(comboBox);
    }

    private void callFunction(String value) {
        Notification.show("Selected: " + value);
        trainDepartureService.setStationUicCode(getStationCodeFromDisplayName(value));
    }
}

