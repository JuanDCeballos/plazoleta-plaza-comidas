package co.juan.plazacomidas.twiliosender;

import co.juan.plazacomidas.model.notificacion.NotificacionGateway;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioNotificacionSender implements NotificacionGateway {

    private final String fromNumber;

    public TwilioNotificacionSender(@Value("${twilio.account-sid}") String accountSid,
                                    @Value("${twilio.auth-token}") String authToken,
                                    @Value("${twilio.from-number}") String fromNumber) {
        this.fromNumber = fromNumber;
        Twilio.init(accountSid, authToken);
    }


    @Override
    public void enviarNotificacion(String numeroCelular, String mensaje) {
        if (numeroCelular == null || mensaje == null) {
            log.warn("Número de celular o mensaje nulo, no se envía notificación.");
            return;
        }

        try {
            PhoneNumber to = new PhoneNumber(numeroCelular);
            PhoneNumber from = new PhoneNumber(this.fromNumber);

            Message message = Message.creator(to, from, mensaje).create();

            log.info("Mensaje enviado via Twilio. SID: {}", message.getSid());
        } catch (Exception e) {
            log.error("Error al enviar el SMS via Twilio", e);
            throw new RuntimeException("Error al enviar la notificación vía Twilio: " + e.getMessage());
        }
    }
}
