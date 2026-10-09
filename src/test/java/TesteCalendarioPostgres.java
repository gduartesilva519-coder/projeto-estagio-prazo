import adaptadores.postgresql.CalendarioPostgres;
import java.time.LocalDate;

public class TesteCalendarioPostgres {

     public static void main(String[] args) {

        CalendarioPostgres calendario = new CalendarioPostgres();

        LocalDate data = LocalDate.of(2026, 10, 5);

        boolean temExpediente = calendario.temExpediente(data);

        System.out.println("Data: " + data);
        System.out.println("Tem expediente: " + temExpediente);
     }
}