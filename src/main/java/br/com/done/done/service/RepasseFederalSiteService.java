package br.com.done.done.service;

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
public class RepasseFederalSiteService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<String[]> getRepasse(String diaInicial, String diaFinal) {

        List<String[]> result = new ArrayList<>();

        String url = "https://www42.bb.com.br/portalbb/daf/beneficiario.bbx";

        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);

        List<Municipio> municipios = municipioRepository.findByUfSiglaOrderByNome("AM");

        if (municipios != null) {
            for (Municipio municipio : municipios) {
                log.info("Repasse federal para municipio: " + municipio.getNome());

                try (WebClient wc = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
                    wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
                    wc.getOptions().setJavaScriptEnabled(true);
                    wc.getCookieManager().setCookiesEnabled(true);

                    HtmlPage page = null;
                    try {
                        page = wc.getPage(url);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    if (page != null) {
                        String m = StringUtil.removerAcento(municipio.getNome());
                        HtmlInput beneficiario = (HtmlInput) page.getElementById("formulario:j_id34:nome");
                        beneficiario.setValueAttribute(m);
                        HtmlInput buscar = (HtmlInput) page.getElementById("formulario:botaoBuscar");

                        HtmlPage consulta = null;
                        try {
                            consulta = buscar.click();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                        if (consulta != null) {

                            HtmlInput dtinicial = (HtmlInput) consulta.getElementById(
                                    "formulario:j_id44:dataInicialInputDate");
                            if (dtinicial != null) {
                                dtinicial.setValueAttribute(diaInicial);

                                HtmlInput dtfinal = (HtmlInput) consulta.getElementById(
                                        "formulario:j_id54:dataFinalInputDate");
                                dtfinal.setValueAttribute(diaFinal);

                                HtmlSelect comboBeneficiario = (HtmlSelect) consulta.getElementById("formulario:j_id34:comboBeneficiario");
                                if (comboBeneficiario.getOptions().size() > 1) {
                                    HtmlOption option = comboBeneficiario.getOptionByText(m + " - AM");
                                    comboBeneficiario.setSelectedAttribute(option, true);
                                }

                                HtmlInput continuar = (HtmlInput) consulta.getElementById(
                                        "formulario:botaoContinuar");

                                HtmlPage demonstrativo = null;
                                try {
                                    demonstrativo = continuar.click();
                                } catch (Exception ex) {
                                    log.error(ex.getMessage());
                                }

                                if (demonstrativo != null) {
                                    DomElement t = demonstrativo.getElementById("formulario:demonstrativoList");
                                    if (t != null) {
                                        DomNodeList<HtmlElement> r = t.getElementsByTagName("tr");
                                        if (r != null) {
                                            // criada para descartar os totais
                                            boolean loop = true;
                                            String data = "";
                                            String sigla = "";
                                            String fundo = "";

                                            for (int i = 3; i < r.size(); i++) {
                                                DomNodeList<HtmlElement> c = r.get(i).getElementsByTagName("td");
                                                if (c != null) {
                                                    if (c.size() == 1) {
                                                        if (!c.get(0).asText().equalsIgnoreCase("TOTAL DOS REPASSES NO PERIODO")) {
                                                            String f[] = c.get(0).asText().trim().split("-");
                                                            sigla = f[0].trim();
                                                            fundo = f[1].trim();
                                                            i++;
                                                            loop = true;
                                                        }
                                                    } else if (c.size() == 3 && loop) {

                                                        String dt = c.get(0).asText();
                                                        String pc = c.get(1).asText();
                                                        String vl = c.get(2).asText();
                                                        if (!dt.isEmpty() && dt.equalsIgnoreCase("TOTAIS")) {
                                                            loop = false;
                                                        } else if ((!dt.isEmpty() || !pc.isEmpty() || !vl.isEmpty())
                                                                && !pc.equalsIgnoreCase("TOTAL:")) {

                                                            if (!dt.isEmpty()) {
                                                                data = c.get(0).asText().trim();
                                                            }
                                                            data = data.replace(".", "/");
                                                            String vls[] = c.get(2).asText().trim().split(" ");

                                                            String dados[] = new String[7];
                                                            dados[0] = String.valueOf(municipio.getId());
                                                            dados[1] = sigla;
                                                            dados[2] = fundo;
                                                            dados[3] = data;
                                                            dados[4] = c.get(1).asText().trim(); // parcela
                                                            dados[5] = vls[1].trim(); // valor
                                                            dados[6] = vls[2].trim();// tipo credito ou debito

                                                            result.add(dados);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
