package br.com.done.done.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoDto {

    private String credor;
    private String data;
    private String nrOb;
    private String nrNl;
    private String nrNe;
    private String fonte;
    private String classificacao;
    private String valor;
}
