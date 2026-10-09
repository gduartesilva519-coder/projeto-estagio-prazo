import java.sql.Connection;
import java.sql.DriverManager;

public class TestePostgreSQL {

    public static void main(String[] args) throws Exception {

        String url = System.getenv().getOrDefault(
        "PRAZO_DB_URL",
        "jdbc:postgresql://localhost:5432/prazo"
        );

        String usuario = System.getenv().getOrDefault(
        "PRAZO_DB_USER",
        "prazo"
        );

        String senha = System.getenv("PRAZO_DB_PASSWORD");

        Connection conexao =
                DriverManager.getConnection(url, usuario, senha);

        System.out.println("Conexão com PostgreSQL realizada com sucesso!");

        conexao.close();
    }
}