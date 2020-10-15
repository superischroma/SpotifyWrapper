# SpotifyWrapper
A simple Spotify API wrapper.
The wrapper's use is as simple as accessing the Spotify class and using methods from it.

## Example
```java
public static void main(String[] args) throws Exception
{
    // Assuming you have already defined CLIENT_ID and CLIENT_SECRET
    // Create a new Spotify instance
    Spotify spotify = new Spotify(CLIENT_ID, CLIENT_SECRET);
    // Search by artist with a specified query, result count, and result starting point and store the results in a List.
    List<SpotifyArtist> artists = spotify.searchByArtist("lil darkie", 10, 0);
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
    // Create a new Spotify instance
    Spotify spotify = new Spotify(CLIENT_ID, CLIENT_SECRET);
    // Get a track using its ID (Spotify URI without the prefixes and such)
    SpotifyTrack track = spotify.getTrack("79s5XnCN4TJKTVMSmOx8Ep");
    System.out.println(track.getName());
}
```

### Result
```Dior```
## Documentation
#### `new Spotify(String clientID, String clientSecret)`
Instancing the Spotify class gives you access to all the methods you need to access the rest of the API. If you don't know how to get a client ID and client secret, please go to the [Spotify dashboard](https://developer.spotify.com/dashboard/) in order to create an application.
#### All methods specified below are part of an instanced `Spotify` class.
#### `searchByTrack(String query, int limit, int resultOffset)`
Returns a List with tracks from the search.
#### `searchByAlbum(String query, int limit, int resultOffset)`
Returns a List with albums from the search.
#### `searchByArtist(String query, int limit, int resultOffset)`
Returns a List with artists from the search.
#### `searchByPlaylist(String query, int limit, int resultOffset)`
Returns a List with playlists from the search.
#### `searchByShow(String query, int limit, int resultOffset)`
Returns a List with shows from the search.
#### `searchByEpisode(String query, int limit, int resultOffset)`
Returns a List with episodes from the search.
#### `getTrack(String id)`
Returns a SpotifyTrack object containing all of the track's information.
#### `getAlbum(String id)`
Returns a SpotifyAlbum object containing all of the album's information.
#### `getArtist(String id)`
Returns a SpotifyArtist object containing all of the artist's information.
#### `getPlaylist(String id)`
Returns a SpotifyPlaylist object containing all of the playlist's information.
#### `getUser(String id)`
Returns a SpotifyUser object containing all of the user's information.
#### `getShow(String id)`
Returns a SpotifyShow object containing all of the show's information.
#### `getEpisode(String id)`
Returns a SpotifyEpisode object containing all of the episode's information.
#### `findTrack(String name)`
Returns the first track given when the query is the name provided.
#### `findAlbum(String name)`
Returns the first album given when the query is the name provided.
#### `findArtist(String name)`
Returns the first artist given when the query is the name provided.
#### `findPlaylist(String name)`
Returns the first playlist given when the query is the name provided.
#### `findShow(String name)`
Returns the first show given when the query is the name provided.
#### `findEpisode(String name)`
Returns the first episode given when the query is the name provided.
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
