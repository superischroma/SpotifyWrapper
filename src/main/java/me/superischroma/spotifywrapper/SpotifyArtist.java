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
 * The captured state of a Spotify artist.
 * Some values may be null if artist is created from an album, track, or playlist. (this is called a partial artist object)
 */
public class SpotifyArtist extends SpotifyObject
{
    @Getter
    private long followers;
    @Getter
    private List<String> genres;
    @Getter
    private List<SpotifyImage> images;
    @Getter
    private String name;
    @Getter
    private long popularity;

    public SpotifyArtist(JSONObject object)
    {
        super(object);
        if (object.get("followers") != null)
        {
            JSONObject o = (JSONObject) object.get("followers");
            if (o.get("total") != null) this.followers = (long) o.get("total");
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
        if (object.get("popularity") != null) this.popularity = (long) object.get("popularity");
    }
}