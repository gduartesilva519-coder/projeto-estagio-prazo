import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TesteApiRest {
    
    public static void main(String[] args) throws Exception {

        URL url = new URL(
            "http://localhost:8081/prazo/v1/apuracao"
        );

        HttpURLConnection conexao =
                (HttpURLConnection) url.openConnection();

        conexao.setRequestMethod("POST");
        conexao.setRequestProperty(
            "Content-Type", "application/json; charset=UTF-8"
        );
        conexao.setDoOutput(true);

        String json = "{"
                + "\"inicio\":\"2026-10-08\","
                + "\"quantidade\":3,"
                + "\"unidade\":\"DIAS\","
                + "\"regra\":\"CORRIDOS\""
                + "}";

        try (OutputStream saida = conexao.getOutputStream()) {
            saida.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conexao.getResponseCode();

        InputStream respostaStream = status >= 400
                ? conexao.getErrorStream()
                : conexao.getInputStream();

        StringBuilder resposta = new StringBuilder();

        if (respostaStream != null) {
            try (BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(
                        respostaStream, StandardCharsets.UTF_8))) {

                String linha;

                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }
            }
        }

        if (status != 200 && status != 202) {
            throw new AssertionError(
                    "Status HTTP inesperado: " + status
                            + ". Resposta: " + resposta
            );
        }

        if (!resposta.toString().contains("2026-10-11")) {
            throw new AssertionError(
                    "Vencimento inesperado. Resposta: " + resposta
            );
        }

        System.out.println("Teste de API REST válida: APROVADO");
        System.out.println("Status HTTP: " + status);
        System.out.println("Resposta: " + resposta);

        conexao.disconnect();
    }
}