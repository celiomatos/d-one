package br.com.done.done.service;

import br.com.done.done.dto.OrgaoDto;
import br.com.done.done.model.Orgao;
import br.com.done.done.model.Parametro;
import br.com.done.done.repository.ParametroRepository;
import br.com.done.done.util.MyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ParametroService {

    @Autowired
    private ParametroRepository parametroRepository;

    @Autowired
    private EmpenhoSiteService empenhoSiteService;

    @Autowired
    private OrgaoService orgaoService;

    /**
     * Retorna o valor para ser usado como parametro como mes e ano de pesquisa de pagamento.
     *
     * @return
     */
    public String getParametroPagamento() {

        String result = null;

        Optional<Parametro> p = parametroRepository.findById(MyConstant.PAGAMENTO);

        if (p.isPresent()) {
            result = getMesAno(p.get(), 1);
        }
        return result;
    }


    /**
     * Retorna o valor para ser usado como mes e ano da pesquisa de pagamentos e add usada pra incrementar mes.
     *
     * @param parametro
     * @param add
     * @return
     */
    private String getMesAno(Parametro parametro, int add) {

        String result = parametro.getAtual();

        String nv[] = result.split("/");
        int mes = Integer.parseInt(nv[0]) - 1;
        int ano = Integer.parseInt(nv[1]);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + add);

        if (c.after(Calendar.getInstance())) {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.YEAR, (ano - 7));
            parametro.setAtual(sdf.format(c.getTime()));
        } else {
            parametro.setAtual(sdf.format(c.getTime()));
        }

        parametroRepository.save(parametro);

        return result;
    }

    /**
     * Retorna valor para usado como ano de pesquisa e orgaos .
     *
     * @param pr
     * @return
     */
    public String getParametroEmpenho(long pr) {
        String result = null;
        Optional<Parametro> p = parametroRepository.findById(pr);

        if (p.isPresent()) {
            // dados atuais
            String paramentros = p.get().getAtual();
            // separando ano e orgaos
            String dados[] = paramentros.split("[-]");

            int ano = Integer.parseInt(dados[0].trim());
            String orgao = "";

            if (dados.length == 3) {
                String orgaos[] = dados[2].split(";");
                orgao = orgaos[0].trim();
            } else {
                // coleta todos os orgao listados no site para setar em parametros
                Calendar cal = Calendar.getInstance();
                int anoAtual = cal.get(Calendar.YEAR);
                if (pr == MyConstant.EMPENHO_ANOS_ANTERIORES) {
                    if (ano < (anoAtual - 1)) {
                        ano = ano + 1;
                    } else {
                        ano = ano - 6;
                    }
                } else {
                    ano = anoAtual;
                }
                // obtendo a lista de orgaos para atualizar os paramentros
                List<OrgaoDto> orgaos = empenhoSiteService.getOrgaos(ano);
                if (!orgaos.isEmpty()) {
                    // atualiza ou cria os orgaos
                    updateOrgaos(orgaos);
                    // remove o primeiro para enviar para pesquisa
                    orgao = orgaos.get(0).getCodigo();
                    orgaos.remove(0);

                    StringBuilder o = new StringBuilder();
                    int tam = orgaos.size();
                    for (int i = 0; i < tam; i++) {
                        o.append(orgaos.get(i).getCodigo());
                        if (i < (tam - 1)) {
                            o.append(";");
                        }
                    }

                    paramentros = ano + "-2-" + o.toString();
                }
                p.get().setAtual(paramentros);
                parametroRepository.save(p.get());
            }

            result = ano + orgao;
        }
        return result;
    }

    /**
     * @param lst
     */
    private void updateOrgaos(List<OrgaoDto> lst) {
        lst.forEach((b) -> {
            Optional<Orgao> opt = orgaoService.findByCodigo(b.getCodigo());

            Orgao o = opt.isPresent() ? opt.get() : new Orgao();

            o.setCodigo(b.getCodigo());
            o.setEsfera(MyConstant.ESFERA_ESTADO);
            o.setOrgName(b.getNome());
            o.setSigla(b.getSigla());

            if (o.getId() == 0L) {
                o.setNome(b.getNome());
            }
            orgaoService.save(o);
        });
    }

    /**
     * @param pr
     * @return
     */
    public int getIndexEmpenho(long pr) {

        int result = 2;
        Optional<Parametro> p = parametroRepository.findById(pr);
        // dados atuais
        String paramentros = p.get().getAtual();
        // separando ano e orgaos
        String dados[] = paramentros.split("[-]");
        result = Integer.parseInt(dados[1]);

        return result;
    }

    public void setIndexEmpenho(long pr, int idx) {

        Optional<Parametro> p = parametroRepository.findById(pr);


        // dados atuais
        String paramentros = p.get().getAtual();
        // separando ano e orgaos
        String d[] = paramentros.split("[-]");
        if (idx > 2) {
            p.get().setAtual(d[0] + "-" + idx + "-" + d[2]);
        } else {
            // se ainda existe orgao para coleta de notas de empenho
            paramentros = d[2];
            String orgaos[] = d[2].split(";");
            String orgao = orgaos[0].trim();
            paramentros = paramentros.replace(orgao + ";", "");
            // repetindo caso este seja o ultimo orgao
            paramentros = paramentros.replace(orgao, "");
            p.get().setAtual(d[0] + "-2-" + paramentros);
        }

        parametroRepository.save(p.get());
    }

}
