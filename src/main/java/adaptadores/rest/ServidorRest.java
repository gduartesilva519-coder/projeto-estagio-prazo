package adaptadores.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import adaptadores.postgresql.CalendarioPostgres;
import aplicacao.CalculadorPrazoService;
import portas.entrada.CalculadorPrazoPorta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;

public class ServidorRest {

    public static void main(String[] args) throws IOException {

        CalendarioPostgres calendario =
                new CalendarioPostgres();

        CalculadorPrazoPorta calculador =
                new CalculadorPrazoService(calendario);

        HttpServer servidor = HttpServer.create(
            new InetSocketAddress(8081),
            0
        );

        servidor.createContext(
            "/prazo/v1/apuracao",
            exchange -> responder(exchange, calculador)
        );

        servidor.createContext(
            "/",
            ServidorRest::responderPaginaInicial
        );

        System.out.println(
            "Servidor REST iniciado na porta 8081."
        );

        servidor.start();
    }

    private static void responder(
        HttpExchange exchange,
        CalculadorPrazoPorta calculador
    ) throws IOException {

        InputStream entrada = exchange.getRequestBody();

        java.util.Scanner scanner =
                new java.util.Scanner(entrada, "UTF-8")
                        .useDelimiter("\\A");

        String corpo = scanner.hasNext()
                ? scanner.next()
                : "";

        System.out.println("Recebido: " + corpo);

        String inicioTexto = "";

        if (corpo.contains("\"inicio\"")) {
            inicioTexto = corpo
                    .replaceAll(
                        ".*\"inicio\"\\s*:\\s*\"([^\"]+)\".*",
                        "$1"
                    );
        }

        String quantidadeTexto = "";

        if (corpo.contains("\"quantidade\"")) {
            quantidadeTexto = corpo
                    .replaceAll(
                        ".*\"quantidade\"\\s*:\\s*(-?\\d+).*",
                        "$1"
                    );
        }

        if (inicioTexto.isEmpty() || quantidadeTexto.isEmpty()) {

            String resposta =
                    "{\"erro\":\"Campos obrigatórios ausentes.\"}";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                400,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);
            }

            return;
        }

        LocalDate inicio;

        try {

            inicio = LocalDate.parse(inicioTexto);

        } catch (Exception e) {

            String resposta =
                    "{\"erro\":\"Data de início inválida.\"}";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                400,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);
            }

            return;
        }

        int quantidade =
                Integer.parseInt(quantidadeTexto);

        String unidade = "";

        if (corpo.contains("\"unidade\"")) {
            unidade = corpo
                    .replaceAll(
                        ".*\"unidade\"\\s*:\\s*\"([^\"]+)\".*",
                        "$1"
                    );
        }

        String regra = "";

        if (corpo.contains("\"regra\"")) {
            regra = corpo
                    .replaceAll(
                        ".*\"regra\"\\s*:\\s*\"([^\"]+)\".*",
                        "$1"
                    );
        }

        try {

            LocalDate vencimento =
                    calculador.calcular(
                        inicio,
                        quantidade,
                        unidade,
                        regra
                    );

            String resposta =
                    "{\"vencimento\":\"" + vencimento
                    + "\",\"regraAplicada\":\"" + regra + "\"}";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                200,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);
            }

        } catch (IllegalArgumentException e) {

            String resposta =
                    "{\"erro\":\"" + e.getMessage() + "\"}";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                422,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);
            }

        } catch (IllegalStateException e) {

            String resposta =
                    "{\"erro\":\"" + e.getMessage() + "\"}";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                409,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);
            }

        } catch (RuntimeException e) {

            String resposta =
                    "{\"erro\":\"Erro ao consultar o calendário.\"}";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                500,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);
            }
        }
    }

    private static void responderPaginaInicial(
            HttpExchange exchange
    ) throws IOException {

        InputStream entrada =
                ServidorRest.class
                        .getClassLoader()
                        .getResourceAsStream("index.html");

        if (entrada == null) {

            String resposta =
                    "index.html não encontrado.";

            byte[] respostaBytes =
                    resposta.getBytes("UTF-8");

            exchange.sendResponseHeaders(
                404,
                respostaBytes.length
            );

            try (OutputStream saida =
                    exchange.getResponseBody()) {

                saida.write(respostaBytes);    
            }

            return;
        }

        java.io.ByteArrayOutputStream buffer =
                new java.io.ByteArrayOutputStream();

        byte[] dados = new byte[1024];

        int quantidadeLida;

        while ((quantidadeLida = entrada.read(dados)) != -1) {
            buffer.write(dados, 0, quantidadeLida);
        }

        byte[] pagina =
                buffer.toByteArray();

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");

        exchange.sendResponseHeaders(
            200, 
            pagina.length
        );

        try (OutputStream saida =
                exchange.getResponseBody()) {

            saida.write(pagina);
        }
    }
}