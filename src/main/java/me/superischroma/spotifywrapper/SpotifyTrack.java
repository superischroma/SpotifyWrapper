package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The captured state of a Spotify track.
 * Some values may be null if track is created from a album or playlist. (this is called a partial track object)
 * This object will provide an incomplete album object.
 */
public class SpotifyTrack extends SpotifyObject
{
    private static List<SpotifyTrack> CREATED_TRACKS = new ArrayList<>();

    @Getter
    private SpotifyAlbum album;
    @Getter
    private List<SpotifyArtist> artists;
    @Getter
    private List<String> markets;
    @Getter
    private long discCount;
    @Getter
    private long duration;
    @Getter
    private boolean explicit;
    @Getter
    private Map<String, String> externalIDs;
    @Getter
    private boolean playable;
    @Getter
    private SpotifyTrackLink linkedFrom;
    @Getter
    private long popularity;
    @Getter
    private URL previewURL;
    @Getter
    private long trackNumber;
    @Getter
    private boolean local;

    private SpotifyTrack(JSONObject object)
    {
        super(object);
        if (object.get("album") != null) this.album = SpotifyAlbum.getAlbum((JSONObject) object.get("album"));
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
        if (object.get("disc_number") != null) this.discCount = (long) object.get("disc_number");
        if (object.get("duration_ms") != null) this.duration = (long) object.get("duration_ms");
        if (object.get("explicit") != null) this.explicit = (boolean) object.get("explicit");
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
        if (object.get("popularity") != null) this.popularity = (long) object.get("popularity");
        if (object.get("track_number") != null) this.trackNumber = (long) object.get("track_number");
        if (object.get("is_local") != null) this.local = (boolean) object.get("is_local");
        try
        {
            if (object.get("preview_url") != null) this.previewURL = new URL((String) object.get("preview_url"));
        }
        catch (MalformedURLException ex) {} // it should not be throwing this
        CREATED_TRACKS.add(this);
    }

    public static SpotifyTrack getTrack(JSONObject object)
    {
        for (SpotifyTrack t : CREATED_TRACKS)
        {
            if (t.getObject().toString().equals(object.toString()))
            {
                return t;
            }
        }
        return new SpotifyTrack(object);
    }
}
