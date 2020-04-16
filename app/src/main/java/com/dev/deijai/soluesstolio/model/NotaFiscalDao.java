package com.dev.deijai.soluesstolio.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.dev.deijai.soluesstolio.util.Util;

import java.util.ArrayList;
import java.util.List;

public class NotaFiscalDao {

    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String valorTotal;

    public NotaFiscalDao(Context ctx) {
        this.ctx = ctx;
    }


    //Gravar pagamento
    public boolean gravarPagamento(NotaFiscalBean notaFiscal) {


        try {

            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            gravacao = false;

            sql = "INSERT INTO PAGAMENTOS (pg_numtransvenda, pg_codcli, pg_cliente, pg_chavenfe, pg_numnota, pg_prest, pg_valor, pg_group_nota) VALUES(?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = db.compileStatement(sql);

            stmt.bindString(1, notaFiscal.getNUMTRANSVENDA());
            stmt.bindLong(2, notaFiscal.getCODCLI());
            stmt.bindString(3, notaFiscal.getCLIENTE());
            stmt.bindString(4, notaFiscal.getCHAVENFE());
            stmt.bindString(5, notaFiscal.getNUMNOTA());
            stmt.bindLong(6, notaFiscal.getPREST());
            stmt.bindString(7, notaFiscal.getVALOR());
            stmt.bindString(8, "TT99");

            if (stmt.executeInsert() > 0) {
                gravacao = true;
                sql = "";
            }

        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
            gravacao = false;
        }


        return gravacao;
    }

    public boolean buscarNota(String numTransvenda) {
        db = new Db(ctx).getReadableDatabase();
        NotaFiscalBean notas = null;
        gravacao = false;
        try {
            cursor = db.rawQuery("SELECT * FROM PAGAMENTOS WHERE pg_numtransvenda = ? ", new String[]{numTransvenda});
            if (cursor.moveToFirst()) {
                gravacao = true;
                }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar ao buscar nota pela transação" + e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return gravacao;
    }


    public List<NotaFiscalBean> lista (){
        List<NotaFiscalBean> n = new ArrayList<NotaFiscalBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from PAGAMENTOS", null);
            while (cursor.moveToNext()) {
                NotaFiscalBean notas = new NotaFiscalBean();
                notas.setNUMTRANSVENDA(cursor.getString(1));
                notas.setCODCLI(cursor.getInt(cursor.getColumnIndex("pg_codcli")));
                notas.setCLIENTE(cursor.getString(3));
                notas.setCHAVENFE(cursor.getString(4));
                notas.setNUMNOTA(cursor.getString(5));
                notas.setPREST(cursor.getInt(cursor.getColumnIndex("pg_prest")));
                notas.setVALOR(cursor.getString(7));
                notas.setGROUP_NOTA(cursor.getString(8));

                n.add(notas);
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_clientes_nao_enviados" + e.getMessage());
        } finally {
            db.close();
        }
        return n;
    }

    public String sumValorTotal(){

        db = new Db(ctx).getReadableDatabase();

        try {
            cursor = db.rawQuery("SELECT sum(pg_valor) FROM PAGAMENTOS",null);
            if (cursor.moveToFirst()) {
                valorTotal = cursor.getString(0);
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException somar valores" + e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return valorTotal;
    }


    public boolean DeletarDados() {
        db = new Db(ctx).getReadableDatabase();
        gravacao = false;
        try {
            cursor = db.rawQuery("DELETE FROM PAGAMENTOS ", null);
            if (cursor.moveToFirst()) {
                gravacao = true;
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar ao buscar nota pela transação" + e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return gravacao;
    }







}

