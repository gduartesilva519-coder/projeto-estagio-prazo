Projeto de cálculo de prazos

Um serviço que recebe uma data de início, uma quantidades de dias ou meses e uma regra de contagem,
a resposta deve informar quando o prazo termina e mostrar como o cálculo chegou àquela data.

Tecnologias: 

Java 8, PostgreSQL 18, Docker.

Arquiteturas:

Arquitetura hexagonal = Usuário -> Adaptador de entrada: REST -> Porta de entrada: CalculadorPrazoPorta.java -> 
CalculadorPrazoService -> Núcleo: CalculadoraPrazo.java -> Porta de saída: CalendarioPorta.java ->
Adaptador de saída: CalendarioPostgres.java -> Postgres

Banco de dados: 



Testes: 



Objetivo: 

