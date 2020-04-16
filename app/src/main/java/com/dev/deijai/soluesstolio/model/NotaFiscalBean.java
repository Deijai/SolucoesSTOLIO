package com.dev.deijai.soluesstolio.model;

public class NotaFiscalBean {


    public static final String C_CODCLI = "CODCLI";
    public static final String C_NUMTRANSVENDA = "NUMTRANSVENDA";
    public static final String C_CLIENTE = "CLIENTE";
    public static final String C_CHAVENFE = "CHAVENFE";
    public static final String C_NUMNOTA = "NUMNOTA";
    public static final String C_PREST = "PREST";
    public static final String C_VALOR = "VALOR";
    public static final String C_GROUP_NOTA = "GROUP_NOTA";


    private int CODCLI;
    private String NUMTRANSVENDA;
    private String CLIENTE;
    private String CHAVENFE;
    private String NUMNOTA;
    private Integer PREST;
    private String VALOR;
    private String GROUP_NOTA;



    public int getCODCLI() {
        return CODCLI;
    }

    public void setCODCLI(int CODCLI) {
        this.CODCLI = CODCLI;
    }

    public String getNUMTRANSVENDA() {
        return NUMTRANSVENDA;
    }

    public void setNUMTRANSVENDA(String NUMTRANSVENDA) {
        this.NUMTRANSVENDA = NUMTRANSVENDA;
    }

    public String getCLIENTE() {
        return CLIENTE;
    }

    public void setCLIENTE(String CLIENTE) {
        this.CLIENTE = CLIENTE;
    }

    public String getCHAVENFE() {
        return CHAVENFE;
    }

    public void setCHAVENFE(String CHAVENFE) {
        this.CHAVENFE = CHAVENFE;
    }

    public String getNUMNOTA() {
        return NUMNOTA;
    }

    public void setNUMNOTA(String NUMNOTA) {
        this.NUMNOTA = NUMNOTA;
    }

    public Integer getPREST() {
        return PREST;
    }

    public void setPREST(Integer PREST) {
        this.PREST = PREST;
    }

    public String getVALOR() {
        return VALOR;
    }

    public void setVALOR(String VALOR) {
        this.VALOR = VALOR;
    }

    public String getGROUP_NOTA() {
        return GROUP_NOTA;
    }

    public void setGROUP_NOTA(String GROUP_NOTA) {
        this.GROUP_NOTA = GROUP_NOTA;
    }

    @Override
    public String toString() {
        return "NotaFiscalBean{" +
                "CODCLI=" + CODCLI +
                ", NUMTRANSVENDA='" + NUMTRANSVENDA + '\'' +
                ", CLIENTE='" + CLIENTE + '\'' +
                ", CHAVENFE='" + CHAVENFE + '\'' +
                ", NUMNOTA='" + NUMNOTA + '\'' +
                ", PREST=" + PREST +
                ", VALOR='" + VALOR + '\'' +
                ", GROUP_NOTA='" + GROUP_NOTA + '\'' +
                '}';
    }
}
