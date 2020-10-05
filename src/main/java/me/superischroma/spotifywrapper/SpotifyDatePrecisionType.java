package me.superischroma.spotifywrapper;

/**
 * The precision of dates from a Spotify object.
 * Can be a year, month, or day.
 */
public enum SpotifyDatePrecisionType implements ValueIndicator
{
    YEAR,
    MONTH,
    DAY;

    @Override
    public String getValue()
    {
        return name().toLowerCase();
    }
}