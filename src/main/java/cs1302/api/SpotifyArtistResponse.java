package cs1302.api;

/**
 * Represents a response from the Spotify API when requesting a
 * Spotify artist's name. This is used by Gson to create an
 * object from the Json response body.
 */
public class SpotifyArtistResponse {

    private String name;

    /**
     * Method retrieves Spotify artist's name from JSON response.
     *
     * @return returns Spotify artist name.
     */
    public String getName() {
        return name;
    } // getName

} // SpotifyArtistResponse
