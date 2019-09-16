package br.com.done.done.controller;

import br.com.done.done.model.Fonte;
import br.com.done.done.service.FonteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fontes")
public class FonteController {

    @Autowired
    private FonteService fonteService;

    @GetMapping("/find-all")
    public Page<Fonte> findAll(@RequestParam Integer page, @RequestParam Integer count,
                               @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return fonteService.findAll(PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }

    @GetMapping("/find-by-nome/{nome}")
    public Page<Fonte> findByName(@PathVariable String nome, @RequestParam Integer page, @RequestParam Integer count,
                                  @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return fonteService.findByNome(nome, PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }
}
