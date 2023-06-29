package com.home.application.views.main;

import com.home.application.model.TrainInfo;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {

    public MainView() {

        Grid<TrainInfo> grid = new Grid<>();
        List<TrainInfo> trainInfos = new ArrayList<>();
        trainInfos.add(new TrainInfo("Utrecht Centraal", "09:23:00", "3", "SPR", false));
        trainInfos.add(new TrainInfo("Arnhem Centraal", "09:47:00", "2", "ST", false));
        trainInfos.add(new TrainInfo("Utrecht Centraal", "09:53:00", "3", "SPR", false));


        grid.setItems(trainInfos);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(TrainInfo::direction).setHeader("Direction");
        grid.addColumn(TrainInfo::actualDepartureTime).setHeader("Depart");
        grid.addColumn(TrainInfo::actualTrack).setHeader("Platform");
        grid.addColumn(TrainInfo::trainCategory).setHeader("Train");

        /*grid.addColumn(
                        LitRenderer
                                .<TrainInfo>of("<b>${item.direction}</b>")
                                .withProperty("direction", trainInfo -> trainInfo.direction()))
                .setHeader("Direction");*/

        add(grid);
    }

}
