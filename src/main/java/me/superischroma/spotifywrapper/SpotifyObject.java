package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SpotifyObject
{
    @Getter
    protected Map<String, String> externalURLs;
    @Getter
    protected String href;
    @Getter
    protected String id;
    @Getter
    protected URI uri;
    @Getter
    protected String name;
    @Getter
    protected JSONObject object;

    public SpotifyObject(JSONObject object)
    {
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
        if (object.get("href") != null) this.href = (String) object.get("href");
        if (object.get("id") != null) this.id = (String) object.get("id");
        try
        {
            if (object.get("uri") != null) this.uri = new URI((String) object.get("uri"));
        }
        catch (URISyntaxException ex) {} // it should not throw this either
        if (object.get("name") != null) this.name = (String) object.get("name");
        this.object = object;
    }
}