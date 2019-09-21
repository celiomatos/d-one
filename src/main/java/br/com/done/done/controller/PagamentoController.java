package br.com.done.done.controller;

import br.com.done.done.dto.FiveYearsDto;
import br.com.done.done.dto.PagamentoSearchDto;
import br.com.done.done.model.Pagamento;
import br.com.done.done.service.ExcelGeneratorService;
import br.com.done.done.service.PagamentoService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/search")
    public Page<Pagamento> findPagamentos(@RequestBody @Valid PagamentoSearchDto pagSearchDto) {
        return pagamentoService.findAll(pagSearchDto);
    }

    @PostMapping("/sum-pagamento-valor")
    public BigDecimal sumPagamentoValor(@RequestBody PagamentoSearchDto pagSearchDto) {
        return pagamentoService.sumPagamentoValor(pagSearchDto);
    }

    @GetMapping("/five-years")
    public List<FiveYearsDto> fiveYearsPagagmentos() {
        return pagamentoService.fiveYearsPagamentos();
    }

    @PostMapping("/pagamentos-to-excell")
    public void pagamentosToExcell(@RequestBody PagamentoSearchDto pagSearchDto) throws IOException {

        InputStream targetStream = excelGeneratorService.pagamentosToExcell(pagSearchDto);
        response.addHeader("Content-disposition", "attachment;filename=sample.xlsx");
        response.setContentType("application/vnd.ms-excel");
        IOUtils.copy(targetStream, response.getOutputStream());

        response.flushBuffer();
    }
}
