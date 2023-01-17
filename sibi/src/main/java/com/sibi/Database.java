package com.sibi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;


public class Database {
    static Driver driver = GraphDatabase.driver("neo4j+s://d33b7603.databases.neo4j.io", AuthTokens.basic("neo4j", "A9IJ7u_6aYW3hXQWL4ot_vNxfU6Zsjx5khk9MssOCLA"));
    static Session session = driver.session();

    List<String> retrieveGenres()
    {
        int index = 0;
        List<String> genres = new ArrayList<String>();

        Result result = session.run("MATCH (g:Genre) RETURN g.listed_in as genre");
        while (result.hasNext()) {
            Record record = result.next();
            genres.add(index, record.get("genre").asString());
            index++;
        }

        Collections.sort(genres);
        return genres;
    }

    List<String> suggestFilm(String category)
    {
        List<String> films = new ArrayList<String>();
        List<Integer> rating = new ArrayList<Integer>();
        System.out.println("Here are some " + category + " books that you might like:");
        System.out.println("--------------------");

        Result result = session.run("MATCH (f:Film)-[ES_GENERO_DE]->(g:Genre {listed_in: \"" + category + "\"}) RETURN f.title AS title, f.rating AS stars");
        while (result.hasNext()) {
            Record record = result.next();
            films.add(record.get("title").asString());
            rating.add(record.get("rating", 0));
        }

        //Sorting based on rating of the books
        final Map<String, Integer> filmRatingMap = new HashMap<>();
        for(int i = 0; i < films.size(); i++) {
            filmRatingMap.put(films.get(i), rating.get(i));
        }
        Collections.sort(films, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return filmRatingMap.get(o1) - filmRatingMap.get(o2);
            }
        });

        return films;
    }

    String retrieveDescription(String bookTitle)
    {
        Result result = session.run("MATCH (f:Film {title: \"" + bookTitle + "\"}) RETURN f.description AS description");
        Record record = result.next();

        return record.get("description").asString();
    }

    String getFilmCategory(String bookTitle)
    {
        Result result = session.run("MATCH (g:Genre)<-[ES_GENERO_DE]-(:Film {title: \"" + bookTitle + "\"}) RETURN g.listed_in AS category");

        Record record = result.next();

        return record.get("category").asString();
    }

    List<String> retrieveFilms()
    {
        int index = 0;
        List<String> films = new ArrayList<String>();

        Result result = session.run("MATCH (f:Film) RETURN f.title as title");
        while (result.hasNext()) {
            Record record = result.next();
            films.add(index, record.get("title").asString());
            index++;
        }

        Collections.sort(films);
        return films;
    }

    //Close program
    static void terminate()
    {
        session.close();
        driver.close();
        System.exit(0);
    }
}
