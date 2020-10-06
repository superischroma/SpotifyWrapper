package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpotifyEpisode extends SpotifyObject
{
    private static List<SpotifyEpisode> CREATED_EPISODES = new ArrayList<>();

    @Getter
    private URL audioPreviewURL;
    @Getter
    private String description;
    @Getter
    private long duration;
    @Getter
    private boolean explicit;
    @Getter
    private List<SpotifyImage> images;
    @Getter
    private boolean externallyHosted;
    @Getter
    private boolean playable;
    @Getter
    private List<String> languages;
    @Getter
    private String name;
    @Getter
    private String releaseDate;
    @Getter
    private SpotifyDatePrecisionType releaseDatePrecision;
    @Getter
    private SpotifyShow show;

    private SpotifyEpisode(JSONObject object)
    {
        super(object);
        try
        {
            if (object.get("audio_preview_url") != null) this.audioPreviewURL = new URL((String) object.get("audio_preview_url"));
        }
        catch (MalformedURLException ex) {} // it should not be throwing this
        if (object.get("description") != null) this.description = (String) object.get("description");
        if (object.get("duration_ms") != null) this.duration = (long) object.get("duration_ms");
        if (object.get("explicit") != null) this.explicit = (boolean) object.get("explicit");
        if (object.get("images") != null)
        {
            this.images = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("images");
            for (Object o : a)
                this.images.add(new SpotifyImage((JSONObject) o));
        }
        if (object.get("is_externally_hosted") != null) this.externallyHosted = (boolean) object.get("is_externally_hosted");
        if (object.get("is_playable") != null) this.playable = (boolean) object.get("is_playable");
        if (object.get("languages") != null)
        {
            this.languages = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("languages");
            for (Object o : a)
                this.languages.add((String) o);
        }
        if (object.get("name") != null) this.name = (String) object.get("name");
        if (object.get("release_date") != null) this.releaseDate = (String) object.get("release_date");
        if (object.get("release_date_precision") != null) this.releaseDatePrecision = SpotifyDatePrecisionType.valueOf(String.valueOf(object.get("release_date_precision")).toUpperCase());
        if (object.get("show") != null) this.show = SpotifyShow.getShow((JSONObject) object.get("show"));
        CREATED_EPISODES.add(this);
    }

    public static SpotifyEpisode getEpisode(JSONObject object)
    {
        for (SpotifyEpisode e : CREATED_EPISODES)
        {
            if (e.getObject().toString().equals(object.toString()))
            {
                return e;
            }
        }
        return new SpotifyEpisode(object);
    }
}