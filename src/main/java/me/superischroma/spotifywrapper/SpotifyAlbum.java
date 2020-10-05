package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The captured state of a Spotify album.
 * Some values may be null if album is created from a track or playlist. (this is called a partial album object)
 * This object may provide incomplete versions of tracks. (these are partial track objects)
 */
public class SpotifyAlbum extends SpotifyObject
{
    private static List<SpotifyAlbum> CREATED_ALBUMS = new ArrayList<>();

    @Getter
    private SpotifyAlbumType type;
    @Getter
    private List<SpotifyArtist> artists;
    @Getter
    private List<String> markets;
    @Getter
    private List<SpotifyCopyright> copyrights;
    @Getter
    private Map<String, String> externalIDs;
    @Getter
    private List<String> genres;
    @Getter
    private List<SpotifyImage> images;
    @Getter
    private String name;
    @Getter
    private String label;
    @Getter
    private long popularity;
    @Getter
    private String releaseDate;
    @Getter
    private SpotifyDatePrecisionType releaseDatePrecision;
    @Getter
    private List<SpotifyTrack> tracks;

    private SpotifyAlbum(JSONObject object)
    {
        super(object);
        if (object.get("album_type") != null) this.type = SpotifyAlbumType.valueOf(String.valueOf(object.get("album_type")).toUpperCase());
        if (object.get("artists") != null)
        {
            this.artists = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("artists");
            for (Object o : a)
                this.artists.add(new SpotifyArtist((JSONObject) o));
        }
        if (object.get("available_markets") != null)
        {
            this.markets = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("available_markets");
            for (Object o : a)
                this.markets.add((String) o);
        }
        if (object.get("copyrights") != null)
        {
            this.copyrights = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("copyrights");
            for (Object o : a)
                this.copyrights.add(new SpotifyCopyright((JSONObject) o));
        }
        if (object.get("external_ids") != null)
        {
            this.externalIDs = new HashMap<>();
            JSONObject o = (JSONObject) object.get("external_ids");
            for (Object e : o.entrySet())
            {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) e;
                this.externalIDs.put(entry.getKey(), entry.getValue());
            }
        }
        if (object.get("genres") != null)
        {
            this.genres = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("genres");
            for (Object o : a)
                this.genres.add((String) o);
        }
        if (object.get("images") != null)
        {
            this.images = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("images");
            for (Object o : a)
                this.images.add(new SpotifyImage((JSONObject) o));
        }
        if (object.get("name") != null) this.name = (String) object.get("name");
        if (object.get("label") != null) this.label = (String) object.get("label");
        if (object.get("popularity") != null) this.popularity = (long) object.get("popularity");
        if (object.get("release_date") != null) this.releaseDate = (String) object.get("release_date");
        if (object.get("release_date_precision") != null) this.releaseDatePrecision = SpotifyDatePrecisionType.valueOf(String.valueOf(object.get("release_date_precision")).toUpperCase());
        if (object.get("tracks") != null)
        {
            this.tracks = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("tracks");
            for (Object o : a)
                this.tracks.add(SpotifyTrack.getTrack((JSONObject) o));
        }
        CREATED_ALBUMS.add(this);
    }

    public static SpotifyAlbum getAlbum(JSONObject object)
    {
        for (SpotifyAlbum a : CREATED_ALBUMS)
        {
            if (a.getObject().toString().equals(object.toString()))
            {
                return a;
            }
        }
        return new SpotifyAlbum(object);
    }
}