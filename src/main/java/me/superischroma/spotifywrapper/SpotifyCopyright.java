package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONObject;

/**
 * A Spotify copyright object.
 */
public class SpotifyCopyright
{
    @Getter
    private String text;
    @Getter
    private String type;

    public SpotifyCopyright(JSONObject object)
    {
        if (object.get("text") != null) this.text = (String) object.get("text");
        if (object.get("type") != null) this.type = (String) object.get("type");
    }
}