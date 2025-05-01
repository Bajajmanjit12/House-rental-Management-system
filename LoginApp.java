package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Background Image with blur
        ImageView bgImageView = createBackgroundImage();

        // Main container (login panel)
        VBox mainContainer = createMainContainer();

        // StackPane as root
        StackPane root = new StackPane();
        root.getChildren().addAll(bgImageView, mainContainer);

        // Force login box to always be centered
        StackPane.setAlignment(mainContainer, Pos.CENTER);

        // Scene setup
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Stage setup
        primaryStage.setTitle("HouseRental System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);   // Optional: minimum size
        primaryStage.setMinHeight(600);
        primaryStage.show();

        // Make background image resize with window
        bgImageView.fitWidthProperty().bind(scene.widthProperty());
        bgImageView.fitHeightProperty().bind(scene.heightProperty());
    }


    private ImageView createBackgroundImage() {
        ImageView bgImageView = new ImageView(new Image(getClass().getResource("/images/bg.png").toExternalForm()));
        bgImageView.setPreserveRatio(false);
        bgImageView.setFitWidth(1920);
        bgImageView.setFitHeight(1080);

        // Modern blur effect
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(15);
        boxBlur.setHeight(15);
        boxBlur.setIterations(3);

        ColorAdjust dim = new ColorAdjust();
        dim.setBrightness(-0.3);

        bgImageView.setEffect(new Blend(BlendMode.MULTIPLY, dim, boxBlur));
        return bgImageView;
    }

    private VBox createMainContainer() {
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setMaxWidth(450);
        mainContainer.setMinHeight(550);
        mainContainer.setPadding(new Insets(20));

        // Logo with effects
        ImageView logoView = new ImageView(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        logoView.setFitHeight(100);
        logoView.setPreserveRatio(true);

        // Logo effects
        InnerShadow innerGlow = new InnerShadow();
        innerGlow.setColor(Color.WHITE.deriveColor(1, 1, 1, 0.5));
        innerGlow.setRadius(15);
        innerGlow.setChoke(0.8);

        DropShadow dropGlow = new DropShadow();
        dropGlow.setColor(Color.WHITE.deriveColor(1, 1, 1, 0.7));
        dropGlow.setRadius(25);
        dropGlow.setSpread(0.2);

        logoView.setEffect(new Blend(BlendMode.SCREEN, innerGlow, dropGlow));

        // Heading
        Text heading = new Text("Welcome to HouseRental");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        heading.setFill(Color.WHITE);

        // Auth container box
        VBox authContainer = new VBox(15);
        authContainer.setAlignment(Pos.CENTER);
        authContainer.getStyleClass().add("auth-container");
        authContainer.setPadding(new Insets(25, 30, 30, 30));

        // Tab pane for Login/Signup
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("auth-tab-pane");

        // Login Tab
        Tab loginTab = new Tab("Login");
        loginTab.setClosable(false);
        loginTab.setContent(createAuthForm(false));

        // Signup Tab
        Tab signupTab = new Tab("Sign Up");
        signupTab.setClosable(false);
        signupTab.setContent(createAuthForm(true));

        tabPane.getTabs().addAll(loginTab, signupTab);

        // Footer
        Text footer = new Text("© 2025 HouseRental System");
        footer.setFill(Color.WHITE);
        footer.setFont(Font.font(10));
        footer.setOpacity(0.7);

        // Add all to auth container
        authContainer.getChildren().add(tabPane);

        // Add all to main container
        mainContainer.getChildren().addAll(logoView, heading, authContainer, footer);

        return mainContainer;
    }

    private VBox createAuthForm(boolean isSignup) {
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);

        // User Type
        ComboBox<String> userType = new ComboBox<>();
        userType.getItems().addAll("Buyer", "Seller", "Admin");
        userType.setPromptText("Select Role");
        userType.getStyleClass().add("auth-combo");

        // Form fields
        TextField username = createStyledField("Username");
        PasswordField password = createStyledField("Password");

        // Additional fields for signup
        TextField email = createStyledField("Email");
        PasswordField confirmPassword = createStyledField("Confirm Password");

        // Action button
        Button actionButton = new Button(isSignup ? "Sign Up" : "Login");
        actionButton.getStyleClass().add("auth-button");

        // Add hover effect
        setupButtonEffects(actionButton);

        // Add fields to form
        form.getChildren().addAll(userType, username);
        if (isSignup) {
            form.getChildren().addAll(email);
        }
        form.getChildren().addAll(password);
        if (isSignup) {
            form.getChildren().addAll(confirmPassword);
        }
        form.getChildren().add(actionButton);

        return form;
    }

    private <T extends TextInputControl> T createStyledField(String prompt) {
        T field = (T) (prompt.toLowerCase().contains("password") ?
                new PasswordField() : new TextField());
        field.setPromptText(prompt);
        field.getStyleClass().add("auth-input");
        return field;
    }

    private void setupButtonEffects(Button button) {
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #00a383;");
            button.setEffect(new DropShadow(10, Color.rgb(0, 180, 150, 0.5)));
        });

        // Normal state
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #00b894;");
            button.setEffect(null);
        });
    }

    public static void main(String[] args) {
        // Configure system properties to avoid warnings
        System.setProperty("prism.marlin.log", "false");
        System.setProperty("prism.verbose", "false");
        launch(args);
    }
}
