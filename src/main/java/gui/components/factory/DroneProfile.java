package main.java.gui.components.factory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.gui.components.buttons.CustomButton;
import main.java.model.DroneBase;
import main.java.util.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Factory class for creating and managing drone profile views.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>{@link #DroneProfile(DroneBase) DroneProfile constructor}: Initialize a drone profile with the given drone.</li>
 *     <li>{@link #setupDroneProfile(DroneBase) setupDroneProfile}: Set up the drone profile view with the given drone.</li>
 *     <li>{@link #createDroneIdLabel(DroneBase) createDroneIdLabel}: Create a label for the drone ID.</li>
 *     <li>{@link #createLabelList(DroneBase) createLabelList}: Create a list of labels for drone details.</li>
 *     <li>{@link #setupLabelsVBox(List) setupLabelsVBox}: Set up the VBox for labels :: drone type details.</li>
 *     <li>{@link #setupMoreButton() setupMoreButton}: Set up the 'More' button that shows the other 5 labels .</li>
 *     <li>{@link #toggleAdditionalLabelsVisibility() toggleAdditionalLabelsVisibility}: Toggle the visibility of additional labels.</li>
 *     <li>{@link #createLabel(String, String) createLabel}: Create a label with given text and value.</li>
 * </ul>
 * </p>
 */

public class DroneProfile extends HBox {

    private static final int DETAIL_COUNT = 5;
    private final VBox mainLabelsVBox = new VBox();
    private final VBox additionalLabelsVBox = new VBox();

    public DroneProfile(DroneBase drone) {
        setupDroneProfile(drone);
    }

    private void setupDroneProfile(DroneBase drone) {
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setPrefSize(340, 100);

        Label droneIdLabel = createDroneIdLabel(drone);
        List<Label> labels = createLabelList(drone);
        setupLabelsVBox(labels);

        CustomButton moreButton = setupMoreButton();

        VBox allLabelsVBox = new VBox(mainLabelsVBox, additionalLabelsVBox);
        HBox hBox = new HBox(allLabelsVBox, moreButton);
        AnchorPane.setRightAnchor(moreButton, 10.0);
        AnchorPane.setTopAnchor(moreButton, 0.0);

        AnchorPane anchorPane = new AnchorPane(hBox);

        this.getChildren().addAll(droneIdLabel, anchorPane);
        this.getStyleClass().add("rectangle");
    }

    private Label createDroneIdLabel(DroneBase drone) {
        Label droneIdLabel = new Label(drone.getDroneDetails().get(Constants.DRONE_ID).toString());
        droneIdLabel.setPrefSize(120, 100);
        droneIdLabel.getStyleClass().add("square");
        return droneIdLabel;
    }

    private void setupLabelsVBox(List<Label> labels) {
        mainLabelsVBox.getChildren().addAll(labels.subList(0, DETAIL_COUNT));
        additionalLabelsVBox.getChildren().addAll(labels.subList(DETAIL_COUNT, labels.size()));
        additionalLabelsVBox.managedProperty().bind(additionalLabelsVBox.visibleProperty());
        additionalLabelsVBox.setVisible(false);
    }

    private List<Label> createLabelList(DroneBase drone) {
        return Arrays.asList(
                createLabel("Serial Number : ", drone.getDroneDetails().get(Constants.SERIAL_NUMBER).toString()),
                createLabel("Carriage Weight : ", drone.getDroneDetails().get(Constants.CARRIAGE_WEIGHT).toString()),
                createLabel("Carriage Type : ", drone.getDroneDetails().get(Constants.CARRIAGE_TYPE).toString()),
                createLabel("Type Name : ", drone.getDroneTypeInfo().get(Constants.TYPE_NAME).toString()),
                createLabel("Manufacturer : ", drone.getDroneTypeInfo().get(Constants.MANUFACTURER).toString()),
                createLabel("Weight : ", drone.getDroneTypeInfo().get(Constants.WEIGHT).toString()),
                createLabel("Max Speed : ", drone.getDroneTypeInfo().get(Constants.MAX_SPEED).toString()),
                createLabel("Battery Capacity : ", drone.getDroneTypeInfo().get(Constants.BATTERY_CAPACITY).toString()),
                createLabel("Control Range : ", drone.getDroneTypeInfo().get(Constants.CONTROL_RANGE).toString()),
                createLabel("Max Carriage : ", drone.getDroneTypeInfo().get(Constants.MAX_CARRIAGE).toString())
        );
    }

    private CustomButton setupMoreButton() {
        CustomButton moreButton = new CustomButton("", "plus.png");
        moreButton.getStyleClass().clear();
        moreButton.getStyleClass().add("more-button");
        moreButton.setOnAction(event -> toggleAdditionalLabelsVisibility());

        return moreButton;
    }

    private void toggleAdditionalLabelsVisibility() {
        additionalLabelsVBox.setVisible(!additionalLabelsVBox.isVisible());
    }

    private Label createLabel(String text, String value) {
        Label label = new Label(text + value);
        label.getStyleClass().add("profile-detail");
        return label;
    }
}