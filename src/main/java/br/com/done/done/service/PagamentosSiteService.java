package br.com.done.done.service;

import br.com.done.done.dto.OrgaoValorDto;
import br.com.done.done.dto.PagamentoDto;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Service
public class PagamentosSiteService {

    /**
     * @param mes
     * @param ano
     * @return
     */
    public List<OrgaoValorDto> getOrgaoValue(String mes, String ano) {

        List<OrgaoValorDto> lst = new LinkedList<>();

        StringBuilder url = new StringBuilder();
        url.append("http://sistemas.sefaz.am.gov.br/transpprd/mnt/info/");
        url.append("RelPagamentos.do?method=Pesquisar&anoexercicio=");
        url.append(ano);
        url.append("&mes=");
        url.append(mes);

        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);

        log.info("Pagamentos: {}", url.toString());

        try (WebClient wc = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            HtmlPage page = null;

            try {
                page = wc.getPage(url.toString());
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

            if (page != null) {

                HtmlTable table = null;
                try {
                    table = page.getHtmlElementById("item");
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                if (table != null) {
                    DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                    for (int i = 2; i < rows.size(); i++) {
                        DomNodeList<HtmlElement> col = rows.get(i).getElementsByTagName("td");

                        String c = col.get(0).getElementsByTagName("a").get(0).getAttribute("href");
                        c = c.replaceAll("[javascript:showOrgao(']", "")
                                .replaceAll("[')]", "");

                        OrgaoValorDto b = new OrgaoValorDto();
                        b.setCodigo(c);
                        b.setOrgao(col.get(0).asText().trim());
                        b.setValor(col.get(1).asText().trim());

                        log.info("Orgao: {} valor: {}", b.getOrgao(), b.getValor());

                        lst.add(b);
                    }
                }
            }
        }
        return lst;
    }

    /**
     * @param orgaos
     * @param mes
     * @param ano
     * @return
     */
    public List<OrgaoValorDto> getPagamentos(List<OrgaoValorDto> orgaos, String mes, String ano) {

        StringBuilder url = new StringBuilder();
        url.append("http://sistemas.sefaz.am.gov.br/transpprd/mnt/info/");
        url.append("RelPagamentosOrgao.do?method=Pesquisar&anoexercicio=");
        url.append(ano);
        url.append("&mes=");
        url.append(mes);
        url.append("&counidadegestora=");

        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);

        for (OrgaoValorDto orgao : orgaos) {

            try (WebClient wc = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
                // filtrando por orgao
                HtmlPage page = null;
                log.info(url.toString() + orgao.getCodigo());
                try {
                    page = wc.getPage(url.toString() + orgao.getCodigo());
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

                if (page != null) {
                    HtmlTable table = null;
                    try {
                        table = page.getHtmlElementById("item");
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    if (table != null) {
                        // todos os pagamentos do orgao
                        DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                        List<PagamentoDto> pagamentos = new ArrayList<>();
                        for (int i = 2; i < rows.size(); i++) {

                            DomNodeList<HtmlElement> col = rows.get(i).getElementsByTagName("td");

                            PagamentoDto bean = new PagamentoDto();
                            bean.setCredor(col.get(0).asText().trim());
                            bean.setData(col.get(1).asText().trim());
                            bean.setNrOb(col.get(2).asText().trim());
                            bean.setNrNl(col.get(3).asText().trim());
                            bean.setNrNe(col.get(4).asText().trim());
                            bean.setFonte(col.get(5).asText().trim());
                            bean.setClassificacao(col.get(6).asText().trim());
                            bean.setValor(col.get(7).asText().trim());
                            pagamentos.add(bean);
                        }
                        // obtendo o nome do orgao
                        DomNodeList<DomElement> lst = page.getElementsByTagName("fieldset");
                        String nome[] = lst.get(1).asText().split("-");
                        orgao.setNome(nome[1].trim());

                        orgao.setPagamentos(pagamentos);
                    }
                }
            }
        }
        return orgaos;
    }

}
