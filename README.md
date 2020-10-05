# SpotifyWrapper
A simple Spotify API wrapper.
The wrapper's use is as simple as accessing the Spotify class and using methods from it.

Example:
```java
public static void main(String[] args) throws Exception
{
    // Assuming you have already defined CLIENT_ID and CLIENT_SECRET
    // Retrieve your token to access the Spotify API
    String token = Spotify.getAccessToken(CLIENT_ID, CLIENT_SECRET);
    // Search by artist with a specified query, result count, and result starting point and store the results in a List.
    List<SpotifyArtist> artists = Spotify.searchByArtist(token, "lil darkie", 10, 0);
    // Pull the first artist found out of the List.
    SpotifyArtist first = artists.get(0); // Assuming Lil Darkie is the first result.
    // Here is where you would do stuff with the artist.
    // For the example, we are just printing the name of the artist.
    System.out.println(first.getName());
}
```

Result:

```Lil Darkie```

Documentation for the wrapper can be found within the code.
