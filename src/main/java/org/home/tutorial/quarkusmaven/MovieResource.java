package org.home.tutorial.quarkusmaven;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")
@Tag(name = "Movie Resource", description = "Movie Rest APIs")
public class MovieResource {

    public static List<Movie> movies = new ArrayList<>();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getMovies",
            summary = "Get Movies",
            description = "Get All Movies In The List"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Completed.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getMovies(){
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            operationId = "countMovies",
            summary = "count Number of Movies",
            description = "Size Of  The List"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Integer countMovies(){
        return movies.size();
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createMovie",
            summary = "Create Movie",
            description = "Adding Movie to the list"
    )
    @APIResponse(
            responseCode = "201",
            description = "Movie Created ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response createMovie(
            @RequestBody(
                    description = "Movie to Create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class))
            )
            Movie newMovie){
        movies.add(newMovie);
        return Response.status(Response.Status.CREATED).entity(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "Update a Movie",
            summary = "Movie Updated.",
            description = "Update Movie to the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Update Movie in the list",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateMovie(
            @Parameter(
                    description = "Movie id",
                    required = true
            )
            @PathParam("id") Long id,
            @Parameter(
                    description = "Movie Title",
                    required = true
            )
            @PathParam("title") String title ) {
        movies = movies.stream().map(movie -> {
            if (movie.getId().equals(id)) {
                movie.setTitle(title);
            }
            return movie;
        }).collect(Collectors.toList());
        return Response.ok(movies).build();
    }


    @DELETE
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(
            operationId = "DELETE a Movie",
            summary = "Movie Deleted.",
            description = "delete Movie from list"
    )
    @APIResponse(
            responseCode = "204",
            description = "delete Movie from the list",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Movie Not Valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteMovie(
            @Parameter(
                    description = "Movie id",
                    required = true
            )
            @PathParam("id") Long id){
            Optional<Movie> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();
            boolean removed = false;
            if (movieToDelete.isPresent()){
                removed = movies.remove(movieToDelete.get());
            }
            if (removed){
                return Response.noContent().build();
            }

            return Response.status(Response.Status.BAD_REQUEST).build();
    }

}



