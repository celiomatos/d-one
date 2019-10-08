package br.com.done.done.service;

import br.com.done.done.model.Fundo;
import br.com.done.done.model.Municipio;
import br.com.done.done.model.RepasseFederal;
import br.com.done.done.repository.FundoRepository;
import br.com.done.done.repository.RepasseFederalRepository;
import br.com.done.done.util.DateUtil;
import br.com.done.done.util.NumberUtil;
import br.com.done.done.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RepasseFederalService {

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private RepasseFederalSiteService repasseFederalSiteService;

    @Autowired
    private FundoRepository fundoRepository;

    @Autowired
    private RepasseFederalRepository repasseFederalRepository;

    private final Map<String, Long> mpfundo = new HashMap<>();


    public void updateBySchedule() {

        String mesAnoInicial = parametroService.getParametroRepasseFederal();
        String diaInicial = "01/" + mesAnoInicial;
        int mes = Integer.parseInt(mesAnoInicial.substring(0, 2));
        int ano = Integer.parseInt(mesAnoInicial.substring(3));
        String diaFinal = DateUtil.lastDayMonth((mes + 1), ano);

        List<String[]> result = repasseFederalSiteService.getRepasse(diaInicial, diaFinal);

        if (!result.isEmpty()) {
            List<RepasseFederal> p = repasseFederalRepository.findByDiaBetweenOrderByMunicipioIdAscFundoIdAsc(
                    DateUtil.strToDate(diaInicial), DateUtil.strToDate(diaFinal));

            for (String[] s : result) {

                StringBuilder chave1 = new StringBuilder();
                chave1.append(s[0]);
                chave1.append(s[1]);
                chave1.append(s[2]);
                chave1.append(s[3]);
                chave1.append(s[4]);
                chave1.append(NumberUtil.strToBigDecimal(s[5]));
                chave1.append(s[6]);
                boolean achou = false;
                log.info(chave1.toString());

                for (int i = 0; i < p.size(); i++) {

                    StringBuilder chave2 = new StringBuilder();
                    chave2.append(p.get(i).getMunicipio().getId());
                    chave2.append(p.get(i).getFundo().getSigla());
                    chave2.append(p.get(i).getFundo().getNome());
                    chave2.append(DateUtil.formatDate(p.get(i).getDia()));
                    chave2.append(p.get(i).getParcela());
                    chave2.append(p.get(i).getValor());
                    chave2.append(p.get(i).getTipo());

                    if (chave1.toString().equalsIgnoreCase(chave2.toString())) {
                        achou = true;
                        p.remove(i);
                        break;
                    }
                }

                if (!achou) {
                    Long idfundo = getFundo(s[2], s[1]);
                    if (idfundo != null) {

                        RepasseFederal rf = new RepasseFederal();
                        rf.setMunicipio(new Municipio(Long.parseLong(s[0])));
                        rf.setFundo(new Fundo(idfundo));
                        rf.setDia(DateUtil.strToDate(s[3]));
                        rf.setParcela(s[4]);
                        rf.setValor(NumberUtil.strToBigDecimal(s[5]));
                        rf.setTipo(s[6]);
                        try {
                            log.info("Criando...");
                            repasseFederalRepository.save(rf);
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    }
                }
            }
        }

        log.info("Finish Repasse Federal Schedule " + new Date());
    }

    private Long getFundo(String nome, String sigla) {
        if (mpfundo.size() > 20) {
            mpfundo.clear();
        }
        Long idfundo = mpfundo.get(StringUtil.removerAcento(nome));
        if (idfundo == null) {
            try {
                Fundo fundo;
                Optional<Fundo> f = fundoRepository.findByNomeIgnoreCase(nome);
                if (f.isPresent()) {
                    fundo = f.get();
                } else {
                    fundo = new Fundo();
                    fundo.setNome(StringUtil.inTrim(nome));
                    fundo.setSigla(StringUtil.inTrim(sigla));
                    fundo = fundoRepository.save(fundo);
                }
                if (fundo != null) {
                    idfundo = fundo.getId();
                    mpfundo.put(StringUtil.removerAcento(nome), idfundo);
                }

            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return idfundo;
    }
}
