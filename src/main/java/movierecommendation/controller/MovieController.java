package movierecommendation.controller;

import lombok.extern.slf4j.Slf4j;
import movierecommendation.model.Movie;
import movierecommendation.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieController {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping(path = "/getMovies")
    public List<Movie> getAllMovies() {
        log.info("Entering getAllMovies() Api ");
        return movieRepository.findAll();
    }

    @PostMapping(path = "/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        log.info("Entering addMovie() Api ");
        if (movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie already exists");
        }

        Movie savedMovie = movieRepository.save(movie);
        if (savedMovie != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add movie");
        }
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateMovie(
            @PathVariable Long id,
            @RequestParam double rating) {
        log.info("Entering rateMovie() Api ");
        if (rating < 1 || rating > 5) {
            log.info("Invalid rating value: {}", rating);
            return ResponseEntity.badRequest().body("Invalid rating value. Rating must be between 1 and 5.");
        }
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            double currentTotalRating = movie.getAverageRating() * movie.getNumberOfRatings();
            int newNumberOfRatings = movie.getNumberOfRatings() + 1;
            double newAverageRating = (currentTotalRating + rating) / newNumberOfRatings;

            movie.setAverageRating(newAverageRating);
            movie.setNumberOfRatings(newNumberOfRatings);
            movieRepository.save(movie);
            log.info("Movie rated successfully for ID {}", id);
            return ResponseEntity.ok("Movie rated successfully");
        } else {
            log.info("Movie not found for ID {}", id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie Not Found");
        }
    }


    @GetMapping("/recommend")
    public List<Movie> recommendMoviesByRatingsAndGenre(
            @RequestParam(required = false) String genre) {
        log.info("Entering recommendMoviesByRatingsAndGenre() Api ");
        Sort sort = Sort.by(Sort.Direction.DESC, "averageRating");
        List<Movie> recommendedMovies;

        if (genre != null) {
            recommendedMovies = movieRepository.findByGenreIgnoreCase(genre, sort);
        } else {
            recommendedMovies = movieRepository.findAll(sort);
        }

        return recommendedMovies;
    }
}
