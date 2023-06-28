package com.home.application.views.main;

import com.home.application.model.TrainInfo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Main")
@Route(value = "")
public class MainView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public MainView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);



        Grid<TrainInfo> grid = new Grid<>();
        List<TrainInfo> trainInfos = new ArrayList<>();
        trainInfos.add(new TrainInfo("Utrecht Centraal","09:23:00","3","SPR", false))
        trainInfos.add(new TrainInfo("Arnhem Centraal","09:47:00","2","ST", false))
        trainInfos.add(new TrainInfo("Utrecht Centraal","09:53:00","3","SPR", false))

        grid.setItems(trainInfos);

        grid.addColumn(LitRenderer
                .<Person>of("<b>[[item.name]]</b>")
                .withProperty("name", Person::getName)
        ).setHeader("Name");
    }

}
