package br.com.done.done.service;

import br.com.done.done.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksService {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private EmpenhoService empenhoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RepasseService repasseService;

    /**
     * second, minute, hour, day of month, month, day(s) of week
     */
    @Scheduled(cron = "10 10 9,15,20 * * *")
    public void pagamentoMesAtual() {
        pagamentoService.updateBySchedule(true);
    }

    @Scheduled(cron = "10 10 8,14,19 * * *")
    public void pagamentoMesAnterior() {
        pagamentoService.updateBySchedule(false);
    }

    @Scheduled(cron = "10 10 10,16,21 * * *")
    public void empenhoAnoAtual() {
        empenhoService.updateBySchedule(MyConstant.EMPENHO_ANO_ATUAL);
    }

    @Scheduled(cron = "10 10 11,17,22 * * *")
    public void empenhoAnoAnterior() {
        empenhoService.updateBySchedule(MyConstant.EMPENHO_ANOS_ANTERIORES);
    }

    @Scheduled(cron = "10 20 * * * *")
    public void sendDAlertMessage()  {
        emailService.sendDAlert();
    }

    @Scheduled(cron = "10 10 10 * * *")
    public void sendPaymentMessage()  {
        emailService.sendPayment();
    }

    @Scheduled(cron = "10 10 12,18,23 * * *")
    public void repasseEstadual()  {
        repasseService.updateBySchedule();
    }
}
