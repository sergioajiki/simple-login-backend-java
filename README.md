# **Simple Login Backend Java API**
Este repositório contém um projeto de API desenvolvido em Java utilizando Spring Boot. A API oferece funcionalidades de gerenciamento de usuários, autenticação usando Spring Security e JWT, e documentação interativa com o Swagger.

### **Pré-requisitos**
Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

* Java Development Kit (JDK) 11 ou superior
* Maven
* Docker
* Docker Compose
  
### Configuração do Banco de Dados
Certifique-se de que o Docker e o Docker Compose estão instalados. Execute o seguinte comando para iniciar o banco de dados MySQL:

> docker-compose up -d
> O MySQL estará disponível em localhost:3306 com o usuário "root" e a senha "root".


### Executando a Aplicação
1. Clone o repositório:
> git clone https://github.com/sergioajiki/simple-login-backend-java.git

2. Navegue até o diretório do projeto:
> cd simple-login-backend-java

3. Execute a aplicação usando o Maven:
>mvn spring-boot:run

A aplicação será iniciada em http://localhost:8080.

### **Endpoints da API**

## **Usuários**
**GET /users**: Retorna todos os usuários cadastrados.

**POST /users**: Cria um novo usuário. Envie os dados do usuário no corpo da requisição.

**POST /users/username**: Recupera um usuário pelo nome de usuário. Envie o nome de usuário no corpo da requisição.

**POST /users/email**: Recupera um usuário pelo endereço de e-mail. Envie o e-mail no corpo da requisição.

**GET /users/{id}**: Recupera um usuário pelo ID.

**DELETE /users/{id}**: Apaga um usuário pelo ID.

## **Autenticação**
**POST /auth/login**: Realiza o login do usuário. Envie as credenciais (nome de usuário e senha) no corpo da requisição. Retorna um token JWT para autenticação subsequente.
## **Documentação Swagger**
**/swagger-ui/index.html**: Acesse a documentação interativa da API usando o Swagger.
## **Desenvolvimento**
O projeto foi desenvolvido em Java, utilizando Spring Boot, Spring Security, JWT para autenticação e MySQL para armazenamento de dados. O Swagger foi integrado para facilitar a interação e teste da API.
