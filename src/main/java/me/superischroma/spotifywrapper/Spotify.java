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
import java.util.*;

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

    private static JSONArray jsonSearch(String accessToken, String query, SpotifySearchType type, int limit, int resultOffset) throws Exception
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
        if (connection.getResponseCode() != 200)
        {
            System.out.println("An error occurred while searching: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        return getItems((JSONObject) ((JSONObject) PARSER.parse(content.toString())).get(type.getValue() + "s"));
    }

    private static List<SpotifyObject> search(String accessToken, String query, SpotifySearchType type, int limit, int resultOffset) throws Exception
    {
        JSONArray items = jsonSearch(accessToken, query, type, limit, resultOffset);
        if (items == null) return null;
        List<SpotifyObject> objects = new ArrayList<>();
        for (Object o : items)
        {
            JSONObject jo = (JSONObject) o;
            switch (type)
            {
                case TRACK: objects.add(SpotifyTrack.getTrack(jo)); break;
                case ALBUM: objects.add(SpotifyAlbum.getAlbum(jo)); break;
                case ARTIST: objects.add(new SpotifyArtist(jo)); break;
                case PLAYLIST: objects.add(new SpotifyPlaylist(jo)); break;
            }
        }
        return objects;
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
        List<SpotifyObject> objects = search(accessToken, query, SpotifySearchType.TRACK, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyTrack> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyTrack) object);
        return l;
    }

    /**
     * Search for albums on Spotify.
     * @param accessToken Used to access the Web API.
     * @param query Used to find something to search.
     * @param limit Number of albums given back.
     * @param resultOffset Where the albums should start to be searched from.
     * @return A List with the albums.
     */
    public static List<SpotifyAlbum> searchByAlbum(String accessToken, String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(accessToken, query, SpotifySearchType.ALBUM, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyAlbum> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyAlbum) object);
        return l;
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
        List<SpotifyObject> objects = search(accessToken, query, SpotifySearchType.ARTIST, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyArtist> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyArtist) object);
        return l;
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
        List<SpotifyObject> objects = search(accessToken, query, SpotifySearchType.PLAYLIST, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyPlaylist> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyPlaylist) object);
        return l;
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

    /**
     * Retrieve the captured state of an album from the Spotify API.
     * @param accessToken Used to access the Web API.
     * @param id The album's ID.
     * @return A SpotifyAlbum object containing all of the album's information.
     */
    public static SpotifyAlbum getAlbum(String accessToken, String id) throws Exception
    {
        URL url = new URL("https://api.spotify.com/v1/albums/" + id);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        if (connection.getResponseCode() != 200)
        {
            System.out.println("Error getting album: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        return SpotifyAlbum.getAlbum((JSONObject) PARSER.parse(content.toString()));
    }

    /**
     * Retrieve the captured state of a playlist from the Spotify API.
     * @param accessToken Used to access the Web API.
     * @param id The album's ID.
     * @return A SpotifyPlaylist object containing all of the playlist's information.
     */
    public static SpotifyPlaylist getPlaylist(String accessToken, String id) throws Exception
    {
        URL url = new URL("https://api.spotify.com/v1/playlists/" + id);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        if (connection.getResponseCode() != 200)
        {
            System.out.println("Error getting playlist: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        return new SpotifyPlaylist((JSONObject) PARSER.parse(content.toString()));
    }

    /**
     * Attempts to find the first artist with the specified name.
     * @param accessToken Used to access the Web API.
     * @param name Name of the artist.
     * @return A SpotifyArtist that meets the name requirement.
     */
    public static SpotifyArtist findArtist(String accessToken, String name) throws Exception
    {
        List<SpotifyArtist> artists = searchByArtist(accessToken, name, 1, 0);
        if (artists == null) return null;
        if (artists.size() == 0) return null;
        return artists.get(0);
    }

    /**
     * Attempts to find the first track with the specified name.
     * @param accessToken Used to access the Web API.
     * @param name Name of the track.
     * @return A SpotifyTrack that meets the name requirement.
     */
    public static SpotifyTrack findTrack(String accessToken, String name) throws Exception
    {
        List<SpotifyTrack> tracks = searchByTrack(accessToken, name, 1, 0);
        if (tracks == null) return null;
        if (tracks.size() == 0) return null;
        return tracks.get(0);
    }

    /**
     * Attempts to find the first playlist with the specified name.
     * @param accessToken Used to access the Web API.
     * @param name Name of the playlist.
     * @return A SpotifyPlaylist that meets the name requirement.
     */
    public static SpotifyPlaylist findPlaylist(String accessToken, String name) throws Exception
    {
        List<SpotifyPlaylist> playlists = searchByPlaylist(accessToken, name, 1, 0);
        if (playlists == null) return null;
        if (playlists.size() == 0) return null;
        return playlists.get(0);
    }

    /**
     * Attempts to find the first album with the specified name.
     * @param accessToken Used to access the Web API.
     * @param name Name of the album.
     * @return A SpotifyAlbum that meets the name requirement.
     */
    public static SpotifyAlbum findAlbum(String accessToken, String name) throws Exception
    {
        List<SpotifyAlbum> albums = searchByAlbum(accessToken, name, 1, 0);
        if (albums == null) return null;
        if (albums.size() == 0) return null;
        return albums.get(0);
    }

    private static JSONArray getItems(JSONObject paging)
    {
        return (JSONArray) paging.get("items");
    }

    private static String split(Collection<SpotifySearchType> collection, String d)
    {
        StringBuilder r = new StringBuilder();
        boolean b = false;
        for (SpotifySearchType type : collection)
        {
            if (b)
                r.append(d);
            else
                b = true;
            r.append(type.getValue());
        }
        return r.toString();
    }
}