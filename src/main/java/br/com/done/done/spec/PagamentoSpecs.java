package br.com.done.done.spec;

import br.com.done.done.model.Pagamento;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PagamentoSpecs {

    public static Specification<Pagamento> isOrgaos(List<Long> ids) {
        return (root, query, builder) -> root.join("orgao").get("id").in(ids);
    }

    public static Specification<Pagamento> isFontes(List<String> ids) {
        return (root, query, builder) -> root.join("fonte").get("id").in(ids);
    }

    public static Specification<Pagamento> isClassificacoes(List<Long> ids) {
        return (root, query, builder) -> root.join("classificacao").get("id").in(ids);
    }

    public static Specification<Pagamento> isCredores(List<Long> ids) {
        return (root, query, builder) -> root.join("credor").get("id").in(ids);
    }

    public static Specification<Pagamento> isDataLess(Date date) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("data"), date);
    }

    public static Specification<Pagamento> isDataGreater(Date date) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("data"), date);
    }

    public static Specification<Pagamento> isValorLess(BigDecimal valor) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("valor"), valor);
    }

    public static Specification<Pagamento> isValorGreater(BigDecimal valor) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("valor"), valor);
    }
}
