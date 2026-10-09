package aplicacao;

import dominio.CalculadoraPrazo;
import portas.entrada.CalculadorPrazoPorta;
import portas.saida.CalendarioPorta;

import java.time.LocalDate;

public class CalculadorPrazoService implements CalculadorPrazoPorta {

    private final CalculadoraPrazo calculadora;
    private final CalendarioPorta calendario;

    public CalculadorPrazoService(CalendarioPorta calendario) {
        this.calculadora = new CalculadoraPrazo();
        this.calendario = calendario;
    }

    @Override
    public LocalDate calcular(
            LocalDate inicio,
            int quantidade,
            String unidade,
            String regra) {

        if ("DIAS".equals(regra)) {

            return calculadora.calcularDiasUteis(
                inicio, 
                quantidade, 
                calendario
            );
        }

        if ("CORRIDOS".equals(regra)) {

            return calculadora.calcularDiasCorridos(
                inicio, 
                quantidade
            );
        }

        throw new IllegalArgumentException(
                "Regra não suportada: " + regra
        );
    }
}