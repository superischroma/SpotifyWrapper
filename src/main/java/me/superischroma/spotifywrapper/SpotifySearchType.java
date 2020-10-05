package me.superischroma.spotifywrapper;

/**
 * The type of a Spotify search.
 * Can be an album, artist, playlist, track, show, or episode search.
 */
public enum SpotifySearchType implements ValueIndicator
{
    ALBUM,
    ARTIST,
    PLAYLIST,
    TRACK,
    SHOW,
    EPISODE;

    public String getValue()
    {
        return name().toLowerCase();
    }
}
