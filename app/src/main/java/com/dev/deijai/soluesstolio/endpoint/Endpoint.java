package com.dev.deijai.soluesstolio.endpoint;

import com.dev.deijai.soluesstolio.model.NotaFiscalBean;
import com.fasterxml.jackson.annotation.JsonInclude;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Endpoint {

    @GET("nfe/{nfe}")
    Call<NotaFiscalBean>buscarDados(@Path("nfe") String chavenfe);

    @POST
    Call<NotaFiscalBean>InserirDados(@Field("CODCLI") Integer codcli,
                                 @Field("NUMTRANSVENDA") String numTransvenda,
                                 @Field("CLIENTE") String cliente,
                                 @Field("CHAVENFE") String chaveNfe,
                                 @Field("NUMNOTA") String numNota,
                                 @Field("PREST") String prest,
                                 @Field("VALOR") String valor);

}
