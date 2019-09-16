package br.com.done.done.service;

import br.com.done.done.dto.EmpenhoDto;
import br.com.done.done.model.Classificacao;
import br.com.done.done.model.Empenho;
import br.com.done.done.model.EmpenhoDespesa;
import br.com.done.done.repository.EmpenhoDespesaRepository;
import br.com.done.done.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class EmpenhoDespesaService {

    @Autowired
    private EmpenhoDespesaRepository empenhoDespesaRepository;

    @Autowired
    private ClassificacaoService classificacaoService;

    /**
     * @param empenhoDto
     * @param empenho
     */
    public void save(EmpenhoDto empenhoDto, Empenho empenho) {

        Optional<EmpenhoDespesa> optEmpenhoDespesa = empenhoDespesaRepository.findByEmpenhoIdAndAno(
                empenho.getId(), empenhoDto.getAno());

        EmpenhoDespesa empenhoDespesa = optEmpenhoDespesa.orElseGet(EmpenhoDespesa::new);
        empenhoDespesa.setAno(String.valueOf(empenhoDto.getAno()));
        empenhoDespesa.setEmpenho(empenho);
        empenhoDespesa.setValor(NumberUtil.strToBigDecimal(empenhoDto.getVaDocumento()));
        if (empenhoDespesa.getId() == 0L) {
            empenhoDespesa.setLancamento(new Date());
        }
        Classificacao classificacao = classificacaoService.getOrCreateClassificacao(empenhoDto.getNaturezaDespesa());
        empenhoDespesa.setNatureza(classificacao);

        empenhoDespesaRepository.save(empenhoDespesa);
    }
}
