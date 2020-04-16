package com.dev.deijai.soluesstolio.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.deijai.soluesstolio.MainActivity;
import com.dev.deijai.soluesstolio.R;
import com.dev.deijai.soluesstolio.model.NotaFiscalBean;
import com.dev.deijai.soluesstolio.model.NotaFiscalDao;
import com.dev.deijai.soluesstolio.ui.adapter.AdapterListaCliente;
import com.dev.deijai.soluesstolio.webconfig.WebConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagamentoFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;

    public PagamentoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(), "onCreateView() PagamantoFragment", Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_pagamento, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewClienteId);
        textView = root.findViewById(R.id.TextViewValorTotalId);



        NotaFiscalDao notaFiscalDao = new NotaFiscalDao(getContext());
        notaFiscalDao.DeletarDados();

        AdapterListaCliente adapterListaClientea = new AdapterListaCliente(notaFiscalDao.lista());


        //Configurar o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterListaClientea);

        return root;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getContext(), "onActivityResult() PagamantoFragment", Toast.LENGTH_LONG).show();
    }
}
