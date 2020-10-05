# SpotifyWrapper
A simple Spotify API wrapper.
The wrapper's use is as simple as accessing the Spotify class and using methods from it.

## Example
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

### Result
```Lil Darkie```
###
```java
public static void main(String[] args) throws Exception
{
    // Assuming you have already defined CLIENT_ID and CLIENT_SECRET
    // Retrieve your token to access the Spotify API
    String token = Spotify.getAccessToken(CLIENT_ID, CLIENT_SECRET);
    // Get a track using its ID (Spotify URI without the prefixes and such)
    SpotifyTrack track = Spotify.getTrack(token, "79s5XnCN4TJKTVMSmOx8Ep");
    System.out.println(track.getName());
}
```

### Result
```Dior```
## Documentation
#### All methods specified below are within the `Spotify` class.
#### `getAccessToken(String clientID, String clientSecret)`
Returns an access token that can be used to access the rest of the Spotify API. If you don't know how to get a client ID and client secret, please go to the [Spotify dashboard](https://developer.spotify.com/dashboard/) in order to create an application.
#### `searchByTrack(String accessToken, String query, int limit, int resultOffset)`
Returns a List with tracks from the search.
#### `searchByAlbum(String accessToken, String query, int limit, int resultOffset)`
Returns a List with albums from the search.
#### `searchByArtist(String accessToken, String query, int limit, int resultOffset)`
Returns a List with artists from the search.
#### `searchByPlaylist(String accessToken, String query, int limit, int resultOffset)`
Returns a List with playlists from the search.
#### `getTrack(String accessToken, String id)`
Returns a SpotifyTrack object containing all of the track's information.
#### `getAlbum(String accessToken, String id)`
Returns a SpotifyAlbum object containing all of the album's information.
#### `getArtist(String accessToken, String id)`
Returns a SpotifyArtist object containing all of the artist's information.
#### `getPlaylist(String accessToken, String id)`
Returns a SpotifyPlaylist object containing all of the playlist's information.
#### `getUser(String accessToken, String id)`
Returns a SpotifyUser object containing all of the user's information.
#### `findTrack(String accessToken, String name)`
Returns the first track given when the query is the name provided.
#### `findAlbum(String accessToken, String name)`
Returns the first album given when the query is the name provided.
#### `findArtist(String accessToken, String name)`
Returns the first artist given when the query is the name provided.
#### `findPlaylist(String accessToken, String name)`
Returns the first playlist given when the query is the name provided.
## Implementation
This Spotify wrapper can be implemented two ways:
### Maven
#### Repository
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
#### Dependency
```xml
<dependency>
    <groupId>com.github.superischroma</groupId>
    <artifactId>SpotifyWrapper</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```
### Vanilla Java
Simply add the latest release from the [release page](https://github.com/superischroma/SpotifyWrapper/releases) to your project's classpath.
