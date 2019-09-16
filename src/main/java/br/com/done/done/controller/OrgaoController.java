package br.com.done.done.controller;

import br.com.done.done.dto.TopFiveOrgaosDto;
import br.com.done.done.model.Orgao;
import br.com.done.done.service.OrgaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orgaos")
public class OrgaoController {

    @Autowired
    private OrgaoService orgaoService;

    @GetMapping("/top-five")
    public List<TopFiveOrgaosDto> topFive(@RequestParam String dateInicial, @RequestParam String dateFinal) {
        return orgaoService.topFive(dateInicial, dateFinal);
    }

    @GetMapping("/find-all")
    public Page<Orgao> findAll(@RequestParam Integer page, @RequestParam Integer count,
                               @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return orgaoService.findAll(PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }

    @GetMapping("/find-by-nome/{nome}")
    public Page<Orgao> findByName(@PathVariable String nome, @RequestParam Integer page, @RequestParam Integer count,
                                  @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return orgaoService.findByNome(nome, PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }
}
