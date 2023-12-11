package cs1302.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a response from the SeatGeek API when requesting a
 * Spotify artist's events. This is used by Gson to create an
 * object from the Json response body.
 */
public class SeatGeekEventsResponse {

    private List<Event> events;

    /**
     * Method retrieves Spotify artist's events from Json response.
     *
     * @return returns Spotify artist's events.
     */
    public List<Event> getEvents() {
        return events;
    } // getEvents

    /**
     * Retrieves event specific details.
     */
    public static class Event {

        private String title;
        @SerializedName("datetime_utc")
        private String dateTimeUTC;
        private Venue venue;

        /**
         * Method retrieves event title from Json response.
         *
         * @return returns event title.
         */
        public String getTitle() {
            return title;
        } // getTitle

        /**
         * Method retrieves date and time (UTC) of event
         * from Json response.
         *
         * @return returns date and time (UTC) of event.
         */
        public String getDateTimeUTC() {
            return dateTimeUTC;
        } // getDateTimeUTC

        /**
         * Method retrieves event venues from Json response.
         *
         * @return returns event venues.
         */
        public Venue getVenue() {
            return venue;
        } // getVenue

    } // Events

    /**
     * Retrieves venue specific details.
     */
    public static class Venue {

        private String city;
        private String state;
        private String name;

        /**
         * Method retrieves the city the venue
         * is located in from Json response.
         *
         * @return returns venue city.
         */
        public String getCity() {
            return city;
        } // getCity

        /**
         * Method retrieves the state the venue
         * is located in from Json response.
         *
         * @return returns venue state.
         */
        public String getState() {
            return state;
        } // getState

        /**
         * Method retrieves the name of the venue
         * from Json response.
         *
         * @return returns venue name.
         */
        public String getName() {
            return name;
        } // getName

    } // Venue

    /**
     * Method displays event details for each detail
     * from Json response.
     */
    public void displayEvents() {

        if (events == null || events.isEmpty()) {
            System.out.println("The artist has no events listed at the moment.");
        } else {
            int eventsToDisplay = Math.min(5, events.size());
            for (int i = 0; i < eventsToDisplay; i++) {
                Event event = events.get(i);
                System.out.println("Event Title: " + event.getTitle());
                System.out.println("Date/Time: " + event.getDateTimeUTC());
                System.out.println("Venue: " + event.getVenue().getName());
                System.out.println("---------------------");
            } // for
        } // if-else

    } // displayEvents

} // SeatGeekEventsResponse
