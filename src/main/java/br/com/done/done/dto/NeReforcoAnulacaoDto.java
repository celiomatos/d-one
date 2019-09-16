package br.com.done.done.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeReforcoAnulacaoDto {

    private String neOriginal;
    private String neReforcadaAnulada;
    private String evento;
    private String valor;
    private String descricao;
}
