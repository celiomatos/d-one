package br.com.done.done.controller;

import br.com.done.done.dto.TopFiveCredoresDto;
import br.com.done.done.model.Credor;
import br.com.done.done.service.CredorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credores")
public class CredorController {

    @Autowired
    private CredorService credorService;

    @GetMapping("/top-five")
    public List<TopFiveCredoresDto> topFiveCredores(@RequestParam String dateInicial, @RequestParam String dateFinal) {
        return credorService.topFive(dateInicial, dateFinal);
    }

    @GetMapping("/find-all")
    public Page<Credor> findAll(@RequestParam Integer page, @RequestParam Integer count,
                                @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return credorService.findAll(PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }

    @GetMapping("/find-by-nome/{nome}")
    public Page<Credor> findByName(@PathVariable String nome, @RequestParam Integer page, @RequestParam Integer count,
                                   @RequestParam String order, @RequestParam String sortProperty) {
        Sort.Direction direction = (order == null || order.equalsIgnoreCase("ASC"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return credorService.findByNome(nome, PageRequest.of(page, count, new Sort(direction, sortProperty)));
    }
}
