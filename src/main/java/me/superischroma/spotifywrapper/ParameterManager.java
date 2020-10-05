package me.superischroma.spotifywrapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Simple way of handling URL parameters.
 */
public class ParameterManager
{
    private String parameters = "";

    public void add(String key, String value)
    {
        try
        {
            if (parameters.length() == 0)
                parameters += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            else
                parameters += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {} // should never have to catch
    }

    public String getParameters()
    {
        return parameters;
    }
}