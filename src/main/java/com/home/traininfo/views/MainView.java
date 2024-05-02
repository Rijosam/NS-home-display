package com.home.traininfo.views;

import com.home.traininfo.application.TrainDepartureService;
import com.home.traininfo.domain.Status;
import com.home.traininfo.domain.TrainDeparture;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {
    private final TrainDepartureService trainDepartureService;
    private final Grid<TrainDeparture> grid;

    public MainView(TrainDepartureService trainDepartureService) {
        this.trainDepartureService = trainDepartureService;
        grid = new Grid<>();
        List<TrainDeparture> trainDepartures = trainDepartureService.getDepartureInfo();
        grid.setItems(trainDepartures);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(createDepartureRenderer()).setHeader("DEPART");
        grid.addColumn(createDirectionRenderer()).setHeader("DIRECTION").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createPlatformRenderer()).setHeader("PLATFORM");
        grid.addColumn(createStatusComponentRenderer()).setHeader("STATUS");
        grid.addColumn(createTimeToDepartureRenderer()).setHeader("DEPART IN");
        add(grid);
    }

    // executed at the start of every minute.
    @Scheduled(cron = "0 * * * * ?")
    public void updateGrid() {
        var trainDepartures = trainDepartureService.getDepartureInfo();
        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(trainDepartures)));
    }

    private static Renderer<TrainDeparture> createDirectionRenderer() {
        return new ComponentRenderer<>(trainDeparture -> {
            var direction = new Span(trainDeparture.direction());
            var lines = new FlexLayout(direction);
            lines.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
            if (StringUtils.isNotBlank(trainDeparture.routeStations())) {
                Span routeStations = new Span("via " + trainDeparture.routeStations());
                routeStations.getStyle().set("color", "var(--lumo-secondary-text-color)");
                routeStations.getStyle().set("font-size", "var(--lumo-font-size-xl)");
                lines.add(routeStations);
            }
            var layout = new HorizontalLayout(lines);
            layout.setJustifyContentMode(JustifyContentMode.START);
            return layout;
        });
    }

    private static Renderer<TrainDeparture> createDepartureRenderer() {
        return new ComponentRenderer<>(trainDeparture -> {
            var departureTime = new Span(trainDeparture.departureTime());
            var lines = new FlexLayout(departureTime);
            lines.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
            if (StringUtils.isNotBlank(trainDeparture.departureDelayInMinutes())) {
                var delay = new Span("+ " + trainDeparture.departureDelayInMinutes());
                delay.getStyle().set("color", "#CA150C");
                delay.getStyle().set("font-size", "var(--lumo-font-size-xxl)");
                delay.getStyle().set("align-self", "flex-end");
                lines.add(delay);
            }
            var layout = new HorizontalLayout(lines);
            layout.setJustifyContentMode(JustifyContentMode.START);
            return layout;
        });
    }

    private static Renderer<TrainDeparture> createPlatformRenderer() {
        return LitRenderer.<TrainDeparture>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "    <span part=\"platformStyle\"> ${item.actualTrack} </span>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("actualTrack", TrainDeparture::actualTrack);
    }

    private static Renderer<TrainDeparture> createTimeToDepartureRenderer() {
        return LitRenderer.<TrainDeparture>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "    <span part=\"departInStyle\">${item.timeToDeparture}</span>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("timeToDeparture", TrainDeparture::timeToDeparture);
    }

    private static ComponentRenderer<Span, TrainDeparture> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private static final SerializableBiConsumer<Span, TrainDeparture> statusComponentUpdater
            = (span, trainDeparture) -> {
        span.getElement().setAttribute("theme", getTheme(trainDeparture.status()));
        span.getStyle().set("font-size", "var(--lumo-font-size-xxl)");
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
