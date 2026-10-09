package dominio;

import portas.saida.CalendarioPorta;
import java.time.LocalDate;

public class CalculadoraPrazo {

    public LocalDate calcularDiasCorridos(LocalDate inicio, int quantidade) {

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de dias deve ser positiva."
            );
        }

        return inicio.plusDays(quantidade);
    }

    public LocalDate calcularDiasUteis(
            LocalDate inicio,
            int quantidade,
            CalendarioPorta calendario) {

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de dias deve ser positiva."
            );
        }

        LocalDate data = inicio;
        int diasContados = 0;

        while (diasContados < quantidade) {

            data = data.plusDays(1);

            if (calendario.temExpediente(data)) {
                diasContados++;
            }
        }

        return data;
    }
}