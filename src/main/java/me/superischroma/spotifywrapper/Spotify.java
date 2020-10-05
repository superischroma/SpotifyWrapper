package me.superischroma.spotifywrapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * The interface for the wrapper.
 * Contains methods which return Spotify objects and other important information.
 */
public class Spotify
{
    public static final JSONParser PARSER = new JSONParser();

    /**
     * Retrieves a Spotify access token using the specified client ID and client secret.
     * @param clientID The client ID of your application.
     * @param clientSecret The client secret of your application.
     * @return An access token that can be used to access the rest of the Spotify API.
     */
    public static String getAccessToken(String clientID, String clientSecret) throws Exception
    {
        URL url = new URL("https://accounts.spotify.com/api/token");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((clientID + ":" + clientSecret).getBytes()));
        ParameterManager manager = new ParameterManager();
        manager.add("grant_type", "client_credentials");
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        out.write(manager.getParameters());
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line);
        JSONObject object = (JSONObject) PARSER.parse(content.toString());
        return (String) object.get("access_token");
    }

    /**
     * Advanced way of searching for items on Spotify.
     * @param accessToken Used to access the Web API.
     * @param query Used to find something to search.
     * @param type Used to find what to search for.
     * @param limit Number of items given back.
     * @param resultOffset Where the items should start to be searched from.
     * @return A JSONArray with the items.
     */
    public static JSONArray search(String accessToken, String query, SpotifySearchType type, int limit, int resultOffset) throws Exception
    {
        ParameterManager manager = new ParameterManager();
        manager.add("q", query);
        manager.add("type", type.getValue());
        manager.add("limit", String.valueOf(limit));
        manager.add("offset", String.valueOf(resultOffset));
        URL url = new URL("https://api.spotify.com/v1/search?" + manager.getParameters());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        return getItems((JSONObject) ((JSONObject) PARSER.parse(content.toString())).get(type.getValue() + "s"));
    }

    /**
     * Search for tracks on Spotify.
     * @param accessToken Used to access the Web API.
     * @param query Used to find something to search.
     * @param limit Number of tracks given back.
     * @param resultOffset Where the tracks should start to be searched from.
     * @return A List with the tracks.
     */
    public static List<SpotifyTrack> searchByTrack(String accessToken, String query, int limit, int resultOffset)
            throws Exception
    {
        JSONArray items = search(accessToken, query, SpotifySearchType.TRACK, limit, resultOffset);
        List<SpotifyTrack> tracks = new ArrayList<>();
        for (Object o : items)
            tracks.add(SpotifyTrack.getTrack((JSONObject) o));
        return tracks;
    }

    /**
     * Search for tracks on Spotify.
     * @param accessToken Used to access the Web API.
     * @param query Used to find something to search.
     * @param limit Number of tracks given back.
     * @param resultOffset Where the tracks should start to be searched from.
     * @return A List with the tracks.
     */
    public static List<SpotifyAlbum> searchByAlbum(String accessToken, String query, int limit, int resultOffset)
            throws Exception
    {
        JSONArray items = search(accessToken, query, SpotifySearchType.ALBUM, limit, resultOffset);
        List<SpotifyAlbum> albums = new ArrayList<>();
        for (Object o : items)
            albums.add(SpotifyAlbum.getAlbum((JSONObject) o));
        return albums;
    }

    /**
     * Search for artists on Spotify.
     * @param accessToken Used to access the Web API.
     * @param query Used to find something to search.
     * @param limit Number of artists given back.
     * @param resultOffset Where the artists should start to be searched from.
     * @return A List with the artists.
     */
    public static List<SpotifyArtist> searchByArtist(String accessToken, String query, int limit, int resultOffset)
            throws Exception
    {
        JSONArray items = search(accessToken, query, SpotifySearchType.ARTIST, limit, resultOffset);
        List<SpotifyArtist> artists = new ArrayList<>();
        for (Object o : items)
            artists.add(new SpotifyArtist((JSONObject) o));
        return artists;
    }

    /**
     * Search for playlists on Spotify.
     * @param accessToken Used to access the Web API.
     * @param query Used to find something to search.
     * @param limit Number of playlists given back.
     * @param resultOffset Where the playlists should start to be searched from.
     * @return A List with the playlists.
     */
    public static List<SpotifyPlaylist> searchByPlaylist(String accessToken, String query, int limit, int resultOffset)
            throws Exception
    {
        JSONArray items = search(accessToken, query, SpotifySearchType.PLAYLIST, limit, resultOffset);
        List<SpotifyPlaylist> playlists = new ArrayList<>();
        for (Object o : items)
            playlists.add(new SpotifyPlaylist((JSONObject) o));
        return playlists;
    }

    /**
     * Retrieve the captured state of a track from the Spotify API.
     * @param accessToken Used to access the Web API.
     * @param id The track's ID.
     * @return A SpotifyTrack object containing all of the track's information.
     */
    public static SpotifyTrack getTrack(String accessToken, String id) throws Exception
    {
        URL url = new URL("https://api.spotify.com/v1/tracks/" + id);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        if (connection.getResponseCode() != 200)
        {
            System.out.println("Error getting track: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        return SpotifyTrack.getTrack((JSONObject) PARSER.parse(content.toString()));
    }

    /**
     * Retrieve the captured state of an artist from the Spotify API.
     * @param accessToken Used to access the Web API.
     * @param id The artist's ID.
     * @return A SpotifyArtist object containing all of the artist's information.
     */
    public static SpotifyArtist getArtist(String accessToken, String id) throws Exception
    {
        URL url = new URL("https://api.spotify.com/v1/artists/" + id);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        if (connection.getResponseCode() != 200)
        {
            System.out.println("Error getting artist: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        return new SpotifyArtist((JSONObject) PARSER.parse(content.toString()));
    }

    /*
    private static String getUserAccessToken(String clientID) throws Exception
    {
        ParameterManager manager = new ParameterManager();
        manager.add("client_id", clientID);
        manager.add("response_type", "code");
        URL url = new URL("https://accounts.spotify.com/authorize?" + manager.getParameters());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        System.out.println(content.toString());
        return null;
    }
     */

    /**
     * Gets the array of objects from a paging object.
     * @param paging The paging object.
     * @return A JSONArray containing all of the objects stored in the paging object.
     */
    private static JSONArray getItems(JSONObject paging)
    {
        return (JSONArray) paging.get("items");
    }
}