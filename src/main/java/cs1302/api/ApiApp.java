package cs1302.api;

import cs1302.api.SeatGeekEventsResponse.Event;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

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
        .build(); // HTTP_CLIENT

    /** Google {@code GSON} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create(); // GSON

    // root
    private Stage stage;
    private Scene scene;
    private VBox root;

    // idLayer
    private HBox idLayer;
    private Button helpButton;
    private TextField idSearch;
    private Button loadButton;

    // messageLayer
    private HBox messageLayer;
    private Label messageLabel;

    // textLayer
    private ScrollPane textLayer;
    private TextFlow textFlow;

    // apiLayer
    private HBox apiLayer;
    private Region apiRegion1;
    private Label apiLabel;
    private Region apiRegion2;

    // apiCredentials
    private static final String CONFIG_PATH = "resources/config.properties";

    // Spotify API
    private static final String TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token";
    private static final String ARTISTS_ENDPOINT = "https://api.spotify.com/v1/artists/";
    private String spotifyClientID;
    private String spotifyClientSecret;
    private String spotifyID;
    private String spotifyURI;
    private String spotifyToken;
    private String artistName;

    // SeatGeek API
    private static final String EVENTS_ENDPOINT = "https://api.seatgeek.com/2/events";
    private String seatgeekClientID;
    private String seatgeekClientSecret;
    private String seatgeekURI;
    private SeatGeekEventsResponse artistEvents;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        stage = null;
        scene = null;
        root = new VBox(1);

        // idLayer
        idLayer = new HBox(5);
        helpButton = new Button("Help");
        idSearch = new TextField(
            "45eNHdiiabvmbp4erw26rg");
        loadButton = new Button("Load");

        // messageLayer
        messageLayer = new HBox();
        messageLabel = new Label(
            "Press the \"Help\" button for instructions on where to get an artist's Spotify ID.");

        // textLayer
        textLayer = new ScrollPane();
        textFlow = new TextFlow();

        // apiLayer
        apiLayer = new HBox();
        apiRegion1 = new Region();
        apiLabel = new Label(
            "Data provided by Spotify API and SeatGeek API.");
        apiRegion2 = new Region();

        // apiCredentials
        apiCredentials();

        // getSpotifyToken
        spotifyToken = getSpotifyToken();
        artistName = getArtistName();

        // buttonEvents
        buttonEvents();

    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        // root
        root.getChildren().addAll(idLayer, messageLayer, textLayer, apiLayer);

        // idLayer
        idLayer.getChildren().addAll(helpButton, idSearch, loadButton);
        HBox.setHgrow(idSearch, Priority.ALWAYS);

        // messageLayer
        messageLayer.getChildren().add(messageLabel);

        // textLayer
        textLayer.setContent(textFlow);
        textLayer.setPrefHeight(450);
        textFlow.setMaxWidth(635);

        // apiLayer
        apiLayer.getChildren().addAll(apiRegion1, apiLabel, apiRegion2);
        HBox.setHgrow(apiRegion1, Priority.ALWAYS);
        HBox.setHgrow(apiRegion2, Priority.ALWAYS);
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root, 640, 480);
        stage.setTitle("────── · · ⊱Spotify Artist Events⊰ · · ──────");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Method pops an alert message that explains to users how to
     * retrieve an artist's Spotify ID.
     */
    public void helpAlert() {
        Platform.runLater(() -> {
            Alert helpAlert = new Alert(AlertType.INFORMATION);
            helpAlert.setTitle("Help");
            helpAlert.setHeaderText("Press \"OK\" to continue.");
            String content1 = "A Spotify ID is the base-62 identifier at the end of a Spotify URL.";
            String content2 = " The URL can be obtained from Spotify's website in the search bar.";
            String contentSeperator = "\n------------------------------";
            String content3 = "\nAn example URL would look something like this:";
            String content4 = "\n\"https://open.spotify.com/artist/45eNHdiiabvmbp4erw26rg\"\n";
            String content5 = "\n\"45eNHdiiabvmbp4erw26rg\" would be the Spotify ID for ILLENIUM.";
            helpAlert.setContentText(
                content1 + content2 + contentSeperator + content3 + content4 + content5);
            helpAlert.showAndWait();
        }); // Platform.runLater
    } // helpAlert

    /**
     * Method handles all button events.
     */
    public void buttonEvents() {
        // helpButton
        helpButton.setOnAction(e -> {
            helpAlert();
        });

        // loadButton
        loadButton.setOnAction(e -> {
            load();
        });
    } // buttonEvents

    /**
     * Method that contains functions for loadButton.
     */
    public void load() {
        artistEvents = getArtistEvents();
        List<Event> events = artistEvents.getEvents();
        artistEvents.displayEvents();
        Platform.runLater(() -> {
            textFlow.getChildren().clear();
            Text nameText1 = new Text("\n     ̗̀ Spotify Artist");
            Text nameText2 = new Text(": " + artistName + "   ̖́");
            Text nameText3 = new Text("\n     ---------------");
            Text eventText = new Text("");
            if (events == null || events.isEmpty()) {
                Text noEventsText1 = new Text(
                    "\n     This Spotify artist has no events listed on SeatGeek at the moment.");
                Text noEventsText2 = new Text(
                    "\n     Please choose a different artist.");
                textFlow.getChildren().addAll(
                    nameText1, nameText2, nameText3, noEventsText1, noEventsText2);
            } else {
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    eventText.setText(eventText.getText() +
                        "\n      Event Title: " + event.getTitle() +
                        "\n      Date/Time: " + event.getDateTimeUTC() +
                        "\n      Venue: " + event.getVenue().getName()  +
                        "\n     ---------------");
                } // for
                textFlow.getChildren().addAll(nameText1, nameText2, nameText3, eventText);
            } // if-else
        }); // Platform.runLater
    } // load

    /**
     * Method loads API credentials from resource/config.properties.
     */
    public void apiCredentials() {
        try (FileInputStream configFileStream = new FileInputStream(CONFIG_PATH)) {
            Properties config = new Properties();
            config.load(configFileStream);
            spotifyClientID = config.getProperty("spotify.clientid");
            spotifyClientSecret = config.getProperty("spotify.clientsecret");
            seatgeekClientID = config.getProperty("seatgeek.clientid");
            seatgeekClientSecret = config.getProperty("seatgeek.clientsecret");
        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        } // try-with-resources
    } // apiCredentials

    /**
     * Method uses client credentials to request a
     * Spotify access token.
     *
     * @return returns Spotify access token, otherwise return {@code null} if an error occurs.
     */
    public String getSpotifyToken() {
        try {
            String spotifyClientCredentials = spotifyClientID + ":" + spotifyClientSecret;
            String spotifyEncodedCredentials = Base64.getEncoder().encodeToString(
                (spotifyClientCredentials).getBytes());
            String spotifyGrantType = "grant_type=client_credentials";
            HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_ENDPOINT))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + spotifyEncodedCredentials)
                .POST(HttpRequest.BodyPublishers.ofString(spotifyGrantType))
                .build(); // HttpRequest
            HttpResponse<String> tokenResponse = HTTP_CLIENT
                .send(tokenRequest, HttpResponse.BodyHandlers.ofString()); // HttpResponse
            if (tokenResponse.statusCode() != 200) {
                System.out.println("Error response: " + tokenResponse.statusCode());
                System.out.println(tokenResponse.body());
                return null;
            } // if
            String tokenResponseBody = tokenResponse.body();
            SpotifyTokenResponse spotifyTokenResponse = GSON
                .fromJson(tokenResponseBody, SpotifyTokenResponse.class); // GSON
            String accessToken = spotifyTokenResponse.getAccessToken();
            return accessToken;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            return null;
        } // try-catch
    } // getSpotifyToken

    /**
     * Method makes Spotify URI.
     */
    public void makeSpotifyURI() {
        try {
            spotifyID = URLEncoder.encode(idSearch.getText(), StandardCharsets.UTF_8);
            spotifyURI = ARTISTS_ENDPOINT + spotifyID;
        } catch (Exception e) {
            System.err.println("Error encoding Spotify ID: " + e.getMessage());
            e.printStackTrace();
        } // try-catch
    } // makeSpotifyURI

    /**
     * Method uses Spotify access token to retrieve information
     * about an artist using the 'Get Artist' endpoint.
     *
     * @return returns Spotify artist's information,
     * otherwise returns {@code null} if an error ocurs.
     */
    public String getArtistName() {
        try {
            System.out.println("Spotify Token: " + spotifyToken);
            System.out.println(spotifyURI);
            if (spotifyToken == null) {
                System.out.println("Failed to retrieve access token.");
                return null;
            } // if
            if (spotifyURI == null) {
                makeSpotifyURI();
            } // if
            HttpRequest artistRequest = HttpRequest.newBuilder()
                .uri(URI.create(spotifyURI))
                .header("Authorization", "Bearer " + spotifyToken)
                .GET()
                .build(); // HttpRequest
            HttpResponse<String> artistResponse = HTTP_CLIENT
                .send(artistRequest, HttpResponse.BodyHandlers.ofString()); // HttpResponse
            if (artistResponse.statusCode() != 200) {
                System.out.println("Error response: " + artistResponse.statusCode());
                System.out.println(artistResponse.body());
                return null;
            } // if
            String artistResponseBody = artistResponse.body();
            SpotifyArtistResponse spotifyArtistResponse = GSON
                .<SpotifyArtistResponse>fromJson(
                    artistResponseBody, SpotifyArtistResponse.class); // GSON
            String name = spotifyArtistResponse.getName();
            return name;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            return null;
        } // try-catch
    } // getArtistName

    /**
     * Method makes SeatGeek URI.
     */
    public void makeSeatGeekURI() {
        try {
            String performersSlug = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
            performersSlug = performersSlug.replace("+", "-");
            String query = String.format("?performers.slug=%s&client_id=%s&client_secret=%s",
                performersSlug, seatgeekClientID, seatgeekClientSecret);
            seatgeekURI = EVENTS_ENDPOINT + query;
            System.out.println(performersSlug);
            System.out.println(seatgeekURI);
        } catch (Exception e) {
            System.err.println("Error encoding SeatGeek URI: " + e.getMessage());
            e.printStackTrace();
        } // try-catch
    } // makeSeatGeekURI

    /**
     * Method uses SeatGeek API and the Spotify artist's
     * name to retrieve information about the artist's
     * events using the 'Events' endpoint.
     *
     * @return returns Spotify artist's events, otherwise
     * returns {@code null} if an error occurs.
     */
    public SeatGeekEventsResponse getArtistEvents() {
        try {
            if (seatgeekURI == null) {
                makeSeatGeekURI();
            } // if
            HttpRequest eventsRequest = HttpRequest.newBuilder()
                .uri(URI.create(seatgeekURI))
                .build(); // HttpRequest
            HttpResponse<String> eventsResponse = HTTP_CLIENT
                .send(eventsRequest, HttpResponse.BodyHandlers.ofString()); // HttpResponse
            if (eventsResponse.statusCode() != 200) {
                System.out.println("Error response: " + eventsResponse.statusCode());
                System.out.println(eventsResponse.body());
                return null;
            } // if
            String eventsResponseBody = eventsResponse.body();
            SeatGeekEventsResponse seatgeekEventsResponse = GSON
                .<SeatGeekEventsResponse>fromJson(
                    eventsResponseBody, SeatGeekEventsResponse.class); // GSON
            return seatgeekEventsResponse;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            return null;
        } // try-catch
    } // getArtistEvents

} // ApiApp
