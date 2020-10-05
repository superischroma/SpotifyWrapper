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
 * The captured state of a Spotify user.
 */
public class SpotifyUser extends SpotifyObject
{
    @Getter
    private String displayName;
    @Getter
    private long followers;
    @Getter
    private List<SpotifyImage> images;

    public SpotifyUser(JSONObject object)
    {
        super(object);
        if (object.get("display_name") != null) this.displayName = (String) object.get("display_name");
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
    }
}