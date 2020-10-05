package me.superischroma.spotifywrapper;

import lombok.Getter;
import org.json.simple.JSONObject;

/**
 * The captured state of a Spotify playlist track.
 */
public class SpotifyPlaylistTrack
{
    @Getter
    private String addedAt;
    @Getter
    private SpotifyUser addedBy;
    @Getter
    private boolean local;
    @Getter
    private SpotifyTrack track;

    public SpotifyPlaylistTrack(JSONObject object)
    {
        if (object.get("added_at") != null) this.addedAt = (String) object.get("added_at");
        if (object.get("added_by") != null) this.addedBy = new SpotifyUser((JSONObject) object.get("added_by"));
        if (object.get("is_local") != null) this.local = (boolean) object.get("is_local");
        if (object.get("track") != null) this.track = SpotifyTrack.getTrack((JSONObject) object.get("track"));
    }
}