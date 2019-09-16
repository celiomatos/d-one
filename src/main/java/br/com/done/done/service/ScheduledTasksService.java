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

    /**
     * second, minute, hour, day of month, month, day(s) of week
     */
    @Scheduled(cron = "10 20 9,15,20 * * *")
    public void pagamentoMesAtual() {
        pagamentoService.updateBySchedule(true);
    }

    @Scheduled(cron = "20 50 */5 * * *")
    public void pagamentoMesAnterior() {
        pagamentoService.updateBySchedule(false);
    }

    @Scheduled(cron = "30 10 */2 * * *")
    public void empenhoAnoAtual() {
        empenhoService.updateBySchedule(MyConstant.EMPENHO_ANO_ATUAL);
    }

    @Scheduled(cron = "40 40 * * * *")
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
}
