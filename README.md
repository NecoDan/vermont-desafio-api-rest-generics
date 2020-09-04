# API RESTFul WebService - Pessoas - © Vermont.
  Projeto em Spring Boot de uma construção API RESTFul voltado a atender o desafio da Vermont <link>http://www.vermont.com.br/.
   
  Uma solução criada em Java em formato de API REST que atenda aos requisitos para a recepção e/ou criação de pessoas contendo os seus respectivos dados. Onde todos os serviços devem trabalhar com JSON em suas chamadas e retornos.
  
 #### Stack do projeto
  - Escrito em Java 8;
  - Utilizando as facilidades e recursos framework Spring Boot;
  - Lombok na classes para evitar o boilerplate do Java;
  - Framework Hibernarte e Spring Data JPA para garantir a persistência dos dados e facilitar as operações CRUD (aumentando o nivel de desempenho e escalabilidade);
  - Boas práticas de programação, utilizando Design Patterns (Builder);
  - Testes unitários, testes automatizados (Junit, Mocks, WebMocks);
  - Banco de dados PostgreSQL;
  - Docker utilizando compose;
  
  #### Visão Geral
  
  A aplicaçao tem como objetivo disponibilizar endpoints para consulta de informações e operações à respeito de:
  - Pessoas, com os seus respectivos dados, tais como: 
  
    - Nome;
    - Endereço;
    - CPF;
    - Email;
    - Data de nascimento;
    - Naturalidade;
    - Nacionalidade;
    - Entre outros 
  
  #### Instruções Inicialização - Projeto
    
      1. Clone o repositório git@github.com:NecoDan/vermont-desafio-api-rest-generics.git
      
      2. Ou faça o download do arquivo ZIP do projeto em https://github.com/NecoDan/vermont-desafio-api-rest-generics
          
      3. Importar o projeto em sua IDE de preferência (lembre-se, projeto baseado em Spring & Maven)
      
      4. Buildar o projeto e executá-lo.
    
  #### Instruções Inicialização - Database
  
 O comando ```docker-compose up``` inicializará uma instância do Postgres 9.3, nesse momento será criado um novo schema denominado ```vermont_services``` , assim como, suas respectivas tabelas no database default ```postgres```. 
 <br><br>Com a finalidade de gerenciar todas as operações relacionadas as pessoas, com informações necessárias para a demonstração do projeto. Em seguida a aplicação de ```vermont-desafio-api-rest-generics``` pode ser executada e inicializada.
 
 #### Instruções Inicialização - Testes
 
 Ao executar a classe ```AppTests``` no escopo de testes, irá ser executado uma bateria de testes, usando banco em memória. Efetuando de forma automatizada todas as requisições a partir dos métodos
 ```GET, POST, PUT & DELETE``` por meio da classe ```Controller de Pessoas``` específica.
 
  #### Endpoints: 
  
  Utilizando a ferramenta de documentação de endpoints ```Swagger```, pode-se visualizar todos os endpoints disponíveis. Basta acessar a documentação da API por meio da URL <link>http://localhost:8080/swagger-ui.html , logo após a sua inicialização. <br><br> 
  De sorte que, segue a lista de alguns endpoints para conhecimento: 
  
  - Executa e retorna a página web escpecifica contendo o repósitório do projeto no GitHub:
    - `http://localhost:8080/sources/`
  
  - Retornar uma lista completa de pessoas páginável em formato (JSON):
    - `http://localhost:8080/pessoas/`
  
 Entre outros, aos quais podem ser identificados no endereço fornecido pelo Swagger: <link>http://localhost:8080/swagger-ui.html.


 #### Autor e mantenedor do projeto
 - Daniel Santos Gonçalves - Bachelor in Information Systems, Federal Institute of Maranhão - IFMA / Software Developer Fullstack.
 - GitHub: https://github.com/NecoDan
 
 - Linkedin: <link>https://www.linkedin.com/in/daniel-santos-bb072321 
 - Twiter: <link>https://twitter.com/necodaniel.
