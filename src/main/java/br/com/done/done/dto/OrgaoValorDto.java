package br.com.done.done.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrgaoValorDto {

    private String codigo;
    private String orgao;
    private String nome;
    private String valor;
    private List<PagamentoDto> pagamentos;
}
