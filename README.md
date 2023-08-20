# Movie Recommendation API

This API provides endpoints for managing and recommending movies. It allows you to retrieve movie data, add movies, rate movies, and get movie recommendations based on ratings and genres.

## Table of Contents

- [Endpoints](#endpoints)
- [Usage](#usage)
- [Getting Started](#getting-started)
- [Dependencies](#dependencies)

## Endpoints

### Get All Movies

- **URL**: `/movies/getMovies`
- **Method**: `GET`
- **Description**: Retrieve a list of all movies.
- **Usage**: Use this endpoint to get a list of all available movies.

### Add a Movie

- **URL**: `/movies/addMovie`
- **Method**: `POST`
- **Description**: Add a new movie to the collection.
- **Usage**: Send a JSON payload with movie details to add a new movie.

### Rate a Movie

- **URL**: `/movies/{movieId}/rate`
- **Method**: `POST`
- **Description**: Rate a movie by providing a rating.
- **Usage**: Provide the movie ID and a rating (between 1 and 5) to rate a movie.

### Recommend Movies

- **URL**: `/movies/recommend`
- **Method**: `GET`
- **Description**: Get movie recommendations based on ratings and optional genre.
- **Usage**: Optionally provide a genre to get movie recommendations based on ratings and genre.

## Usage

Here's how you can use the Movie Recommendation API:

- **Retrieve all movies**: Send a GET request to `/movies/getMovies`.

- **Add a new movie**: Send a POST request to `/movies/addMovie` with a JSON payload containing movie details.

- **Rate a movie**: Send a POST request to `/movies/{movieId}/rate` with the movie ID and rating as query parameters.

- **Get movie recommendations**: Send a GET request to `/movies/recommend`. Optionally, provide a `genre` query parameter for genre-based recommendations.

## Getting Started

To run the Movie Recommendation API locally, follow these steps:

1. Clone this repository: `git clone git@github.com:osachdev80/MovieRecommendation.git`
2. Build the project using your preferred build tool.
3. Start the application.
4. Access the API using the provided endpoints.

## Dependencies

The Movie Recommendation API is built using Java and the Spring Framework. It uses the following dependencies:

- [Spring Boot](https://spring.io/projects/spring-boot): For building and running the application.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa): For data access and persistence.
- [Spring Web](https://spring.io/projects/spring-web): For building RESTful APIs.
- [Lombok](https://projectlombok.org/): For reducing boilerplate code.
- [H2 Database](https://www.h2database.com/html/main.html): An embedded database for development.


