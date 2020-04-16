package com.dev.deijai.soluesstolio.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {


    public static String Dbname = "applio.db";
    public static int versao = 2;

    public Db(Context ctx) {
        super(ctx, Dbname, null, versao);
    }


    private static String SQL_PAGAMENTO = "CREATE TABLE [PAGAMENTOS] (" +
            "pg_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "pg_numtransvenda VARCHAR DEFAULT 50," +
            "pg_codcli VARCHAR DEFAULT 50," +
            "pg_cliente VARCHAR DEFAULT 50 ," +
            "pg_chavenfe VARCHAR DEFAULT 200," +
            "pg_numnota VARCHAR DEFAULT 50," +
            "pg_prest VARCHAR DEFAULT 30," +
            "pg_valor  VARCHAR DEFAULT 20," +
            "pg_group_nota VARCHAR DEFAULT 100 );";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_PAGAMENTO);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int VersaoAntiga, int VersaoNova) {

        // if(VersaoAntiga == 2 && VersaoNova == 3 ){

        // codigo para atualizar qualquer tabela

        // }

    }


}
