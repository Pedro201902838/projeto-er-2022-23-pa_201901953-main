package pt.pa.controller;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pt.pa.Controller.NetworkController;
import pt.pa.model.Route;

import javax.xml.soap.Text;

/**
 *
 * @author Bruno Silva
 */
public class CustomContainer extends BorderPane {

    ListView<Route> routes;
    public CustomContainer(SmartGraphPanel graphView, NetworkController controller) {
        routes = new ListView<>();
        TextField txt = new TextField();
        setCenter(new ContentZoomPane(graphView));

        //create bottom pane with controls
        HBox bottom = new HBox(10);

        CheckBox automatic = new CheckBox("Automatic layout");
        automatic.selectedProperty().bindBidirectional(graphView.automaticLayoutProperty());

        Button b = new Button("undo");
        b.setOnAction(event ->{
            controller.undo();
        });
        Button b3 = new Button("showall");
        b3.setOnAction(event ->{
            controller.showAll();
        });
        Button b2 = new Button("removeEdge");
        b2.setOnAction(event ->{
            controller.removeRoute(txt.getText());
            //controller.undo();
        });



        bottom.getChildren().add(automatic);
        bottom.getChildren().add(b);
        bottom.getChildren().add(b2);
        bottom.getChildren().add(b3);
        bottom.getChildren().add(txt);
        setBottom(bottom);
    }





}
