package br.com.done.done.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopFiveOrgaosDto {

    private String sigla;

    private String orgao;

    private BigDecimal total;
}
