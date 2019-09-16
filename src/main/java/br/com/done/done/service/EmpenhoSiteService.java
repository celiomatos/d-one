package br.com.done.done.service;

import br.com.done.done.dto.EmpenhoDto;
import br.com.done.done.dto.NeReforcoAnulacaoDto;
import br.com.done.done.dto.OrgaoDto;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Service
public class EmpenhoSiteService {

    @Autowired
    private ParametroService parametroService;

    /**
     * @param ano
     * @return
     */
    public List<OrgaoDto> getOrgaos(int ano) {

        StringBuilder url = new StringBuilder();
        url.append("http://sistemas.sefaz.am.gov.br/transpprd/mnt/despesa/");
        url.append("execDespAnoPoder.do?method=");
        url.append("Pesquisar&copoder=0&anoexercicio=");
        url.append(ano);
        url.append("&grupo=1&consulta=1&mes=00&detNatureza=N");

        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);

        List<OrgaoDto> lst = new ArrayList<>();

        try (WebClient wc = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            HtmlPage page = null;
            try {
                page = wc.getPage(url.toString());
            } catch (IOException | FailingHttpStatusCodeException ex) {
                log.error(ex.getMessage());
            }
            if (page != null) {
                HtmlTable table = page.getHtmlElementById("item");
                if (table != null) {
                    DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                    for (int i = 2; i < rows.size(); i++) {
                        DomNodeList<HtmlElement> col = rows.get(i).getElementsByTagName("td");

                        String c = col.get(0).getElementsByTagName("a").get(0).getAttribute("href");
                        c = c.replaceAll("[javascript:showUg(document.getElementById('anoexercicio').value,']", "")
                                .replaceAll("[',)]", "");

                        OrgaoDto b = new OrgaoDto();
                        b.setCodigo(c.substring(0, 6));
                        b.setSigla(col.get(0).asText().trim());
                        b.setNome(col.get(0).getElementsByTagName("a").get(0).getAttribute("title"));
                        lst.add(b);
                    }
                }
            }
        }
        return lst;
    }

    /**
     * @param ano
     * @param orgao
     * @return
     */
    public List<EmpenhoDto> getNotasEmpenho(String ano, String orgao, long pr) {

        StringBuilder url = new StringBuilder();
        url.append("http://sistemas.sefaz.am.gov.br/transpprd/mnt/despesa/");
        url.append("execDespAnoPoderUg.do?method=Pesquisar&counidadegestora=");
        url.append(orgao);
        url.append("&anoexercicio=");
        url.append(ano);
        url.append("&copoder=0&grupo=1&consulta=1&mes=00");

        List<EmpenhoDto> result = new ArrayList<>();

        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);

        // posicao do empenho na tabela
        int posicao = parametroService.getIndexEmpenho(pr);
        int nova = 2;

        int idx = posicao;
        // total de notas de empenho
        int tam = 5;
        // index da tabela dos reforcos
        int idxItems = 1;
        // decide se vai para outra nota de empenho
        // se houver notas de reforco fica falso ate o termino dos reforcos
        boolean loop = true;

        EmpenhoDto bean = null;
        List<NeReforcoAnulacaoDto> list = new LinkedList<>();

        do {
            try (WebClient wc = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
                wc.getOptions().setJavaScriptEnabled(true);

                HtmlPage page = null;
                try {
                    page = wc.getPage(url.toString());
                    log.info(url.toString());
                } catch (Exception ex) {
                    idxItems = 1;
                    loop = true;
                    log.error(ex.getMessage());
                }
                if (page == null) {
                    idxItems = 1;
                    loop = true;
                } else {
                    //tabela de ne

                    DomElement tab = null;
                    try {
                        tab = page.getElementById("item");
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    if (tab != null) {

                        DomNodeList<HtmlElement> rows = tab.getElementsByTagName("tr");
                        HtmlElement row = rows.get(idx);

                        if (rows.size() - posicao > 100) {
                            tam = posicao + 100;
                            nova = tam;
                        } else {
                            tam = rows.size();
                        }

                        DomNodeList<HtmlElement> cols = row.getElementsByTagName("td");
                        String ne = cols.get(0).asText();
                        log.info(idx + " de " + tam + " total " + rows.size() + " empenho " + ne);
                        HtmlAnchor neLink = page.getAnchorByText(ne);

                        HtmlPage detalhe = null;
                        try {
                            detalhe = neLink.click();
                        } catch (Exception ex) {
                            idxItems = 1;
                            loop = true;
                            log.error(ex.getMessage());
                        }

                        DomElement negativa = null;
                        try {
                            negativa = page.getElementById("negativa");
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                        if (detalhe == null || negativa != null) {
                            idxItems = 1;
                            loop = true;
                        } else {
                            // se houver reforco e anulacao havera necessidade de voltar na ne
                            if (loop) {
                                bean = setBean(detalhe, ne, orgao, ano);
                            }

                            // tabela reforco anulacao
                            DomElement ra = null;
                            try {
                                ra = detalhe.getElementById("item");
                            } catch (Exception ex) {
                                log.error(ex.getMessage());
                            }
                            if (ra != null) {

                                DomNodeList<HtmlElement> rra = ra.getElementsByTagName("tr");

                                DomNodeList<HtmlElement> cra = rra.get(idxItems).getElementsByTagName("td");

                                NeReforcoAnulacaoDto beanReforco = new NeReforcoAnulacaoDto();
                                String neReforco = cra.get(0).asText();
                                log.info("reforco {}", neReforco);
                                beanReforco.setNeReforcadaAnulada(neReforco);
                                beanReforco.setEvento(cra.get(1).asText());
                                beanReforco.setValor(cra.get(2).asText());
                                beanReforco.setDescricao(cra.get(3).asText());

                                list.add(beanReforco);

                                HtmlAnchor neReforcoLink = detalhe.getAnchorByText(neReforco);

                                HtmlPage pageReforco = null;
                                try {
                                    pageReforco = neReforcoLink.click();
                                } catch (Exception ex) {
                                    log.error(ex.getMessage());
                                }

                                if (pageReforco != null && pageReforco.isDisplayed()) {
                                    result.add(setBean(pageReforco, neReforco, orgao, ano));
                                }

                                idxItems++;

                                if (idxItems >= rra.size() - 1) {
                                    idxItems = 1;
                                    bean.setAnuladaReforcada(list);
                                    list = new LinkedList<>();
                                    loop = true;
                                } else {
                                    loop = false;
                                }
                            }
                            if (loop) {
                                result.add(bean);
                            }
                        }
                    }
                }
                if (loop) {
                    idx++;
                }
            }
        } while (idx < tam);
        parametroService.setIndexEmpenho(pr, nova);

        return result;
    }

    /**
     * @param page
     * @param ne
     * @return
     */
    private EmpenhoDto setBean(HtmlPage page, String ne, String orgao, String ano) {

        EmpenhoDto bean = new EmpenhoDto();
        bean.setOrgao(orgao);
        bean.setAno(ano);
        bean.setNuEmpenho(ne);

        HtmlElement dataEmissao = page.getElementByName("dataEmissao");
        bean.setDataEmissao(dataEmissao.getAttribute("value"));

        HtmlElement vaDocumento = page.getElementByName("vadocumento");
        bean.setVaDocumento(vaDocumento.getAttribute("value"));

        HtmlElement coCredor = page.getElementByName("cocredor");
        bean.setCoCredor(coCredor.getAttribute("value"));

        HtmlElement tipoEmpenho = page.getElementByName("tipoempenho");
        bean.setTipoEmpenho(tipoEmpenho.getAttribute("value"));

        HtmlElement pT = page.getElementByName("pt");
        bean.setPrograma(pT.getAttribute("value"));

        HtmlElement funcao = page.getElementByName("funcao");
        bean.setFuncao(funcao.getAttribute("value"));

        HtmlElement subFuncao = page.getElementByName("subfuncao");
        bean.setSubFuncao(subFuncao.getAttribute("value"));

        HtmlElement naturezaDespesa = page.getElementByName("naturezadespesa");
        bean.setNaturezaDespesa(naturezaDespesa.getAttribute("value"));

        HtmlElement fonte = page.getElementByName("fonte");
        bean.setFonte(fonte.getAttribute("value"));

        HtmlElement licitacao = page.getElementByName("licitacao");
        bean.setLicitacao(licitacao.getAttribute("value"));

        HtmlElement referenciaLicitacao = page.getElementByName("referencialicitacao");
        bean.setReferenciaLicitacao(referenciaLicitacao.getAttribute("value"));

        HtmlElement nuProcesso = page.getElementByName("nuprocesso");
        bean.setNuProcesso(nuProcesso.getAttribute("value"));

        HtmlElement descricaoEmpenho = page.getElementByName("descricaoEmpenho");
        bean.setDescricaoEmpenho(descricaoEmpenho.asText());

        return bean;
    }
}
