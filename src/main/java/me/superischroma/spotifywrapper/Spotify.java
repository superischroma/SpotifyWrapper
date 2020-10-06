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

    private final String accessToken;

    public Spotify(String clientID, String clientSecret) throws Exception
    {
        this.accessToken = getAccessToken(clientID, clientSecret);
    }

    private JSONArray jsonSearch(String query, SpotifyType type, int limit, int resultOffset) throws Exception
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

    private List<SpotifyObject> search(String query, SpotifyType type, int limit, int resultOffset) throws Exception
    {
        JSONArray items = jsonSearch(query, type, limit, resultOffset);
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
                case SHOW: objects.add(SpotifyShow.getShow(jo)); break;
                case EPISODE: objects.add(SpotifyEpisode.getEpisode(jo)); break;
            }
        }
        return objects;
    }

    /**
     * Search for tracks on Spotify.
     * @param query Used to find something to search.
     * @param limit Number of tracks given back.
     * @param resultOffset Where the tracks should start to be searched from.
     * @return A List with the tracks.
     */
    public List<SpotifyTrack> searchByTrack(String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(query, SpotifyType.TRACK, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyTrack> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyTrack) object);
        return l;
    }

    /**
     * Search for albums on Spotify.
     * @param query Used to find something to search.
     * @param limit Number of albums given back.
     * @param resultOffset Where the albums should start to be searched from.
     * @return A List with the albums.
     */
    public List<SpotifyAlbum> searchByAlbum(String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(query, SpotifyType.ALBUM, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyAlbum> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyAlbum) object);
        return l;
    }

    /**
     * Search for artists on Spotify.
     * @param query Used to find something to search.
     * @param limit Number of artists given back.
     * @param resultOffset Where the artists should start to be searched from.
     * @return A List with the artists.
     */
    public List<SpotifyArtist> searchByArtist(String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(query, SpotifyType.ARTIST, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyArtist> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyArtist) object);
        return l;
    }

    /**
     * Search for playlists on Spotify.
     * @param query Used to find something to search.
     * @param limit Number of playlists given back.
     * @param resultOffset Where the playlists should start to be searched from.
     * @return A List with the playlists.
     */
    public List<SpotifyPlaylist> searchByPlaylist(String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(query, SpotifyType.PLAYLIST, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyPlaylist> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyPlaylist) object);
        return l;
    }

    /**
     * Search for shows on Spotify.
     * @param query Used to find something to search.
     * @param limit Number of shows given back.
     * @param resultOffset Where the shows should start to be searched from.
     * @return A List with the shows.
     */
    public List<SpotifyShow> searchByShow(String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(query, SpotifyType.SHOW, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyShow> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyShow) object);
        return l;
    }

    /**
     * Search for episodes on Spotify.
     * @param query Used to find something to search.
     * @param limit Number of episodes given back.
     * @param resultOffset Where the episodes should start to be searched from.
     * @return A List with the episodes.
     */
    public List<SpotifyEpisode> searchByEpisode(String query, int limit, int resultOffset)
            throws Exception
    {
        List<SpotifyObject> objects = search(query, SpotifyType.EPISODE, limit, resultOffset);
        if (objects == null) return null;
        List<SpotifyEpisode> l = new ArrayList<>();
        for (SpotifyObject object : objects)
            l.add((SpotifyEpisode) object);
        return l;
    }

    private SpotifyObject getObject(String id, SpotifyType type) throws Exception
    {
        URL url = new URL("https://api.spotify.com/v1/" + type.getValue() + "s/" + id);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        if (connection.getResponseCode() != 200)
        {
            System.out.println("Error getting " + type.getValue() + ": " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            content.append(line).append("\n");
        JSONObject jo = (JSONObject) PARSER.parse(content.toString());
        switch (type)
        {
            case TRACK: return SpotifyTrack.getTrack(jo);
            case ALBUM: return SpotifyAlbum.getAlbum(jo);
            case ARTIST: return new SpotifyArtist(jo);
            case PLAYLIST: return new SpotifyPlaylist(jo);
            case USER: return new SpotifyUser(jo);
        }
        return null;
    }

    /**
     * Retrieve the captured state of a track from the Spotify API.
     * @param id The track's ID.
     * @return A SpotifyTrack object containing all of the track's information.
     */
    public SpotifyTrack getTrack(String id) throws Exception
    {
        return (SpotifyTrack) getObject(id, SpotifyType.TRACK);
    }

    /**
     * Retrieve the captured state of an artist from the Spotify API.
     * @param id The artist's ID.
     * @return A SpotifyArtist object containing all of the artist's information.
     */
    public SpotifyArtist getArtist(String id) throws Exception
    {
        return (SpotifyArtist) getObject(id, SpotifyType.ARTIST);
    }

    /**
     * Retrieve the captured state of an album from the Spotify API.
     * @param id The album's ID.
     * @return A SpotifyAlbum object containing all of the album's information.
     */
    public SpotifyAlbum getAlbum(String id) throws Exception
    {
        return (SpotifyAlbum) getObject(id, SpotifyType.ALBUM);
    }

    /**
     * Retrieve the captured state of a playlist from the Spotify API.
     * @param id The album's ID.
     * @return A SpotifyPlaylist object containing all of the playlist's information.
     */
    public SpotifyPlaylist getPlaylist(String id) throws Exception
    {
        return (SpotifyPlaylist) getObject(id, SpotifyType.PLAYLIST);
    }

    /**
     * Retrieve the captured state of a user from the Spotify API.
     * @param id The user's ID.
     * @return A SpotifyUser object containing all of the user's information.
     */
    public SpotifyUser getUser(String id) throws Exception
    {
        return (SpotifyUser) getObject(id, SpotifyType.USER);
    }

    /**
     * Retrieve the captured state of a show from the Spotify API.
     * @param id The show's ID.
     * @return A SpotifyShow object containing all of the show's information.
     */
    public SpotifyShow getShow(String id) throws Exception
    {
        return (SpotifyShow) getObject(id, SpotifyType.SHOW);
    }

    /**
     * Retrieve the captured state of an episode from the Spotify API.
     * @param id The episode's ID.
     * @return A SpotifyEpisode object containing all of the episode's information.
     */
    public SpotifyEpisode getEpisode(String id) throws Exception
    {
        return (SpotifyEpisode) getObject(id, SpotifyType.EPISODE);
    }

    /**
     * Attempts to find the first artist with the specified name.
     * @param name Name of the artist.
     * @return A SpotifyArtist that meets the name requirement.
     */
    public SpotifyArtist findArtist(String name) throws Exception
    {
        List<SpotifyArtist> artists = searchByArtist(name, 1, 0);
        if (artists == null) return null;
        if (artists.size() == 0) return null;
        return artists.get(0);
    }

    /**
     * Attempts to find the first track with the specified name.
     * @param name Name of the track.
     * @return A SpotifyTrack that meets the name requirement.
     */
    public SpotifyTrack findTrack(String name) throws Exception
    {
        List<SpotifyTrack> tracks = searchByTrack(name, 1, 0);
        if (tracks == null) return null;
        if (tracks.size() == 0) return null;
        return tracks.get(0);
    }

    /**
     * Attempts to find the first playlist with the specified name.
     * @param name Name of the playlist.
     * @return A SpotifyPlaylist that meets the name requirement.
     */
    public SpotifyPlaylist findPlaylist(String name) throws Exception
    {
        List<SpotifyPlaylist> playlists = searchByPlaylist(name, 1, 0);
        if (playlists == null) return null;
        if (playlists.size() == 0) return null;
        return playlists.get(0);
    }

    /**
     * Attempts to find the first album with the specified name.
     * @param name Name of the album.
     * @return A SpotifyAlbum that meets the name requirement.
     */
    public SpotifyAlbum findAlbum(String name) throws Exception
    {
        List<SpotifyAlbum> albums = searchByAlbum(name, 1, 0);
        if (albums == null) return null;
        if (albums.size() == 0) return null;
        return albums.get(0);
    }

    /**
     * Attempts to find the first show with the specified name.
     * @param name Name of the show.
     * @return A SpotifyShow that meets the name requirement.
     */
    public SpotifyShow findShow(String name) throws Exception
    {
        List<SpotifyShow> shows = searchByShow(name, 1, 0);
        if (shows == null) return null;
        if (shows.size() == 0) return null;
        return shows.get(0);
    }

    /**
     * Attempts to find the first episode with the specified name.
     * @param name Name of the episode.
     * @return A SpotifyEpisode that meets the name requirement.
     */
    public SpotifyEpisode findEpisode(String name) throws Exception
    {
        List<SpotifyEpisode> episodes = searchByEpisode(name, 1, 0);
        if (episodes == null) return null;
        if (episodes.size() == 0) return null;
        return episodes.get(0);
    }

    private static JSONArray getItems(JSONObject paging)
    {
        return (JSONArray) paging.get("items");
    }

    private static String split(Collection<SpotifyType> collection, String d)
    {
        StringBuilder r = new StringBuilder();
        boolean b = false;
        for (SpotifyType type : collection)
        {
            if (b)
                r.append(d);
            else
                b = true;
            r.append(type.getValue());
        }
        return r.toString();
    }

    /**
     * Retrieves a Spotify access token using the specified client ID and client secret.
     * @param clientID The client ID of your application.
     * @param clientSecret The client secret of your application.
     * @return An access token that can be used to access the rest of the Spotify API.
     */
    private static String getAccessToken(String clientID, String clientSecret) throws Exception
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
}