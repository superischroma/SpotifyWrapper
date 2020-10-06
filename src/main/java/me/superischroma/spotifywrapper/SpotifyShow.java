package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpotifyShow extends SpotifyObject
{
    private static List<SpotifyShow> CREATED_SHOWS = new ArrayList<>();

    @Getter
    private List<String> markets;
    @Getter
    private List<SpotifyCopyright> copyrights;
    @Getter
    private String description;
    @Getter
    private boolean explicit;
    @Getter
    private List<SpotifyEpisode> episodes;
    @Getter
    private List<SpotifyImage> images;
    @Getter
    private boolean externallyHosted;
    @Getter
    private List<String> languages;
    @Getter
    private String mediaType;
    @Getter
    private String name;
    @Getter
    private String publisher;
    @Getter
    private long episodeCount;

    private SpotifyShow(JSONObject object)
    {
        super(object);
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
        if (object.get("description") != null) this.description = (String) object.get("description");
        if (object.get("explicit") != null) this.explicit = (boolean) object.get("explicit");
        if (object.get("episodes") != null)
        {
            this.episodes = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("episodes");
            for (Object o : a)
                this.episodes.add(SpotifyEpisode.getEpisode((JSONObject) o));
        }
        if (object.get("images") != null)
        {
            this.images = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("images");
            for (Object o : a)
                this.images.add(new SpotifyImage((JSONObject) o));
        }
        if (object.get("is_externally_hosted") != null) this.externallyHosted = (boolean) object.get("is_externally_hosted");
        if (object.get("languages") != null)
        {
            this.languages = new ArrayList<>();
            JSONArray a = (JSONArray) object.get("languages");
            for (Object o : a)
                this.languages.add((String) o);
        }
        if (object.get("media_type") != null) this.mediaType = (String) object.get("media_type");
        if (object.get("name") != null) this.name = (String) object.get("name");
        if (object.get("publisher") != null) this.publisher = (String) object.get("publisher");
        if (object.get("total_episodes") != null) this.episodeCount = (long) object.get("total_episodes");
        CREATED_SHOWS.add(this);
    }

    public static SpotifyShow getShow(JSONObject object)
    {
        for (SpotifyShow s : CREATED_SHOWS)
        {
            if (s.getObject().toString().equals(object.toString()))
            {
                return s;
            }
        }
        return new SpotifyShow(object);
    }
}