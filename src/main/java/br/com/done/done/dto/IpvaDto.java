package br.com.done.done.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpvaDto {

    private String pInicial;

    private String pFinal;

    private String vlRepasse;
}
