package com.home.traininfo.views;

import com.home.traininfo.application.TrainDepartureService;
import com.home.traininfo.domain.Status;
import com.home.traininfo.domain.TrainDeparture;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {

    public MainView(final TrainDepartureService trainDepartureService)  {
            Grid<TrainDeparture> grid = new Grid<>();
            List<TrainDeparture> trainDepartures = trainDepartureService.getDepartureInfo();
            grid.setItems(trainDepartures);
            grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
            grid.addColumn(TrainDeparture::actualDepartureTime).setHeader("Depart");
            grid.addColumn(createDirectionRenderer()).setHeader("Direction").setAutoWidth(true).setFlexGrow(0);
            grid.addColumn(createPlatformRenderer()).setHeader("Platform");
            grid.addColumn(TrainDeparture::trainCategory).setHeader("Train");
            grid.addColumn(createStatusComponentRenderer()).setHeader("Status");
            add(grid);
    }


    private static Renderer<TrainDeparture> createDirectionRenderer() {
        return LitRenderer.<TrainDeparture> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.direction} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "    via ${item.routeStations}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("direction", TrainDeparture::direction)
                .withProperty("routeStations", TrainDeparture::routeStations);
    }

    private static Renderer<TrainDeparture> createPlatformRenderer() {
        return LitRenderer.<TrainDeparture> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "    <span part=\"platformStyle\"> ${item.actualTrack} </span>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("actualTrack", TrainDeparture::actualTrack);
    }

    private static ComponentRenderer<Span, TrainDeparture> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private static final SerializableBiConsumer<Span, TrainDeparture> statusComponentUpdater
            = (span, trainDeparture) -> {
        span.getElement().setAttribute("theme", getTheme(trainDeparture.status()));
        span.setText(trainDeparture.status().name().replace('_', ' '));
    };

    private static String getTheme(Status status) {
        return switch (status) {
            case ON_STATION -> "badge success";
            case CANCELLED -> "badge error";
            case INCOMING, DEPARTED -> "badge contrast";
        };
    }

}
