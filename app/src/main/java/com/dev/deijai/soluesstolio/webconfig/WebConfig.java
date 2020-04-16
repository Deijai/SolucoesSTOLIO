package com.dev.deijai.soluesstolio.webconfig;

import com.dev.deijai.soluesstolio.endpoint.Endpoint;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class WebConfig {

    private final Retrofit WebConfig;

    //Link para retorno
    //http://ec2-54-94-247-145.sa-east-1.compute.amazonaws.com:8280/?chave=52171010912900000240550010018133591111019109
    //http://170.83.160.85:8080/api_cielo/api/nfe/
    //chave NF-e 53120107513341000100550010000186331111004010

    public WebConfig() {
        this.WebConfig = new Retrofit.Builder()
                .baseUrl("https://170.83.160.85:8080/projetos/api_teste/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public String postEndpoint(String json) throws IOException {


        //URL url = new URL("http://170.83.160.85:8080/api_cielo/api/nfe/post/");
        URL url = new URL("https://170.83.160.85:8080/projetos/api_teste/api/nfe/post/");

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.setDoOutput(true);


        PrintStream printStream =
                new PrintStream(connection.getOutputStream());
        printStream.println(json);

        connection.connect();

        String jsonDeResposta =
                new Scanner(connection.getInputStream()).next();

        return jsonDeResposta;
    }

    public Endpoint getEndpoint() {
        return this.WebConfig.create(Endpoint.class);
    }


}
