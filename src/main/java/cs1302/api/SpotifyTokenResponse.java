package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the Spotify API when requesting a
 * Spotify access token. This is used by Gson to create an object
 * from the Json response body.
 */
public class SpotifyTokenResponse {

    @SerializedName("access_token")
    private String accessToken;

    /**
     * Method retrieves Spotify access token from JSON response.
     *
     * @return returns Spotify access token.
     */
    public String getAccessToken() {
        return accessToken;
    } // getAccessToken

} // SpotifyTokenResponse
