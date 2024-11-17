# EcoDrive

### Descrição do Projeto
EcoDrive é um projeto desenvolvido para gerenciar diferentes aspectos de uma infraestrutura de mobilidade elétrica. Esta aplicação tem como objetivo fornecer soluções para gerenciar estações de recarga de veículos elétricos, reservas de estações, usuários, consumo de energia e muito mais. O projeto foi desenvolvido em Java, utilizando Spring Boot, com integrações para APIs RESTful e um sistema completo de CRUD.

### Estrutura do Projeto
O projeto está organizado da seguinte forma:

- **.gitattributes e .gitignore**: Arquivos utilizados para configuração do Git, definindo padrões para gerenciar o controle de versão.

- **mvnw e mvnw.cmd**: Scripts para executar o Maven Wrapper, facilitando o gerenciamento de dependências do projeto.

- **pom.xml**: Arquivo de configuração do Maven, onde estão definidas todas as dependências do projeto.

- **src/main/java**: Diretório principal do código-fonte.
  - **config**: Contém classes de configuração como `ModelMapperConfig.java` e `SwaggerConfig.java` para mapeamento de objetos e documentação da API respectivamente.
  - **controller**: Contém as classes de controle que gerenciam as requisições dos usuários, como `BairroController`, `ConcessionariaController`, `UsuarioController`, entre outros.
  - **dto**: Contém objetos de transferência de dados (DTOs) para facilitar a comunicação entre a camada de controle e a camada de serviço.
  - **model**: Contém as classes de modelo que representam as entidades do banco de dados, como `Bairro`, `Concessionaria`, `Veiculo`, etc.
  - **repository**: Contém interfaces que extendem `JpaRepository`, permitindo o acesso ao banco de dados para realizar operações de CRUD.
  - **service**: Contém as classes de serviço, onde é implementada a lógica de negócios para cada funcionalidade, como `BairroService`, `UsuarioService`, etc.

- **src/main/resources**: Diretório que contém arquivos de configuração do projeto, incluindo `application.properties`, onde são definidos parâmetros de configuração como porta do servidor, banco de dados, etc.

- **src/test/java**: Contém classes para realizar testes unitários e de integração da aplicação.

### Tecnologias Utilizadas
- **Java 11**
- **Spring Boot**
- **Maven**
- **JPA/Hibernate**
- **Swagger** para documentação da API
- **ModelMapper** para facilitação de mapeamento de objetos

### Funcionalidades Principais
- **Gestão de Estações de Recarga**: Permite gerenciar informações relacionadas às estações de recarga para veículos elétricos.
- **Gestão de Reservas**: Usuários podem reservar estações de recarga através do sistema.
- **Gestão de Veículos**: Registro de veículos e o acompanhamento do consumo de energia.
- **Documentação com Swagger**: Documentação detalhada da API RESTful para facilitar a integração.
- **Controle de Usuários**: CRUD completo para gerenciar usuários do sistema.

### Como Executar o Projeto
1. **Clonar o Repositório**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```
2. **Entrar no Diretório do Projeto**
   ```bash
   cd Java-main
   ```
3. **Executar o Maven Wrapper para Baixar as Dependências**
   ```bash
   ./mvnw clean install
   ```
4. **Rodar a Aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```
5. **Acessar a Documentação da API**
   Abra o navegador e acesse `http://localhost:8080/swagger-ui.html` para visualizar a documentação gerada pelo Swagger.

### Endpoints Principais
- **Estações de Recarga**
  - `GET /estacoes` - Listar todas as estações
  - `POST /estacoes` - Adicionar uma nova estação de recarga
  - `PUT /estacoes/{id}` - Atualizar uma estação
  - `DELETE /estacoes/{id}` - Remover uma estação

- **Usuários**
  - `GET /usuarios` - Listar todos os usuários
  - `POST /usuarios` - Adicionar um novo usuário
  - `PUT /usuarios/{id}` - Atualizar um usuário
  - `DELETE /usuarios/{id}` - Remover um usuário

### Configurações Adicionais
- **Banco de Dados**: Configure o arquivo `application.properties` com as credenciais e URL do banco de dados para que a aplicação possa se conectar corretamente.

- **Swagger**: Para acessar a documentação da API, certifique-se de que o Swagger está ativado no arquivo de configuração.

### Contribuição
1. **Fork o Repositório**
2. **Crie um Branch para Sua Feature** (`git checkout -b minha-feature`)
3. **Faça Commit de Suas Modificações** (`git commit -m 'Adiciona minha feature'`)
4. **Faça Push para o Branch** (`git push origin minha-feature`)
5. **Abra um Pull Request**


