package movierecommendation.service;

import lombok.extern.slf4j.Slf4j;
import movierecommendation.exception.InvalidMovieDataException;
import movierecommendation.exception.MovieAlreadyExistsException;
import movierecommendation.model.Movie;
import movierecommendation.repository.MovieRepository;
import movierecommendation.util.MovieValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MovieServiceImpl implements MovieService{

    private MovieRepository movieRepository;
    private MovieValidator movieValidator;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieValidator movieValidator) {
        this.movieRepository = movieRepository;
        this.movieValidator = movieValidator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> addMovie(Movie movie) {
        try {
        if (!movieValidator.validateMovie(movie)) {
            throw new InvalidMovieDataException("Missing mandatory fields: title, genre, and director are required");
        }

        if (movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())) {
            throw new MovieAlreadyExistsException("Movie already exists");
        }

        Movie savedMovie = movieRepository.save(movie);
        if (savedMovie != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add movie");
        }
        } catch (InvalidMovieDataException e) {
            log.info("Missing mandatory fields: title, genre, and director are required for {} ", movie);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MovieAlreadyExistsException e) {
            log.info("Movie already exists for {} ",movie);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> rateMovie(Long movieId, double rating) {
        if (rating < 1 || rating > 5) {
            log.info("Invalid rating value: {}", rating);
            return ResponseEntity.badRequest().body("Invalid rating value. Rating must be between 1 and 5.");
        }
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            double currentTotalRating = movie.getAverageRating() * movie.getNumberOfRatings();
            int newNumberOfRatings = movie.getNumberOfRatings() + 1;
            double newAverageRating = (currentTotalRating + rating) / newNumberOfRatings;

            movie.setAverageRating(newAverageRating);
            movie.setNumberOfRatings(newNumberOfRatings);
            movieRepository.save(movie);
            log.info("Movie rated successfully for ID {}", movieId);
            return ResponseEntity.ok("Movie rated successfully");
        } else {
            log.info("Movie not found for ID {}", movieId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie Not Found");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Movie> recommendMoviesByRatingsAndGenre(String genre) {
        Sort sort = Sort.by(Sort.Direction.DESC, "averageRating");
        if (genre != null) {
            return movieRepository.findByGenreIgnoreCase(genre, sort);
        } else {
            return movieRepository.findAll(sort);
        }
    }

}
