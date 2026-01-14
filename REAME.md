# Geospatial API - Desafio Java SCCON

Esta é uma API REST desenvolvida para o gerenciamento de pessoas, com funcionalidades avançadas de cálculos de regras de negócio (salário e idade) e documentação automatizada. O projeto utiliza **Java 17** e **Spring Boot 3.2.1**.

## 🚀 Tecnologias e Arquitetura 
* **Java 17 & Spring Boot 3**
* **Maven 3.3.1**
* **Spring Data JPA**: Para persistência de dados.
* **PostgreSQL**: Banco de dados principal utilizado em produção.
* **H2 Database**: Utilizado para execução de testes automatizados e memoria.
* **Lombok**: Para redução de código boilerplate.
* **Springdoc OpenAPI**: Documentação interativa (Swagger).
* **Docker**: Containerização do banco de dados.

---

## ⚙️ Modos de Execução (Arquitetura Hexagonal)

A aplicação foi projetada utilizando **Portas e Adaptadores (Hexagonal/DDD)**. Isso permite trocar a persistência sem alterar nenhuma regra de negócio.

### Baixar as dependências
```bash
  mvn clean install
```

### 1. Modo Padrão (In-Memory) - **Recomendado para Avaliação**
Atende estritamente aos requisitos do desafio (Mapa em memória, sem dependência externa).
Basta rodar:

```bash
  mvn spring-boot:run
```
Neste modo, a aplicação inicia pré-populada com 3 registros conforme solicitado.

### 2. Modo Produção (PostgreSQL + Docker) - Diferencial
O projeto já disponibiliza um arquivo de configuração para o banco de dados na pasta `local`. Certifique-se de ter o Docker instalado e execute:

```bash
  docker-compose -f local/docker-compose.yml up -d
  
```

Isso iniciará um container PostgreSQL configurado conforme as propriedades da aplicação.

### 3. Executar a Aplicação
Com o banco rodando, execute a aplicação através da sua IDE ou via terminal com o profiles = prod.

```bash
  mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### ## 3. Ajuste no `application.properties`

Para o modo prod não quebrar tentando conectar num banco que não existe, precisamos dizer ao Spring para o configurar o Datasource se estiver no profile prod.

no `application.properties` tire os comentarios relacionado ao postgresql da linha 4 a linha 6 e linha 8 e comente as linhas relacionadas ao H2 da linha 10 a 13.

### 4. Documentação e Testes Manuais
Swagger UI: http://localhost:8080/swagger-ui.html.

🧪 Testes Automatizados
O projeto possui cobertura de testes para o domínio, mapeadores e casos de uso. Para executá-los:

```bash
  ./mvnw test
```
