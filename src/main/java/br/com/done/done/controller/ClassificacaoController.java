package br.com.done.done.controller;

import br.com.done.done.model.Classificacao;
import br.com.done.done.service.ClassificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classificacoes")
public class ClassificacaoController {

    @Autowired
    private ClassificacaoService classificacaoService;

    @GetMapping("/find-all")
    public Page<Classificacao> findAll(@RequestParam Integer page, @RequestParam Integer count,
                                       @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return classificacaoService.findAll(PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }

    @GetMapping("/find-by-nome/{nome}")
    public Page<Classificacao> findByName(@PathVariable String nome, @RequestParam Integer page, @RequestParam Integer count,
                                          @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return classificacaoService.findByNome(nome, PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }
}
