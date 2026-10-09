package adaptadores.memoria;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import portas.saida.CalendarioPorta;

public class CalendarioFicticio implements CalendarioPorta {

    private final Set<LocalDate> diasSemExpediente = new HashSet<>();

    public CalendarioFicticio() {
        diasSemExpediente.add(LocalDate.of(2026, 10, 5));
    }

    @Override
    public boolean temExpediente(LocalDate data) {

        if (data.getDayOfWeek().getValue() >= 6) {
            return false;
        }

        return !diasSemExpediente.contains(data);
    }
}