package me.superischroma.spotifywrapper;

/**
 * The type of an album
 * Can be a normal album, a single, or a compilation.
 */
public enum SpotifyAlbumType implements ValueIndicator
{
    ALBUM,
    SINGLE,
    COMPILATION;

    public String getValue()
    {
        return name().toLowerCase();
    }
}