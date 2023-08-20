package movierecommendation.controller;

import lombok.extern.slf4j.Slf4j;
import movierecommendation.model.Movie;
import movierecommendation.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Retrieve a list of all movies.
     * @return
     */
    @GetMapping(path = "/getMovies")
    public List<Movie> getAllMovies() {
        log.info("Entering getAllMovies() Api ");
        return movieService.getAllMovies();
    }

    /**
     * Add a new movie to the collection.
     * @param movie
     * @return
     */
    @PostMapping(path = "/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        log.info("Entering addMovie() Api ");
       return movieService.addMovie(movie);
    }

    /**
     * Rate a movie by providing a rating.
     * @param movieId
     * @param rating
     * @return
     */
    @PostMapping(path = "/{movieId}/rate")
    public ResponseEntity<String> rateMovie(
            @PathVariable Long movieId,
            @RequestParam double rating) {
        log.info("Entering rateMovie() Api ");
        return movieService.rateMovie(movieId, rating);
    }


    /**
     * Get movie recommendations based on ratings and optional genre.
     * @param genre
     * @return
     */
    @GetMapping("/recommend")
    public List<Movie> recommendMoviesByRatingsAndGenre(
            @RequestParam(required = false) String genre) {
        log.info("Entering recommendMoviesByRatingsAndGenre() Api ");
        return movieService.recommendMoviesByRatingsAndGenre(genre) ;
    }
}
