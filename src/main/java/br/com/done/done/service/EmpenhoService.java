package br.com.done.done.service;

import br.com.done.done.dto.EmpenhoDto;
import br.com.done.done.dto.NeReforcoAnulacaoDto;
import br.com.done.done.model.Credor;
import br.com.done.done.model.Empenho;
import br.com.done.done.model.Fonte;
import br.com.done.done.model.Orgao;
import br.com.done.done.repository.EmpenhoRepository;
import br.com.done.done.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmpenhoService {

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private EmpenhoSiteService empenhoSiteService;

    @Autowired
    private OrgaoService orgaoService;

    @Autowired
    private CredorService credorService;

    @Autowired
    private FonteService fonteService;

    @Autowired
    private EmpenhoRepository empenhoRepository;

    @Autowired
    private EmpenhoDespesaService empenhoDespesaService;

    @Autowired
    private EmpenhoOriginalService empenhoOriginalService;

    /**
     * @param pr
     */
    public void updateBySchedule(long pr) {
        log.info("Rodando Atualizacao do empho atual");
        String p = parametroService.getParametroEmpenho(pr);
        if (p != null) {
            String ano = p.substring(0, 4);
            String orgao = p.substring(4);
            List<EmpenhoDto> lst = empenhoSiteService.getNotasEmpenho(ano, orgao, pr);
            if (!lst.isEmpty()) {
                persistEmpenho(lst);
            }
        }
    }

    /**
     * @param nes
     */
    private void persistEmpenho(List<EmpenhoDto> nes) {
        // verificando a existencia do orgao
        Optional<Orgao> optOrgao = orgaoService.findByCodigo(nes.get(0).getOrgao());

        if (optOrgao.isPresent()) {
            for (EmpenhoDto ne : nes) {

                Empenho empenho = getEmpenho(ne, optOrgao.get());

                if (empenho.getId() != 0L) {
                    empenhoDespesaService.save(ne, empenho);

                    List<NeReforcoAnulacaoDto> list = ne.getAnuladaReforcada();

                    if (list != null && !list.isEmpty()) {
                        empenhoOriginalService.setReforcoAnulacao(list, empenho, optOrgao.get());
                    }
                }
            }
        }
    }

    /**
     * @param ne
     * @param o
     * @return
     */
    private Empenho getEmpenho(EmpenhoDto ne, Orgao o) {

        Empenho empenho = null;

        // credores
        Credor credor = credorService.getOrCreateCredor(ne.getCoCredor());
        if (credor != null) {
            // fontes
            Fonte fonte = fonteService.getOrCreateFonte(ne.getFonte());
            if (fonte != null) {
                Optional<Empenho> optEmpenho = findByNotaAndOrgaoId(ne.getNuEmpenho(), o.getId());

                empenho = optEmpenho.orElseGet(Empenho::new);
                empenho.setData(DateUtil.strToDate(ne.getDataEmissao()));
                empenho.setDescricao(ne.getDescricaoEmpenho());
                empenho.setFuncao(ne.getFuncao());
                empenho.setLicitacao(ne.getLicitacao());
                empenho.setNota(ne.getNuEmpenho());
                empenho.setProcesso(ne.getNuProcesso());
                empenho.setPrograma(ne.getPrograma());
                empenho.setReferencia(ne.getReferenciaLicitacao());
                empenho.setSubFuncao(ne.getSubFuncao());
                empenho.setTipo(ne.getTipoEmpenho());
                empenho.setOrgao(o);
                empenho.setCredor(credor);
                empenho.setFonte(fonte);
                if (empenho.getId() == 0L) {
                    empenho.setLancamento(new Date());
                }
                return empenhoRepository.save(empenho);
            }
        }

        return empenho;
    }

    /**
     * @param nota
     * @param idOrgao
     * @return
     */
    public Optional<Empenho> findByNotaAndOrgaoId(String nota, long idOrgao) {
        return empenhoRepository.findByNotaAndOrgaoId(nota, idOrgao);
    }
}
