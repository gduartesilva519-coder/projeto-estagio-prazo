import adaptadores.postgresql.CalendarioPostgres;
import aplicacao.CalculadorPrazoService;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        CalendarioPostgres calendario =
                 new CalendarioPostgres();

        CalculadorPrazoService service = 
                new CalculadorPrazoService(calendario);
                
        LocalDate inicio = 
                LocalDate.of(2026, 10, 2);

        LocalDate vencimento =
                service.calcular(inicio,
                        3,
                        "DIAS",
                        "UTEIS"
                );

        System.out.println("Data de início: " + inicio);
        System.out.println("Vencimento: " + vencimento);
    }
}