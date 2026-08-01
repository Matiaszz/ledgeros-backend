# 🚀 LedgerOS - Backend API

O backend do **LedgerOS** é uma API REST Serverless construída em Java 21 utilizando a infraestrutura da AWS. O projeto adota uma arquitetura modularizada no modelo **Function-per-Route / Multi-Lambda**, estruturada seguindo os princípios de **Clean Architecture** (Arquitetura Limpa).

---

## 🏛️ Decisões Arquiteturais e Técnicas

### 1. Modelo Multi-Lambda (Function-per-Route)
Em vez de utilizar uma única função Lambda servindo um monólito (ex: Spring Boot com Serverless Java Container), o projeto utiliza uma abordagem **Function-per-Route** onde cada endpoint da API é atendido por uma função Lambda Java dedicada.

- **Menor Cold Start**: Instanciação direta sem o overhead da inicialização de um framework pesado como Spring Boot.
- **Isolamento de Falhas e Desempenho**: Falhas em um endpoint não afetam outros. Cada função possui configurações independentes de memória, timeout e limites de concorrência.
- **Princípio do Menor Privilégio (IAM)**: Políticas de acesso ao DynamoDB e Secrets Manager especificadas por Lambda no `template.yaml`.

### 2. Clean Architecture (Arquitetura Limpa)
O código está organizado em camadas isoladas para manter a regra de negócio desacoplada de infraestrutura e SDKs da AWS:

- `presentation`: Contém os Handlers Lambda (`APIGatewayProxyRequestEvent -> APIGatewayProxyResponseEvent`), DTOs de Request e Response.
- `application`: Implementa os Use Cases da aplicação (`LoginUseCase`, `RegisterUseCase`, etc.), encapsulando a lógica de negócio pura.
- `domain`: Modelos de domínio (`User`, `RefreshToken`) e contratos de repositório (`UserRepository`, `RefreshTokenRepository`).
- `infrastructure`: Tratamento customizado de exceções (`LambdaException`, `ExceptionCode`).
- `shared`: Utilitários reutilizáveis, provedores DynamoDB, manipulação de JWT, parsers e o `LambdaWrapper`.

### 3. Autenticação & Autorização Serverless
- **Custom Authorizer no API Gateway**: Rotas protegidas utilizam uma Lambda dedicada (`AuthorizationLambda`) configurada como autorizador customizado no API Gateway. O token JWT é validado antes mesmo da requisição atingir a função de destino.
- **AWS Secrets Manager**: A chave secreta usada para assinar e validar tokens JWT é obtida com segurança do AWS Secrets Manager.
- **Refresh Token com TTL no DynamoDB**: Tokens de atualização são armazenados na tabela `ledgeros-{stage}-refresh-tokens` e contam com a funcionalidade nativa de **TTL (Time to Live)** do DynamoDB para expiração e remoção automática.

### 4. Wrapper Unificado (`LambdaWrapper`) & Padronização de Respostas
Todas as funções utilizam a classe utilitária `LambdaWrapper.execute(...)`, garantindo:
- Padronização no formato de saída (`ApiResponse<T>`) com campos `status`, `timestamp`, `data` e `code`.
- Captura centralizada de exceções de negócio (`LambdaException`) com mapeamento correto de status HTTP e logs estruturados.

---

## 🛠️ Tecnologias e Ferramentas

- **Linguagem**: Java 21 (LTS)
- **Framework de IaC**: AWS SAM (Serverless Application Model) / CloudFormation
- **Serviços AWS**: AWS Lambda, API Gateway, DynamoDB, Secrets Manager
- **Outras Bibliotecas**: Jackson (JSON), Lombok, SLF4J

---

## 📁 Estrutura do Projeto

```
backend/
├── template.yaml                                    # Infraestrutura como Código (AWS SAM)
├── env.json                                         # Variáveis de ambiente para execução local
├── pom.xml                                          # Dependências Maven
└── src/main/java/com/ledgeros/
    ├── application/                                 # Casos de Uso (Lógica de Negócio)
    ├── domain/                                      # Entidades de Domínio e Interfaces de Repositório
    ├── infrastructure/                              # Exceções e Código de Infraestrutura
    ├── presentation/                                # Handlers Lambda e DTOs (Request/Response)
    └── shared/                                      # Configurações DynamoDB, JWT, Wrappers e Utilitários
```

---

## 💻 Como Rodar o Projeto Localmente

### 📋 Pré-requisitos

1. **Java 21 JDK** instalado e configurado nas variáveis de ambiente.
2. **Apache Maven 3.8+** instalado.
3. **Docker Desktop** rodando na sua máquina (necessário para o AWS SAM executar contêineres Lambda locais).
4. **AWS SAM CLI** instalado ([Guia de Instalação](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)).
5. (Opcional) **AWS CLI** configurado com credenciais para simular acesso ao DynamoDB / Secrets Manager ou um DynamoDB Local via Docker.

---

### 🔧 Passo a Passo para Execução

#### 1. Clonar o repositório e compilar o código Java
```bash
cd backend
mvn clean package
```

#### 2. Compilar o modelo SAM
```bash
sam build
```

#### 3. Configurar as Variáveis de Ambiente Locais
Verifique o arquivo `env.json` na raiz do módulo `backend`. Ajuste os nomes das tabelas DynamoDB e segredos conforme a sua necessidade de testes locais.

#### 4. Iniciar o API Gateway Localmente
Rode o comando abaixo para subir o servidor HTTP local na porta `3000`:
```bash
sam local start-api -n env.json
```

O API Gateway local estará acessível em: `http://127.0.0.1:3000`

---

### 🧪 Testando os Endpoints

Você pode enviar uma requisição `GET` para testar o Healthcheck público:
```bash
curl http://127.0.0.1:3000/healthcheck
```

**Resposta esperada:**
```json
{
  "status": "SUCCESS",
  "timestamp": "2026-08-01T18:00:00Z",
  "data": "Service is healthy",
  "code": null
}
```
