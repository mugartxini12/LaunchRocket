package titan.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LanderIntroScene extends GuiMain {
    public static void setLanderIntroScene() {
        StackPane beginPane = new StackPane();
        landerIntroScene = new Scene(beginPane, screenBounds.getWidth(), screenBounds.getHeight());
        landerIntroScene.getStylesheets().add("Stylesheet.css");

        VBox introBox = new VBox(50);
        Label introLabel = new Label("Titan Landing!");
        introLabel.getStyleClass().add("introLabel");
        landerBeginButton = new Button("Start!");

        introBox.getChildren().addAll(introLabel, landerBeginButton);

        landerBeginButton.setOnAction(e -> {
            singleStage.setScene(landerVisualiserScene);
            singleStage.setFullScreen(true);
            singleStage.setResizable(false);
            singleStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        });
        landerBeginButton.setPrefSize(310, 75);

        introBox.setAlignment(Pos.CENTER);
        beginPane.getChildren().add(introBox);
    }

}
