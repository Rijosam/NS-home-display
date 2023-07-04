package com.home.traininfo.views;

import com.home.traininfo.application.TrainDepartureService;
import com.home.traininfo.domain.TrainDeparture;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {

    public MainView(final TrainDepartureService trainDepartureService) {


        Grid<TrainDeparture> grid = new Grid<>();
        List<TrainDeparture> trainDepartures = trainDepartureService.getDepartureInfoService();

        grid.setItems(trainDepartures);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(TrainDeparture::actualDepartureTime).setHeader("Depart");
        grid.addColumn(TrainDeparture::direction).setHeader("Direction");
        grid.addColumn(TrainDeparture::actualTrack).setHeader("Platform");
        grid.addColumn(TrainDeparture::trainCategory).setHeader("Train");

        /*grid.addColumn(
                        LitRenderer
                                .<TrainInfo>of("<b>${item.direction}</b>")
                                .withProperty("direction", trainInfo -> trainInfo.direction()))
                .setHeader("Direction");*/
        add(grid);
    }

}
