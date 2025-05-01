package app;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Logo
        Image logoImage = new Image(getClass().getResource("/resources/images/logo.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);

        // Title
        Label titleLabel = new Label("House Rental System");
        titleLabel.setId("title");

        // Username and Password Fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");

        // Layout for Fields and Button
        VBox formBox = new VBox(10, usernameField, passwordField, loginButton);
        formBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(20, logoView, titleLabel, formBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setId("contentBox");

        // Background Image
        Image bgImage = new Image(getClass().getResource("/resources/images/bg.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        contentBox.setBackground(new Background(backgroundImage));

        // Scene
        Scene scene = new Scene(contentBox, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());

        primaryStage.setTitle("Login - House Rental System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
