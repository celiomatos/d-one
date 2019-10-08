package br.com.done.done.service;

import br.com.done.done.dto.*;
import br.com.done.done.model.Municipio;
import br.com.done.done.repository.MunicipioRepository;
import br.com.done.done.util.StringUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Service
public class RepasseSiteService {

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private MunicipioRepository municipioRepository;


    public List<RepasseDto> acessar() {
        final int TABLE_IPVA = 5;
        final int TABLE_ICMS = 7;
        final int TABLE_ROYALTY = 10;
        final int TABLE_IPI = 12;

        List<RepasseDto> lst = new ArrayList<>();

        String mesAno = parametroService.getParametroRepasse();
        // lista de municipios para verificacao
        List<Municipio> cidades = municipioRepository.findByUfSiglaOrderByNome("AM");

        if (!cidades.isEmpty()) {
            for (Municipio cidade : cidades) {
                log.info(cidade.getNome());

                RepasseDto bean = new RepasseDto();
                bean.setMes(mesAno.substring(0, 2));
                bean.setAno(mesAno.substring(3));
                bean.setMunicipio(cidade.getId());

                // preparando o client
                Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
                Logger.getLogger("org.apache.http").setLevel(Level.OFF);

                try (WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
                    webClient.getOptions().setJavaScriptEnabled(true);
                    //url principal
                    StringBuilder url = new StringBuilder();
                    url.append("http://sistemas.sefaz.am.gov.br/srt/");
                    url.append("relatorioMensalRateioPublico.do?");
                    url.append("metodo=qbePublico&origem=publico");
                    // acessando a pagina
                    HtmlPage page = null;
                    try {
                        page = webClient.getPage(url.toString());
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    if (page != null) {
                        //obtendo o form
                        HtmlForm form = page.getHtmlElementById("relatorioMensalRateioForm");
                        if (form != null) {
                            form.getInputByName("dtMesAno").setValueAttribute(mesAno);
                            HtmlSelect municipios = (HtmlSelect) page.getElementByName("idMunicipio");
                            if (municipios != null) {
                                String municipio = StringUtil.removerAcento(cidade.getNome());
                                if (municipio.equalsIgnoreCase("SANTA ISABEL DO RIO NEGRO")) {
                                    municipio = "SANTA IZABEL DO RIO NEGRO";
                                }
                                HtmlOption option = municipios.getOptionByText(municipio.toUpperCase());
                                municipios.setSelectedAttribute(option, true);

                                try {
                                    page = form.getInputByValue("Listar").click();
                                } catch (Exception ex) {
                                    page = null;
                                    log.error(ex.getMessage());
                                }
                                if (page != null) {
                                    List<DomElement> tables = page.getElementsByTagName("table");
                                    for (int idx = 0; idx < tables.size(); idx++) {
                                        switch (idx) {
                                            case TABLE_IPVA: {
                                                // tabela IPVA
                                                HtmlTable table = (HtmlTable) tables.get(TABLE_IPVA);
                                                DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                                                List<IpvaDto> ipvas = new ArrayList<>();
                                                for (int i = 2; i < rows.size() - 1; i++) {
                                                    DomNodeList<HtmlElement> cols = rows.get(i).getElementsByTagName("td");
                                                    String periodo[] = cols.get(0).asText().split(" a ");
                                                    IpvaDto ipva = new IpvaDto();
                                                    ipva.setPInicial(periodo[0]);
                                                    ipva.setPFinal(periodo[1]);
                                                    ipva.setVlRepasse(cols.get(1).asText());
                                                    ipvas.add(ipva);
                                                }
                                                bean.setIpvas(ipvas);
                                                break;
                                            }
                                            case TABLE_ICMS: {
                                                // tabela ICMS
                                                HtmlTable table = (HtmlTable) tables.get(TABLE_ICMS);
                                                DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                                                List<IcmsDto> icmss = new ArrayList<>();
                                                for (int i = 2; i < rows.size() - 1; i++) {
                                                    DomNodeList<HtmlElement> cols = rows.get(i).getElementsByTagName("td");
                                                    String periodo[] = cols.get(0).asText().split(" a ");
                                                    IcmsDto icms = new IcmsDto();
                                                    icms.setPInicial(periodo[0]);
                                                    icms.setPFinal(periodo[1]);
                                                    icms.setDtcredito(cols.get(1).asText());
                                                    icms.setVlRepasse(cols.get(2).asText());
                                                    icmss.add(icms);
                                                }
                                                bean.setImcs(icmss);
                                                break;
                                            }
                                            case TABLE_ROYALTY: {
                                                // tabela Royaty
                                                HtmlTable table = (HtmlTable) tables.get(TABLE_ROYALTY);
                                                DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                                                List<RoyaltyDto> royalties = new ArrayList<>();
                                                for (int i = 2; i < rows.size() - 1; i++) {
                                                    DomNodeList<HtmlElement> cols = rows.get(i).getElementsByTagName("td");
                                                    RoyaltyDto royalty = new RoyaltyDto();
                                                    royalty.setRoyalty(cols.get(0).asText());
                                                    royalty.setDtcredito(cols.get(1).asText());
                                                    royalty.setVlbruto(cols.get(2).asText());
                                                    royalty.setPasep(cols.get(3).asText());
                                                    royalties.add(royalty);
                                                }
                                                bean.setRoyalties(royalties);
                                                break;
                                            }
                                            case TABLE_IPI: {
                                                // tabela IPI
                                                HtmlTable table = (HtmlTable) tables.get(TABLE_IPI);
                                                DomNodeList<HtmlElement> rows = table.getElementsByTagName("tr");
                                                List<IpiDto> ipis = new ArrayList<>();
                                                for (int i = 1; i < rows.size() - 1; i++) {
                                                    DomNodeList<HtmlElement> cols = rows.get(i).getElementsByTagName("td");
                                                    IpiDto ipi = new IpiDto();
                                                    ipi.setIpi(cols.get(0).asText());
                                                    ipi.setDtcredito(cols.get(1).asText());
                                                    ipi.setVlbruto(cols.get(2).asText());
                                                    ipi.setVlfundo(cols.get(3).asText());
                                                    ipi.setPasep(cols.get(5).asText());
                                                    ipis.add(ipi);
                                                }
                                                bean.setIpis(ipis);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    lst.add(bean);
                }
            }
        }
        return lst;
    }
}
