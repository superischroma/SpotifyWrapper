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
public class SpotifyUser
{
    @Getter
    private String displayName;
    @Getter
    private Map<String, String> externalURLs;
    @Getter
    private long followers;
    @Getter
    private String href;
    @Getter
    private String id;
    @Getter
    private List<SpotifyImage> images;
    @Getter
    private URI uri;

    public SpotifyUser(JSONObject object)
    {
        if (object.get("display_name") != null) this.displayName = (String) object.get("display_name");
        if (object.get("external_urls") != null)
        {
            this.externalURLs = new HashMap<>();
            JSONObject o = (JSONObject) object.get("external_urls");
            for (Object e : o.entrySet())
            {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) e;
                this.externalURLs.put(entry.getKey(), entry.getValue());
            }
        }
        if (object.get("followers") != null)
        {
            JSONObject o = (JSONObject) object.get("followers");
            if (o.get("total") != null) this.followers = (long) o.get("total");
        }
        if (object.get("href") != null) this.href = (String) object.get("href");
        if (object.get("id") != null) this.id = (String) object.get("id");
        if (object.get("images") != null)
        {
            this.images = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("images");
            for (Object o : a)
                this.images.add(new SpotifyImage((JSONObject) o));
        }
        try
        {
            if (object.get("uri") != null) this.uri = new URI((String) object.get("uri"));
        }
        catch (URISyntaxException ex) {} // it should not throw this either
    }
}