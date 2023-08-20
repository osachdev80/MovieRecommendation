package movierecommendation.service;

import movierecommendation.model.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieService {
    /**
     * Adds a new movie to the system.
     *
     * @param movie The movie to be added.
     * @return ResponseEntity with a status and message.
     */
    ResponseEntity<String> addMovie(Movie movie);

    /**
     * Retrieves all movies in the system.
     *
     * @return List of movies.
     */
    List<Movie> getAllMovies();

    /**
     * Rates a movie with the given rating.
     *
     * @param movieId The ID of the movie to be rated.
     * @param rating  The rating value.
     * @return ResponseEntity with a status and message.
     */
    ResponseEntity<String> rateMovie(Long movieId, double rating);
    /**
     * Recommends movies by ratings and genre.
     *
     * @param genre The genre for filtering (optional).
     * @return List of recommended movies.
     */
    List<Movie> recommendMoviesByRatingsAndGenre(String genre);

}
