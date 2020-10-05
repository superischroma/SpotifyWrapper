package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A Spotify "image"
 * All this object stores is the URL to the image and the width and height of the image.
 */
public class SpotifyImage
{
    @Getter
    private URL url;
    @Getter
    private long width;
    @Getter
    private long height;

    public SpotifyImage(JSONObject object)
    {
        try
        {
            if (object.get("url") != null) this.url = new URL((String) object.get("url"));
        }
        catch (MalformedURLException ex) {}
        if (object.get("width") != null) this.width = (long) object.get("width");
        if (object.get("height") != null) this.height = (long) object.get("height");
    }
}