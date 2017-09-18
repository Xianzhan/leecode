package com.github.xianzhan.sudoku.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/9/18
 */
public class SAlert {
    private Alert.AlertType alertType;
    private String          title;
    private String          headerText;
    private String          contentText;

    SAlert(Alert.AlertType alertType, String title, String contentText) {
        this.alertType = alertType;
        this.title = title;
        this.contentText = contentText;
    }

    void show() {
        Alert alert = new Alert(alertType);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/sudoku/image/logo.png"));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

}
