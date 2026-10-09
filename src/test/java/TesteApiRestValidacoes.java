import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TesteApiRestValidacoes {

public static void main(String[] args) throws Exception {

    URL url = new URL(
        "http://localhost:8081/prazo/v1/apuracao"
    );

    HttpURLConnection conexao =
        (HttpURLConnection) url.openConnection();

    conexao.setRequestMethod("POST");
    conexao.setRequestProperty(
        "Content-Type", 
        "application/json"
    );
    conexao.setDoOutput(true);

    String corpo = "{"
        + "\"inicio\": \"2026-10-08\","
        + "\"quantidade\": 0,"
        + "\"unidade\": \"DIAS\","
        + "\"regra\": \"CORRIDOS\""
        + "}";

    try (OutputStream saida = conexao.getOutputStream()) {
        saida.write(
            corpo.getBytes(StandardCharsets.UTF_8)
        );
    }

    int status = conexao.getResponseCode();

    if (status != 422) {
        throw new AssertionError(
            "Esperado HTTP 422, mas recebido HTTP " + status
        );
    }

    System.out.println(
        "Teste de quantidade zero na API REST: APROVADO"
    );
    System.out.println("Status HTTP: " + status);

    conexao.disconnect();
}
    
}