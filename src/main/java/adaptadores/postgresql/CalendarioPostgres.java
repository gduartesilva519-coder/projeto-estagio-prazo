package adaptadores.postgresql;

import portas.saida.CalendarioPorta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CalendarioPostgres implements CalendarioPorta {

    
    private final String url = System.getenv().getOrDefault(
        "PRAZO_DB_URL",
        "jdbc:postgresql://localhost:5432/prazo"
    );

    private final String usuario = System.getenv().getOrDefault(
        "PRAZO_DB_USER",
        "prazo"
    );

    private final String senha = System.getenv("PRAZO_DB_PASSWORD");

    @Override
    public boolean temExpediente(LocalDate data) {

        String sql = "SELECT tem_expediente FROM calendario WHERE data = ?";

        try (
            Connection conexao = DriverManager.getConnection(
                url, 
                usuario, 
                senha
            );
            PreparedStatement comando = 
            conexao.prepareStatement(sql)
        ) {

            comando.setDate(
                1, 
                java.sql.Date.valueOf(data)
            );

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getBoolean("tem_expediente");
                }

                throw new IllegalStateException(
                    "Data não encontrada na cobertura do calendário: " + data
                );
            }

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {
            
            throw new RuntimeException(
                "Erro ao consultar o calendário no PostgreSQL.",
                e
            );
        }
    }
}