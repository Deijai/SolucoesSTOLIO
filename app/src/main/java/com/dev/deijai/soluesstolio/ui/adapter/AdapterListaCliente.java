package com.dev.deijai.soluesstolio.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.deijai.soluesstolio.R;
import com.dev.deijai.soluesstolio.model.NotaFiscalBean;

import java.util.List;

public class AdapterListaCliente extends RecyclerView.Adapter<AdapterListaCliente.MyViewHolder> {

    private List<NotaFiscalBean> notaFiscalList;

    public AdapterListaCliente(List<NotaFiscalBean> notaFiscalList) {
        this.notaFiscalList = notaFiscalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterListaCliente = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_lista_cliente,
                parent,
                false
        );
        return new MyViewHolder(adapterListaCliente);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NotaFiscalBean notaFiscal = notaFiscalList.get(position);
        holder.codcli.setText("Cod: "+String.valueOf(notaFiscal.getCODCLI()));
        holder.cliente.setText("Cliente: "+notaFiscal.getCLIENTE());
        holder.numtransvenda.setText("Num. Trans: ."+notaFiscal.getNUMTRANSVENDA());
        holder.chavenfe.setText("Chave: "+notaFiscal.getCHAVENFE());
        holder.numnota.setText("Nota: "+notaFiscal.getNUMNOTA());
        holder.prest.setText("Prest: "+String.valueOf(notaFiscal.getPREST()));
        holder.valor.setText("Valor: "+notaFiscal.getVALOR());

    }

    @Override
    public int getItemCount() {
        return notaFiscalList.size();
    }

    //Classe interna View Hoder
    public class MyViewHolder extends RecyclerView.ViewHolder{


        //aqui poderia ser varios tipos de dados (Ex: ImageView etc...)
        TextView codcli;
        TextView cliente;
        TextView numtransvenda;
        TextView chavenfe;
        TextView numnota;
        TextView prest;
        TextView valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            codcli = itemView.findViewById(R.id.codCliId);
            cliente = itemView.findViewById(R.id.clienteId);
            numtransvenda = itemView.findViewById(R.id.numTransvendaId);

            chavenfe = itemView.findViewById(R.id.chaveNfeId);
            numnota = itemView.findViewById(R.id.numNotaId);
            prest = itemView.findViewById(R.id.prestId);
            valor = itemView.findViewById(R.id.valorId);
        }
    }


}
