import org.json.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * @author MJ ( owhcy )
 * this app is meant to request data from a weather api and parse the response. it was meant to learn how to use apis in java
 * minimal exception handling
 */
public class Main {

    public static void main(String[] args) {

        // splitting the link for input conveniences later on
        String link = "https://api.openweathermap.org/data/2.5/weather?q=";
        String stad = inputMethod();
        String apiKey = "&appid=484653222c6ba1af1ffb1ff835c304f2";

        // setting up the client and request
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(link+stad+apiKey)).build();

        // sending an async request, then grabbing the body which is of type String, then parsing it using the @parse method,
        // and then printing the output of the parse method and closing it off with a join
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parse)
                .thenAccept(System.out::println)
                .join();
    }

    public static String inputMethod(){
        String city = "";

        do {
            Scanner scan = new Scanner(System.in);

            System.out.println("What city's weather data would u like to know?");


            city = scan.nextLine();


        }while (city.isBlank());
        return city;

    }

    public static String parse(String responeBody){
        // initialising the variables i want from the response
        String main = "";
        double coordLON = 0;
        double coordLAT = 0;
        int id = 0;


        // putting the response body into an JSONobject. depending on if the response starts with a JSONobject or array the initial step might be different
        JSONObject weer = new JSONObject(responeBody);
            // grabbing the first object which is a normal JSONobject and not an JSONarray and putting it in a variable
        coordLON = weer.getJSONObject("coord").getDouble("lon");
        coordLAT = weer.getJSONObject("coord").getDouble("lat");

        // The next part of the JSON i want parsed is inside of an  JSONarray, so i put that into a JSONArray and initialize it with the key
        JSONArray weatherArray = weer.getJSONArray("weather");


        // i iterate through the array and grab the data i want and put it into variables
        for (int i = 0; i < weatherArray.length(); i++) {
                id = weatherArray.getJSONObject(i).getInt("id");
                main = weatherArray.getJSONObject(i).getString("main");

            }

        // a really messy way for output, this project was meant for testing and learning purposes anyways so it doesnt matter
        String output = String.format("ID: %d Coords: LON=%f, LAT=%f Forecast: %s", id,coordLON, coordLAT,main);

        // returning the String variable back to the main method
            return output;


    }
}
