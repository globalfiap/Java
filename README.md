# EcoDrive

### Descrição do Projeto
EcoDrive é uma aplicação móvel desenvolvida para otimizar a experiência de uso de veículos elétricos, facilitando o acesso às estações de carregamento, promovendo o uso de energias renováveis e proporcionando uma experiência de carregamento eficiente e sustentável. A aplicação foi desenvolvida em Java, utilizando Spring Boot, com integrações para APIs RESTful e um sistema completo de CRUD, abrangendo diversas funcionalidades para melhorar a infraestrutura de mobilidade elétrica.

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

#### Estações de Recarga
- `GET /estacoes` - Listar todas as estações de recarga
- `POST /estacoes` - Adicionar uma nova estação de recarga
- `GET /estacoes/{id}` - Obter detalhes de uma estação específica
- `PUT /estacoes/{id}` - Atualizar uma estação de recarga
- `DELETE /estacoes/{id}` - Remover uma estação de recarga

#### Usuários
- `GET /usuarios` - Listar todos os usuários
- `POST /usuarios` - Adicionar um novo usuário
- `GET /usuarios/{id}` - Obter detalhes de um usuário específico
- `PUT /usuarios/{id}` - Atualizar um usuário
- `DELETE /usuarios/{id}` - Remover um usuário

#### Veículos
- `GET /veiculos` - Listar todos os veículos
- `POST /veiculos` - Adicionar um novo veículo
- `GET /veiculos/{id}` - Obter detalhes de um veículo específico
- `PUT /veiculos/{id}` - Atualizar um veículo
- `DELETE /veiculos/{id}` - Remover um veículo

#### Concessionárias
- `GET /concessionarias` - Listar todas as concessionárias
- `POST /concessionarias` - Adicionar uma nova concessionária
- `GET /concessionarias/{id}` - Obter detalhes de uma concessionária específica
- `PUT /concessionarias/{id}` - Atualizar uma concessionária
- `DELETE /concessionarias/{id}` - Remover uma concessionária

#### Estações Sustentáveis
- `GET /estacoes-sustentaveis` - Listar todas as estações sustentáveis
- `POST /estacoes-sustentaveis` - Adicionar uma nova estação sustentável
- `GET /estacoes-sustentaveis/{id}` - Obter detalhes de uma estação sustentável específica
- `PUT /estacoes-sustentaveis/{id}` - Atualizar uma estação sustentável
- `DELETE /estacoes-sustentaveis/{id}` - Remover uma estação sustentável

#### Reservas
- `GET /reservas` - Listar todas as reservas
- `POST /reservas` - Adicionar uma nova reserva
- `GET /reservas/{id}` - Obter detalhes de uma reserva específica
- `PUT /reservas/{id}` - Atualizar uma reserva
- `DELETE /reservas/{id}` - Cancelar uma reserva

#### Gastos de Carregamento
- `GET /gastos-carregamento` - Listar todos os registros de gastos de carregamento
- `POST /gastos-carregamento` - Adicionar um novo registro de gasto de carregamento
- `GET /gastos-carregamento/{id}` - Obter detalhes de um gasto de carregamento específico
- `PUT /gastos-carregamento/{id}` - Atualizar um gasto de carregamento
- `DELETE /gastos-carregamento/{id}` - Remover um registro de gasto de carregamento

#### Histórico de Carregamento
- `GET /historico-carregamento` - Listar todo o histórico de carregamento
- `POST /historico-carregamento` - Adicionar um novo registro ao histórico de carregamento
- `GET /historico-carregamento/{id}` - Obter detalhes de um registro de histórico de carregamento específico
- `PUT /historico-carregamento/{id}` - Atualizar um registro de histórico de carregamento
- `DELETE /historico-carregamento/{id}` - Remover um registro do histórico de carregamento

#### Fontes de Energia
- `GET /fontes-energia` - Listar todas as fontes de energia
- `POST /fontes-energia` - Adicionar uma nova fonte de energia
- `GET /fontes-energia/{id}` - Obter detalhes de uma fonte de energia específica
- `PUT /fontes-energia/{id}` - Atualizar uma fonte de energia
- `DELETE /fontes-energia/{id}` - Remover uma fonte de energia

### Configurações Adicionais
- **Banco de Dados**: Configure o arquivo `application.properties` com as credenciais e URL do banco de dados para que a aplicação possa se conectar corretamente.

- **Swagger**: Para acessar a documentação da API, certifique-se de que o Swagger está ativado no arquivo de configuração.





