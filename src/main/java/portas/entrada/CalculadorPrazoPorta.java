package portas.entrada;

import java.time.LocalDate;

public interface CalculadorPrazoPorta {

    LocalDate calcular(
            LocalDate inicio,
            int quantidade,
            String unidade,
            String regra
    );
}