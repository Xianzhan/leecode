package com.github.xianzhan.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 描述：数独程序入口
 * @author https://gitee.com/zhangwanjun/sudoku
 *
 * @author Lee
 * @since 2017/9/18
 */
public class Main extends Application {
    private static Stage primaryStage;
    private static final double PRE_WIN_WIDTH  = 600;
    private static final double PRE_WIN_HEIGHT = 600;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/sudoku/mainui.fxml"));
        Scene scene = new Scene(root, PRE_WIN_WIDTH, PRE_WIN_HEIGHT);
        primaryStage.setTitle("Sudoku");
        primaryStage.getIcons().add(new Image("/sudoku/image/logo.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
