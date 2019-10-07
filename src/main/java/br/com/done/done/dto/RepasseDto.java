package br.com.done.done.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepasseDto {

    private Long municipio;

    private String mes;

    private String ano;

    private List<IcmsDto> imcs;

    private List<IpiDto> ipis;

    private List<IpvaDto> ipvas;

    private List<RoyaltyDto> royalties;
}
