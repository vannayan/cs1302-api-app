package cs1302.api;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This app utilizes the Spotify API to extract an artist's name based on the Spotify ID inputted
 * by the user. The Spotify ID is the base-62 identifier found at the end of the URL for a
 * specified artist. Once the artist's name has been retrieved, that data will be utilized by the
 * SeatGeek API to generate a list of upcoming events for the specfified artist.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code GSON} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    // root
    Stage stage;
    Scene scene;
    VBox root;

    // idLayer
    HBox idLayer;
    Button helpButton;
    TextField idSearch;
    Button loadButton;

    // messageLayer
    HBox messageLayer;
    Label messageLabel;

    // textLayer
    ScrollPane textLayer;
    TextFlow textFlow;

    // apiLayer
    HBox apiLayer;
    Region apiRegion1;
    Label apiLabel;
    Region apiRegion2;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        stage = null;
        scene = null;
        root = new VBox(1);

        // Nodes for idLayer
        idLayer = new HBox(5);
        helpButton = new Button("Help");
        idSearch = new TextField(
            "Enter Artist's Spotify ID...");
        loadButton = new Button("Load");

        // Nodes for messageLayer
        messageLayer = new HBox();
        messageLabel = new Label(
            " Press the \"Help\" button for instructions on where to get an artist's Spotify ID.");

        // Nodes for textLayer
        textLayer = new ScrollPane();
        textFlow = new TextFlow();

        // Nodes for apiLayer
        apiLayer = new HBox();
        apiRegion1 = new Region();
        apiLabel = new Label(
            "Information provided by Spotify API and SeatGeek API.");
        apiRegion2 = new Region();
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        // Initialize root
        root.getChildren().addAll(idLayer, messageLayer, textLayer, apiLayer);

        // Initialize idLayer
        idLayer.getChildren().addAll(helpButton, idSearch, loadButton);
        HBox.setHgrow(idSearch, Priority.ALWAYS);

        // Initialize messageLayer
        messageLayer.getChildren().add(messageLabel);

        // Initialize textLayer
        textLayer.setContent(textFlow);
        textLayer.setPrefHeight(460);
        textFlow.setMaxWidth(630);

        // Initialize apiLayer
        apiLayer.getChildren().addAll(apiRegion1, apiLabel, apiRegion2);
        HBox.setHgrow(apiRegion1, Priority.ALWAYS);
        HBox.setHgrow(apiRegion2, Priority.ALWAYS);

        // Call buttonEvents
        buttonEvents();
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root, 640, 480);
        stage.setTitle("Spotify Artist Checker!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Method handles all button events.
     */
    public void buttonEvents() {
        // helpButton
        helpButton.setOnAction(e -> {
            helpAlert();
        });

    } // buttonEvents

    /**
     * Method pops an alert message that explains to users how to
     * retrieve an artist's Spotify ID.
     */
    public void helpAlert() {
        Alert helpAlert = new Alert(AlertType.INFORMATION);
        helpAlert.setTitle("Help");
        helpAlert.setHeaderText("Press \"OK\" to continue.");
        String content1 = "A Spotify ID is the base-62 identifier at the end of a Spotify URL. ";
        String content2 = "The URL can be obtained from Spotify's website in the search bar.";
        String contentSeperator = "\n------------------------------";
        String content3 = "\nAn example URL would look something like this:";
        String content4 = "\n\"https://open.spotify.com/artist/45eNHdiiabvmbp4erw26rg\"";
        String content5 = "\n\n\"45eNHdiiabvmbp4erw26rg\" would be the Spotify ID for ILLENIUM.";
        helpAlert.setContentText(
            content1 + content2 + contentSeperator + content3 + content4 + content5);
        helpAlert.showAndWait();
    } // infoAlert

} // ApiApp
