package com.dev.deijai.soluesstolio;

import android.content.Intent;
import android.os.Bundle;

import com.dev.deijai.soluesstolio.endpoint.Endpoint;
import com.dev.deijai.soluesstolio.model.NotaFiscalBean;
import com.dev.deijai.soluesstolio.model.NotaFiscalDao;
import com.dev.deijai.soluesstolio.ui.adapter.AdapterListaCliente;
import com.dev.deijai.soluesstolio.webconfig.APIClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.sdk.order.*;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.Payment;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerView;
    private Endpoint endpoint;
    public static String valor = null;
    private TextView textView;
    private FloatingActionButton botaoPagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        endpoint = APIClient.getClient().create(Endpoint.class);
        botaoPagamento = findViewById(R.id.fabPag);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fabPag);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pagamento, R.id.nav_cancelamento, R.id.nav_lista_pagamento, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        botaoPagamento.setEnabled(false);


        botaoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Credenciais
                Credentials credentials = new Credentials("8L24NGKvA1ZqIITT10GcV6Iln2vi5qaNuvyoVUwDMaqUfr8tq2", "tYVLDVObE71O49xiGfOoSz2fEwS61uTKUR3wvn0B8KSlMHcFpl");
                //Criando ordem
                 final OrderManager orderManager = new OrderManager(credentials, MainActivity.this);

                ServiceBindListener serviceBindListener = new ServiceBindListener() {

                    @Override public void onServiceBoundError(Throwable throwable) {
                        //Ocorreu um erro ao tentar se conectar com o serviço OrderManager
                       // Toast.makeText(getApplicationContext(), "onServiceBoundError", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onServiceBound() {
                        //Você deve garantir que sua aplicação se conectou com a LIO a partir desse listener
                        //A partir desse momento você pode utilizar as funções do OrderManager, caso contrário uma exceção será lançada.
                       // Toast.makeText(getApplicationContext(), "onServiceBound", Toast.LENGTH_LONG).show();
                        //Log.d("VALOR====", ""+MainActivity.valor);


                        final Order order = orderManager.createDraftOrder("STO DE PEDIDOS");

                        NotaFiscalDao notaFiscalDao = new NotaFiscalDao(getApplicationContext());


                        final List<NotaFiscalBean> notas = notaFiscalDao.lista();

                        for(NotaFiscalBean nota : notas){
                            String valor = nota.getVALOR();
                            int qtd = 1;

                            //Tratanto o valor
                            Float valorFormatodo = (Float.parseFloat(valor) * 100);
                            Integer vFinal = valorFormatodo.intValue();
                            order.addItem(nota.getNUMNOTA(), nota.getCLIENTE(), vFinal, qtd, nota.getNUMTRANSVENDA());
                        }




                        final PaymentListener paymentListener = new PaymentListener() {
                            @Override
                            public void onStart() {
                                Log.d("PAGAMENTO", "COMEÇOU AQUI...");
                                //Toast.makeText(getApplicationContext(), "onStart PAGAMENTO COMEÇOU AQUI...", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPayment(@NotNull final Order order) {
                                Log.d("PAGAMENTO", "FOI REALIZADO...");
                                //Toast.makeText(getApplicationContext(), "onPayment PAGAMENTO FOI REALIZADO...", Toast.LENGTH_LONG).show();

                                //configurar a data de pagamento
                                Calendar c = Calendar.getInstance();

                                String data = null;
                                Integer dia = c.get(Calendar.DATE);
                                Integer ano = c.get(Calendar.YEAR);

                                Integer mes = c.get(Calendar.MONTH);


                                if(mes.equals(0)) {
                                    data = "jan";
                                } else if (mes.equals(1)){
                                    data = "fev";
                                }
                                else if (mes.equals(2)){
                                    data = "mar";
                                }
                                else if (mes.equals(3)){
                                    data = "abr";
                                }
                                else if (mes.equals(4)){
                                    data = "mai";
                                }
                                else if (mes.equals(5)){
                                    data = "jun";
                                }
                                else if (mes.equals(6)){
                                    data = "jul";
                                }
                                else if (mes.equals(7)){
                                    data = "ago";
                                }
                                else if (mes.equals(8)){
                                    data = "set";
                                }
                                else if (mes.equals(9)){
                                    data = "out";
                                }
                                else if (mes.equals(10)){
                                    data = "nov";
                                }
                                else if (mes.equals(11)){
                                    data = "dez";
                                }

                                final String dataFormatada = dia+"-"+data+"-"+ano;


                                //pegando dados do order

                                for (final Payment payment: order.getPayments()) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotaFiscalDao notaFiscalDao = new NotaFiscalDao(getApplicationContext());
                                            List<NotaFiscalBean> n = notaFiscalDao.lista();

                                            for(NotaFiscalBean nn : n){

                                                final JSONObject dados = new JSONObject();
                                                try {

                                                    dados.put("numtransvenda" ,nn.getNUMTRANSVENDA());
                                                    dados.put("prest", nn.getPREST());
                                                    dados.put("codcob", payment.getPaymentFields().get("primaryProductName"));
                                                    dados.put("codcoborig", payment.getPaymentFields().get("secondaryProductName"));
                                                    dados.put("valor", payment.getAmount());
                                                    dados.put("presttef", payment.getPaymentFields().get("numberOfQuotas"));
                                                    dados.put("nsutef", payment.getCieloCode());
                                                    dados.put("codautorizacaotef", payment.getAuthCode());
                                                    dados.put("codadmcartao", "00125");
                                                    dados.put("tipooperacaotef", payment.getPaymentFields().get("v40Code"));
                                                    dados.put("valorjuros", payment.getPaymentFields().get("interestAmount"));
                                                    dados.put("idtransacao", payment.getPaymentFields().get("paymentTransactionId"));
                                                    dados.put("conector", "CIELO");
                                                    dados.put("JSON_CIELO", payment.getPaymentFields());
                                                    dados.put("codbandeira", payment.getBrand());
                                                    dados.put("data_desd", "");
                                                    dados.put("exportado", "N");
                                                    dados.put("data_pagamento", dataFormatada);



/*

                                            dados.put("numtransvenda" ,"55444");
                                            dados.put("prest", "3");
                                            dados.put("codcob", "3");
                                            dados.put("codcoborig", "deb");
                                            dados.put("valor", 2999);
                                            dados.put("presttef", 3);
                                            dados.put("nsutef","000000");
                                            dados.put("codautorizacaotef", "444444");
                                            dados.put("codadmcartao", "00125");
                                            dados.put("tipooperacaotef", "66");
                                            dados.put("valorjuros", 0);
                                            dados.put("idtransacao", "t");
                                            dados.put("conector", "CIELO");
                                            dados.put("JSON_CIELO", "jjkkjk");
                                            dados.put("codbandeira","2");
                                            dados.put("data_desd", "");
                                            dados.put("exportado", "N");
                                            dados.put("data_pagamento", dataFormatada);

 */



                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }






                                                String url = "http://170.83.160.85:8080/projetos/api_teste/api/nfe/post/";


                                                Log.d("URL", "URL"+url);
                                                OkHttpClient client = new OkHttpClient();
                                                String json = dados.toString();


                                                MediaType mediaType = MediaType.parse("application/json");
                                                RequestBody body = RequestBody.create(mediaType, json);

                                                Request request = new Request.Builder()
                                                        .url(url)
                                                        .post(body)
                                                        .build();

                                                Log.d("Request", request.body().contentType().toString());
                                                try {
                                                    okhttp3.Response resp = client.newCall(request).execute();

                                                    if (resp.isSuccessful()){
                                                        Log.d("RESPONSE", resp.body().string());

                                                    } else {
                                                        Log.d("RESPONSE", resp.body().string());
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                order.close();

                                            }
                                        }
                                    }).start();
                                }

                            }



                            @Override
                            public void onCancel() {
                                Log.d("PAGAMENTO", "CANCELADO...");
                            }

                            @Override
                            public void onError(@NotNull PaymentError paymentError) {
                                Log.d("PAGAMENTO", "HOUVE UM ERRO...");
                            }
                        };



                        orderManager.placeOrder(order);
                        orderManager.checkoutOrder(order.getId(), paymentListener);


                        orderManager.unbind();

                    }



                    @Override
                    public void onServiceUnbound() {
                        // O serviço foi desvinculado
                        //Toast.makeText(getApplicationContext(), "onServiceUnbound O serviço foi desvinculado", Toast.LENGTH_LONG).show();
                    }
                };

                orderManager.bind(MainActivity.this,  serviceBindListener);




               // Toast.makeText(getApplicationContext(), "Clicou....", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Toast.makeText(getApplicationContext(), "onActivityResult() aqi", Toast.LENGTH_LONG).show();


        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null){
                if (result.getContents() == null){
                    Toast.makeText(getApplicationContext(), "Cancelar o escaneamento", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Else aqui", Toast.LENGTH_LONG).show();
                    String codigoLido = result.getContents();
                    //http://localhost:8080/api_cielo/api/nfe/29190703932949000541550010001820071111339463
                    //String codigoLido = "53120107513341000100550010000186331111004010";

                    if (codigoLido != null){
                        Call<NotaFiscalBean> call = endpoint.buscarDados(codigoLido);
                        call.enqueue(new Callback<NotaFiscalBean>() {
                            @Override
                            public void onResponse(Call<NotaFiscalBean> call, Response<NotaFiscalBean> response) {
                                NotaFiscalBean notaFiscalBean = response.body();
                                textView = findViewById(R.id.TextViewValorTotalId);

                                if ( !check(notaFiscalBean.getNUMTRANSVENDA())){

                                    NotaFiscalDao notaFiscalDao = new NotaFiscalDao(getApplicationContext());

                                    notaFiscalDao.gravarPagamento(notaFiscalBean);
                                    MainActivity.valor = notaFiscalDao.sumValorTotal();

                                    if (MainActivity.valor != null){
                                        botaoPagamento.setEnabled(true);
                                        Log.d("VALOR IF====", ""+MainActivity.valor);
                                    } else {
                                        botaoPagamento.setEnabled(false);
                                        Log.d("VALOR ELSE====", ""+MainActivity.valor);
                                    }

                                    Log.d("VALOR ====", "" + MainActivity.valor);
                                    textView.setText("TOTAL A PAGAR: "+MainActivity.valor);
                                   // Log.d("VALOR ====", "" + valor);
                                    AdapterListaCliente adapterListaClientea = new AdapterListaCliente(notaFiscalDao.lista());

                                    recyclerView = findViewById(R.id.recyclerViewClienteId);
                                    //textView.setText(MainActivity.valor);




                                    //Configurar o RecyclerView
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(adapterListaClientea);
                                    Log.d("TRUE", "NOTA NÂO EXISTE");
                                } else{
                                    NotaFiscalDao notaFiscalDao = new NotaFiscalDao(getApplicationContext());
                                    MainActivity.valor = notaFiscalDao.sumValorTotal();
                                    textView.setText("TOTAL A PAGAR: "+MainActivity.valor);
                                    AdapterListaCliente adapterListaClientea = new AdapterListaCliente(notaFiscalDao.lista());
                                    Toast.makeText(getApplicationContext(), "Nota Fiscal Já Incluída!", Toast.LENGTH_LONG).show();
                                    //String valor = notaFiscalDao.sumValorTotal();
                                    //textView.setText(MainActivity.valor);

                                    Log.d("VALOR ====", "" + valor);
                                    recyclerView = findViewById(R.id.recyclerViewClienteId);




                                    //Configurar o RecyclerView
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(adapterListaClientea);
                                }





                            }

                            @Override
                            public void onFailure(Call<NotaFiscalBean> call, Throwable t) {
                                Log.d("onFailure", "onFailure");
                                Toast.makeText(getApplicationContext(), "Nota Fiscal não encotrada, tente novamente.", Toast.LENGTH_LONG).show();
                            }
                        });

                        Log.d("aqui", "codigo não nulo");
                        Log.d("call", "call "+call.execute());

                    } else{
                        Toast.makeText(getApplicationContext(), "Falha na leitura do código de barra, tente novamente ", Toast.LENGTH_LONG).show();
                    }

                }

            } else {
                super.onActivityResult(requestCode, resultCode, data );
            }



        }catch (Throwable e){
            // Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public boolean check(String numTransvenda){
        NotaFiscalDao notaFiscalDao = new NotaFiscalDao(this);

        if (notaFiscalDao.buscarNota(numTransvenda)){
            return true;
        } else {
            return false;
        }
    }


    //METODO PARA ESCANEAR
    public void escanear(View view){
        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);


        intent.setPrompt("Escanear Código de Barras");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();


    }

    @Override
    protected void onStop() {
        //Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_LONG).show();
        super.onStop();
    }

    @Override
    protected void onPause() {
        //Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_LONG).show();
        super.onPause();
    }

    @Override
    protected void onResume() {
       // Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_LONG).show();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        //Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_LONG).show();
        super.onRestart();
    }
}
