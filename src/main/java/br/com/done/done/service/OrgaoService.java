package br.com.done.done.service;

import br.com.done.done.dto.OrgaoValorDto;
import br.com.done.done.dto.TopFiveOrgaosDto;
import br.com.done.done.model.Orgao;
import br.com.done.done.repository.OrgaoRepository;
import br.com.done.done.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class OrgaoService {

    @Autowired
    private OrgaoRepository orgaoRepository;

    /**
     * @param dtInicial
     * @param dtFinal
     * @return
     */
    public List<OrgaoValorDto> getOrgaoValueByDate(Date dtInicial, Date dtFinal) {

        List<Object[]> lst = orgaoRepository.getOrgaoValueByDate(
                DateUtil.utilDateToSqlDate(dtInicial),
                DateUtil.utilDateToSqlDate(dtFinal));

        List<OrgaoValorDto> result = new LinkedList<>();

        lst.stream().map((obj) -> {
            OrgaoValorDto bean = new OrgaoValorDto();
            bean.setCodigo(obj[0].toString());
            bean.setNome(obj[1].toString());
            bean.setValor(obj[2].toString());
            return bean;
        }).forEachOrdered((bean) -> {
            result.add(bean);
        });
        return result;
    }

    public Optional<Orgao> findByCodigo(String codigo) {
        return orgaoRepository.findByCodigo(codigo);
    }

    public Orgao save(Orgao orgao) {
        return orgaoRepository.save(orgao);
    }

    public List<TopFiveOrgaosDto> topFive(String dateInicial, String dateFinal) {
        List<Object[]> result = orgaoRepository.findTopFiveOrgaos(
                new java.sql.Date(Long.parseLong(dateInicial)), new java.sql.Date(Long.parseLong(dateFinal)));

        List<TopFiveOrgaosDto> fiveOrgaos = new ArrayList<>();
        for (Object[] obj : result) {
            fiveOrgaos.add(new TopFiveOrgaosDto(
                    obj[0] != null ? obj[0].toString() : "ORGAO0001",
                    obj[1] != null ? obj[1].toString() : "X",
                    new BigDecimal(obj[2].toString())));
        }
        return fiveOrgaos;
    }

    /**
     * @return
     */
    public Page<Orgao> findAll(Pageable pageable) {
        return orgaoRepository.findAll(pageable);
    }

    /**
     * @param nome
     * @param pageable
     * @return
     */
    public Page<Orgao> findByNome(String nome, Pageable pageable) {
        return orgaoRepository.findByNomeIgnoreCaseContainingOrderByNomeAsc(nome.trim(), pageable);
    }
}
