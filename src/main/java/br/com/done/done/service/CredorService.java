package br.com.done.done.service;

import br.com.done.done.dto.TopFiveCredoresDto;
import br.com.done.done.model.Credor;
import br.com.done.done.repository.CredorRepository;
import br.com.done.done.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CredorService {

    @Autowired
    private CredorRepository credorRepository;

    public Optional<Credor> findByNome(String nome) {
        return credorRepository.findByNome(nome);
    }

    public Credor save(Credor credor) {
        return credorRepository.save(credor);
    }

    public Optional<Credor> findByCodigo(String codigo) {
        return credorRepository.findByCodigo(codigo);
    }

    public Credor getOrCreateCredor(String strCredor) {

        Credor credor = null;
        String vtcredor[] = strCredor.split("[-]");

        if (vtcredor.length > 0) {

            String codigo = vtcredor[0].trim();

            Optional<Credor> optCodigo = findByCodigo(codigo);

            if (optCodigo.isPresent()) {
                credor = optCodigo.get();
            } else if (vtcredor.length > 1) {
                String nome = StringUtil.removerAcento(vtcredor[1]).toUpperCase();

                Optional<Credor> optNome = findByNome(nome);

                credor = optNome.orElseGet(Credor::new);
                credor.setCodigo(codigo);
                credor.setNome(nome);

                credor = save(credor);
            }
        }
        return credor;
    }

    public List<TopFiveCredoresDto> topFive(String dateInicial, String dateFinal) {
        List<Object[]> result = credorRepository.findTopFiveCredores(
                new java.sql.Date(Long.parseLong(dateInicial)), new java.sql.Date(Long.parseLong(dateFinal)));

        List<TopFiveCredoresDto> fiveCredores = new ArrayList<>();
        for (Object[] obj : result) {
            fiveCredores.add(new TopFiveCredoresDto(obj[0].toString(), new BigDecimal(obj[1].toString())));
        }
        return fiveCredores;
    }

    /**
     * @param pageable
     * @return
     */
    public Page<Credor> findAll(Pageable pageable) {
        return credorRepository.findAll(pageable);
    }

    /**
     * @param nome
     * @param pageable
     * @return
     */
    public Page<Credor> findByNome(String nome, Pageable pageable) {
        nome = nome.trim().toUpperCase();
        Page<Credor> page;
        if (nome.contains(",")) {
            String[] credores = nome.split(",");
            if (credores.length > 1) {
                page = credorRepository.findByNomeContainingAndNomeContainingOrderByNomeAsc(credores[0].trim(), credores[1].trim(), pageable);
            } else {
                page = credorRepository.findByNomeContainingOrderByNomeAsc(credores[0].trim(), pageable);
            }
        } else {
            page = credorRepository.findByNomeStartingWithOrderByNomeAsc(nome, pageable);
        }
        return page;
    }
}
