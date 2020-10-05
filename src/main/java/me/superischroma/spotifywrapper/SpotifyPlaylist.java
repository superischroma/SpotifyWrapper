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
 * The captured state of a Spotify playlist.
 */
public class SpotifyPlaylist extends SpotifyObject
{
    @Getter
    private boolean collaborative;
    @Getter
    private String description;
    @Getter
    private long followers;
    @Getter
    private List<SpotifyImage> images;
    @Getter
    private String name;
    @Getter
    private SpotifyUser owner;
    @Getter
    private boolean pub;
    @Getter
    private String snapshotID;
    @Getter
    private List<SpotifyPlaylistTrack> tracks;

    public SpotifyPlaylist(JSONObject object)
    {
        super(object);
        if (object.get("collaborative") != null) this.collaborative = (boolean) object.get("collaborative");
        if (object.get("description") != null) this.description = (String) object.get("description");
        if (object.get("followers") != null)
        {
            JSONObject o = (JSONObject) object.get("followers");
            if (o.get("total") != null) this.followers = (long) o.get("total");
        }
        if (object.get("images") != null)
        {
            this.images = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("images");
            for (Object o : a)
                this.images.add(new SpotifyImage((JSONObject) o));
        }
        if (object.get("name") != null) this.name = (String) object.get("name");
        if (object.get("owner") != null) this.owner = new SpotifyUser((JSONObject) object.get("owner"));
        if (object.get("public") != null) this.pub = (boolean) object.get("public");
        if (object.get("snapshot_id") != null) this.snapshotID = (String) object.get("snapshot_id");
        if (object.get("tracks") != null)
        {
            this.tracks = new ArrayList<>();
            JSONObject obj = (JSONObject) object.get("tracks");
            if (obj.get("items") != null)
            {
                JSONArray a = (JSONArray) obj.get("items");
                for (Object o : a)
                    this.tracks.add(new SpotifyPlaylistTrack((JSONObject) o));
            }
        }
    }
}