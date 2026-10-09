package portas.saida;

import java.time.LocalDate;

public interface CalendarioPorta {

    boolean temExpediente(LocalDate data);
}