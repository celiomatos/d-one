package br.com.done.done.service;

import br.com.done.done.dto.*;
import br.com.done.done.model.*;
import br.com.done.done.repository.*;
import br.com.done.done.util.DateUtil;
import br.com.done.done.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RepasseService {

    @Autowired
    private RepasseSiteService siteService;

    @Autowired
    private RepasseRepository repasseRepository;

    @Autowired
    private RepasseIpvaRepository repasseIpvaRepository;

    @Autowired
    private RepasseIcmsRepository repasseIcmsRepository;

    @Autowired
    private RepasseIpiRepository repasseIpiRepository;

    @Autowired
    private RepasseRoyaltyRepository repasseRoyaltyRepository;

    public void updateBySchedule() {

        List<RepasseDto> lst = siteService.acessar();
        if (!lst.isEmpty()) {

            lst.forEach((bean) -> {
                Long idmunicipio = bean.getMunicipio();
                try {
                    int ano = Integer.parseInt(bean.getAno());
                    int mes = Integer.parseInt(bean.getMes());

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DATE, 1);
                    cal.set(Calendar.MONTH, (mes - 1));
                    cal.set(Calendar.YEAR, ano);

                    Optional<Repasse> rep = repasseRepository.findByReferenciaAndMunicipioId(cal.getTime(), idmunicipio);

                    if (rep.isPresent()) {
                        editRepasse(rep.get(), bean);

                    } else {
                        createRepasse(bean);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            });
        }
    }

    private void createRepasse(RepasseDto bean) throws Exception {
        Repasse rep = new Repasse();

        int ano = Integer.parseInt(bean.getAno());
        int mes = Integer.parseInt(bean.getMes());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, (mes - 1));
        cal.set(Calendar.YEAR, ano);

        rep.setReferencia(cal.getTime());
        rep.setMunicipio(new Municipio(bean.getMunicipio()));
        repasseRepository.save(rep);
        // ipva
        List<IpvaDto> ipvas = bean.getIpvas();
        if (!ipvas.isEmpty()) {
            for (IpvaDto i : ipvas) {
                RepasseIpva ipva = new RepasseIpva();
                ipva.setRepasse(rep);
                ipva.setDtarrecadacaoInicial(DateUtil.strToDate(i.getPInicial()));
                ipva.setDtarrecadacaoFinal(DateUtil.strToDate(i.getPFinal()));
                ipva.setValorRepasseIpva(NumberUtil.strToBigDecimal(i.getVlRepasse()));
                ipva.setRemovido(false);
                ipva.setDtlancamento(new Date());
                repasseIpvaRepository.save(ipva);
            }
        }
        // icms
        List<IcmsDto> icmss = bean.getImcs();
        if (!icmss.isEmpty()) {
            for (IcmsDto i : icmss) {
                RepasseIcms icms = new RepasseIcms();
                icms.setRepasse(rep);
                icms.setDtarrecadacaoInicial(DateUtil.strToDate(i.getPInicial()));
                icms.setDtarrecadacaoFinal(DateUtil.strToDate(i.getPFinal()));
                icms.setDtcredito(DateUtil.strToDate(i.getDtcredito()));
                icms.setValorRepasseIcms(NumberUtil.strToBigDecimal(i.getVlRepasse()));
                icms.setRemovido(false);
                icms.setDtlancamento(new Date());
                repasseIcmsRepository.save(icms);
            }
        }
        // royalty
        List<RoyaltyDto> royalties = bean.getRoyalties();
        if (!royalties.isEmpty()) {
            for (RoyaltyDto r : royalties) {
                RepasseRoyalty royalty = new RepasseRoyalty();
                royalty.setRepasse(rep);
                royalty.setRoyalty(r.getRoyalty());
                royalty.setDtcredito(DateUtil.strToDate(r.getDtcredito()));
                royalty.setValorBruto(NumberUtil.strToBigDecimal(r.getVlbruto()));
                royalty.setPasep(NumberUtil.strToBigDecimal(r.getPasep()));
                royalty.setRemovido(false);
                royalty.setDtlancamento(new Date());
                repasseRoyaltyRepository.save(royalty);
            }
        }
        // ipi
        List<IpiDto> ipis = bean.getIpis();
        if (!ipis.isEmpty()) {
            for (IpiDto i : ipis) {
                RepasseIpi ipi = new RepasseIpi();
                ipi.setRepasse(rep);
                ipi.setIpi(i.getIpi());
                ipi.setDtcredito(DateUtil.strToDate(i.getDtcredito()));
                ipi.setValorBruto(NumberUtil.strToBigDecimal(i.getVlbruto()));
                ipi.setFundo(NumberUtil.strToBigDecimal(i.getVlfundo()));
                ipi.setPasep(NumberUtil.strToBigDecimal(i.getPasep()));
                ipi.setRemovido(false);
                ipi.setDtlancamento(new Date());
                repasseIpiRepository.save(ipi);
            }
        }
    }

    private void editRepasse(Repasse r, RepasseDto rb) throws Exception {

        List<IpvaDto> ipva = rb.getIpvas();
        editIpva(r, ipva);
        List<IcmsDto> icms = rb.getImcs();
        editIcms(r, icms);
        List<RoyaltyDto> royalty = rb.getRoyalties();
        editRoyalty(r, royalty);
        List<IpiDto> ipi = rb.getIpis();
        editIpi(r, ipi);
    }

    private void editIpva(Repasse r, List<IpvaDto> bipva) throws Exception {
        if (bipva.isEmpty()) {
            List<RepasseIpva> ipvas = repasseIpvaRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (RepasseIpva ipva : ipvas) {
                ipva.setRemovido(true);
                repasseIpvaRepository.save(ipva);
            }
        } else {
            List<RepasseIpva> ipvas = repasseIpvaRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (IpvaDto b : bipva) {

                StringBuilder chave1 = new StringBuilder();
                chave1.append(b.getPInicial());
                chave1.append(b.getPFinal());
                chave1.append(b.getVlRepasse()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));

                boolean achou = false;

                for (int i = 0; i < ipvas.size(); i++) {
                    RepasseIpva v = ipvas.get(i);
                    StringBuilder chave2 = new StringBuilder();
                    chave2.append(DateUtil.formatDate(v.getDtarrecadacaoInicial()));
                    chave2.append(DateUtil.formatDate(v.getDtarrecadacaoFinal()));
                    chave2.append(v.getValorRepasseIpva().toString());

                    if (chave1.toString().equalsIgnoreCase(chave2.toString())) {
                        ipvas.remove(i);
                        achou = true;
                        break;
                    }
                }

                if (!achou) {
                    RepasseIpva ipva = new RepasseIpva();
                    ipva.setRepasse(r);
                    ipva.setDtarrecadacaoInicial(DateUtil.strToDate(b.getPInicial()));
                    ipva.setDtarrecadacaoFinal(DateUtil.strToDate(b.getPFinal()));
                    ipva.setValorRepasseIpva(NumberUtil.strToBigDecimal(b.getVlRepasse()));
                    ipva.setRemovido(false);
                    ipva.setDtlancamento(new Date());
                    repasseIpvaRepository.save(ipva);
                }
            }
            // removendo lancamentos que constam somente no nosso lado
            if (!ipvas.isEmpty()) {
                for (RepasseIpva ipva : ipvas) {
                    ipva.setRemovido(true);
                    repasseIpvaRepository.save(ipva);
                }
            }
        }
    }

    private void editIcms(Repasse r, List<IcmsDto> bicms) throws Exception {
        if (bicms.isEmpty()) {
            List<RepasseIcms> icmss = repasseIcmsRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (RepasseIcms icms : icmss) {
                icms.setRemovido(true);
                repasseIcmsRepository.save(icms);
            }
        } else {
            List<RepasseIcms> icmss = repasseIcmsRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (IcmsDto b : bicms) {

                StringBuilder chave1 = new StringBuilder();
                chave1.append(b.getPInicial());
                chave1.append(b.getPFinal());
                chave1.append(b.getDtcredito());
                chave1.append(b.getVlRepasse()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));

                boolean achou = false;

                for (int i = 0; i < icmss.size(); i++) {
                    RepasseIcms v = icmss.get(i);
                    StringBuilder chave2 = new StringBuilder();
                    chave2.append(DateUtil.formatDate(v.getDtarrecadacaoInicial()));
                    chave2.append(DateUtil.formatDate(v.getDtarrecadacaoFinal()));
                    chave2.append(DateUtil.formatDate(v.getDtcredito()));
                    chave2.append(v.getValorRepasseIcms().toString());
                    if (chave1.toString().equalsIgnoreCase(chave2.toString())) {
                        icmss.remove(i);
                        achou = true;
                        break;
                    }
                }

                if (!achou) {
                    RepasseIcms icms = new RepasseIcms();
                    icms.setRepasse(r);
                    icms.setDtarrecadacaoInicial(DateUtil.strToDate(b.getPInicial()));
                    icms.setDtarrecadacaoFinal(DateUtil.strToDate(b.getPFinal()));
                    icms.setDtcredito(DateUtil.strToDate(b.getDtcredito()));
                    icms.setValorRepasseIcms(NumberUtil.strToBigDecimal(b.getVlRepasse()));
                    icms.setRemovido(false);
                    icms.setDtlancamento(new Date());
                    repasseIcmsRepository.save(icms);
                }
            }
            // removendo lancamentos que constam somente no nosso lado
            if (!icmss.isEmpty()) {
                for (RepasseIcms icms : icmss) {
                    icms.setRemovido(true);
                    repasseIcmsRepository.save(icms);
                }
            }
        }
    }

    private void editRoyalty(Repasse r, List<RoyaltyDto> broyalty) throws Exception {

        if (broyalty.isEmpty()) {
            List<RepasseRoyalty> royalties = repasseRoyaltyRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (RepasseRoyalty royalty : royalties) {
                royalty.setRemovido(true);
                repasseRoyaltyRepository.save(royalty);
            }
        } else {
            List<RepasseRoyalty> royalties = repasseRoyaltyRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (RoyaltyDto b : broyalty) {

                StringBuilder chave1 = new StringBuilder();
                chave1.append(b.getRoyalty());
                chave1.append(b.getDtcredito());
                chave1.append(b.getVlbruto()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));
                chave1.append(b.getPasep()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));
                boolean achou = false;

                for (int i = 0; i < royalties.size(); i++) {
                    RepasseRoyalty v = royalties.get(i);
                    StringBuilder chave2 = new StringBuilder();
                    chave2.append(v.getRoyalty());
                    chave2.append(DateUtil.formatDate(v.getDtcredito()));
                    chave2.append(v.getValorBruto().toString());
                    chave2.append(v.getPasep().toString());

                    if (chave1.toString().equalsIgnoreCase(chave2.toString())) {
                        royalties.remove(i);
                        achou = true;
                        break;
                    }
                }

                if (!achou) {
                    RepasseRoyalty royalty = new RepasseRoyalty();
                    royalty.setRepasse(r);
                    royalty.setRoyalty(b.getRoyalty());
                    royalty.setDtcredito(DateUtil.strToDate(b.getDtcredito()));
                    royalty.setValorBruto(NumberUtil.strToBigDecimal(b.getVlbruto()));
                    royalty.setPasep(NumberUtil.strToBigDecimal(b.getPasep()));
                    royalty.setRemovido(false);
                    royalty.setDtlancamento(new Date());
                    repasseRoyaltyRepository.save(royalty);
                }
            }
            // removendo lancamentos que constam somente no nosso lado
            if (!royalties.isEmpty()) {
                for (RepasseRoyalty royalty : royalties) {
                    royalty.setRemovido(true);
                    repasseRoyaltyRepository.save(royalty);
                }
            }
        }
    }

    private void editIpi(Repasse r, List<IpiDto> bipi) throws Exception {
        if (bipi.isEmpty()) {
            List<RepasseIpi> ipis = repasseIpiRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (RepasseIpi ipi : ipis) {
                ipi.setRemovido(true);
                repasseIpiRepository.save(ipi);
            }
        } else {
            List<RepasseIpi> ipis = repasseIpiRepository.findByRepasseIdAndRemovidoFalse(r.getId());
            for (IpiDto b : bipi) {

                StringBuilder chave1 = new StringBuilder();
                chave1.append(b.getIpi());
                chave1.append(b.getDtcredito());
                chave1.append(b.getVlbruto()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));
                chave1.append(b.getVlfundo()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));
                chave1.append(b.getPasep()
                        .replaceAll("[.]", "").replaceAll("[,]", "."));

                boolean achou = false;

                for (int i = 0; i < ipis.size(); i++) {
                    RepasseIpi v = ipis.get(i);
                    StringBuilder chave2 = new StringBuilder();
                    chave2.append(v.getIpi());
                    chave2.append(DateUtil.formatDate(v.getDtcredito()));
                    chave2.append(v.getValorBruto().toString());
                    chave2.append(v.getFundo().toString());
                    chave2.append(v.getPasep().toString());

                    if (chave1.toString().equalsIgnoreCase(chave2.toString())) {
                        ipis.remove(i);
                        achou = true;
                        break;
                    }
                }

                if (!achou) {
                    RepasseIpi ipi = new RepasseIpi();
                    ipi.setRepasse(r);
                    ipi.setIpi(b.getIpi());
                    ipi.setDtcredito(DateUtil.strToDate(b.getDtcredito()));
                    ipi.setValorBruto(NumberUtil.strToBigDecimal(b.getVlbruto()));
                    ipi.setFundo(NumberUtil.strToBigDecimal(b.getVlfundo()));
                    ipi.setPasep(NumberUtil.strToBigDecimal(b.getPasep()));
                    ipi.setRemovido(false);
                    ipi.setDtlancamento(new Date());
                    repasseIpiRepository.save(ipi);
                }
            }
            // removendo lancamentos que constam somente no nosso lado
            if (!ipis.isEmpty()) {
                for (RepasseIpi ipi : ipis) {
                    ipi.setRemovido(true);
                    repasseIpiRepository.save(ipi);
                }
            }
        }
    }
}
