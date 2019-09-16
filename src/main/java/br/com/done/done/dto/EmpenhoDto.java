package br.com.done.done.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmpenhoDto {

    private String dataEmissao;
    private String vaDocumento;
    private String coCredor;
    private String tipoEmpenho;
    private String programa;
    private String funcao;
    private String subFuncao;
    private String naturezaDespesa;
    private String fonte;
    private String licitacao;
    private String referenciaLicitacao;
    private String nuProcesso;
    private String descricaoEmpenho;
    private String nuEmpenho;
    private String ano;
    private String orgao;
    private List<NeReforcoAnulacaoDto> anuladaReforcada;

}
