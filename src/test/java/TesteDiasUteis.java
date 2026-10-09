import dominio.CalculadoraPrazo;
import portas.saida.CalendarioPorta;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TesteDiasUteis {

    public static void main(String[] args) {

        CalculadoraPrazo calculadora = new CalculadoraPrazo();

        // Calendário simulado: dias úteis são de segunda a sexta.
        CalendarioPorta calendario = data -> {
            DayOfWeek dia = data.getDayOfWeek();

            //Dia sem expediente: 05/10/2026.
            boolean diaSemExpediente =
                    data.equals(LocalDate.of(2026, 10, 5));

            return dia != DayOfWeek.SATURDAY
                    && dia != DayOfWeek.SUNDAY
                    && !diaSemExpediente;
        };

        // ignorando o dia sem expediente, = quinta-feira.
        LocalDate inicio = LocalDate.of(2026, 10, 2);
        LocalDate esperado = LocalDate.of(2026, 10, 8);

        LocalDate resultado = calculadora.calcularDiasUteis(
                inicio, 3, calendario
        );

        if (!resultado.equals(esperado)) {
            throw new AssertionError(
                    "Erro no cálculo de dias úteis. Esperado: "
                            + esperado + "; obtido: " + resultado
            );
        }

        System.out.println("Teste de dias úteis: APROVADO");

        // Teste 2: quantidade zero deve ser rejeitada.
        try {
            calculadora.calcularDiasUteis(inicio, 0, calendario);

            throw new AssertionError(
                    "A calculadora deveria rejeitar quantidade zero."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Teste de quantidade zero em dias úteis: APROVADO"
            );
        }

        // Teste 3: quantidade negativa deve ser rejeitada.
        try {
            calculadora.calcularDiasUteis(inicio, -3, calendario);

            throw new AssertionError(
                    "A calculadora deveria rejeitar quantidade negativa."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Teste de quantidade negativa em dias úteis: APROVADO"
            );
        }

        // Teste 4: ignorar sábado e domingo.
        LocalDate sexta = LocalDate.of(2026, 10, 9);
        LocalDate segundaEsperada = LocalDate.of(2026, 10, 12);

        LocalDate resultadoFimDeSemana =
                calculadora.calcularDiasUteis(
                    sexta, 1, calendario
                );

        if (!resultadoFimDeSemana.equals(segundaEsperada)) {
            throw new AssertionError(
                    "Erro ao ignorar fim de semana. Esperado: "
                            + segundaEsperada
                            + "; obtido: " + resultadoFimDeSemana
            );
        }

        System.out.println(
            "Teste de fim de semana em dias úteis: APROVADO"
        );
    }
}