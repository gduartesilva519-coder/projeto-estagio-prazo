import dominio.CalculadoraPrazo;
import java.time.LocalDate;

public class TesteCalculadoraPrazo {

public static void main(String[] args) {
    
    CalculadoraPrazo calculadora = new CalculadoraPrazo();

    // Teste 1: dias corridos
    LocalDate inicio = LocalDate.of(2026, 10, 8);
    LocalDate resultado = calculadora.calcularDiasCorridos(inicio, 3);

    LocalDate esperado = LocalDate.of(2026, 10, 11);

    if (!resultado.equals(esperado)) {
        throw new  AssertionError("Erro no cálculo de dias corridos.");
    }

    System.out.println("Teste de dias corridos: APROVADO");

    // Teste 2: quantidade zero deve ser rejeitada
    try {
        calculadora.calcularDiasCorridos(inicio, 0);

        throw new AssertionError(
                "A calculadora deveria rejeitar quantidade zero."
        );

    } catch (IllegalArgumentException e) {
        System.out.println("Teste de quantidade zero: APROVADO");
    }

    // Teste 3: quantidade negativa deve ser rejeitada
    try {
        calculadora.calcularDiasCorridos(inicio, -3);

        throw new AssertionError(
                "A calculadora deveria rejeitar quantidade negativa."
        );

    } catch (IllegalArgumentException e) {
        System.out.println("Teste de quantidade negativa: APROVADO");
    }
}

}