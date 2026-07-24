# LedgerOS - Multi-Lambda (Firebase Functions-style)

Este repositório está estruturado no modelo **Function-per-Route / Multi-Lambda**, similar ao **Firebase Functions** ou **Vercel Serverless Functions**.

Cada função Lambda é uma classe Java independente com sua própria responsabilidade, compartilhando o mesmo artefato JAR e recursos comuns (`models`, `shared`).

---

## 📁 Estrutura de Funções Independente

```
backend/
├── pom.xml
├── template.yaml                            # Mapeia cada função Lambda independente na AWS
└── src/main/java/com/ledgeros/lambda/
    ├── functions/                           # Cada arquivo é uma Lambda Function individual
    │   ├── HealthCheckFunction.java         # Lambda /healthcheck
    │   ├── HealthCheck2Function.java        # Lambda /healthcheck2
    │   └── CreateTransactionFunction.java   # Lambda POST /transactions
    ├── model/
    │   ├── ApiResponse.java                 # Record Wrapper genérico
    │   └── TransactionRecord.java           # Record de dados
    └── shared/
        └── infrastructure/config/           # Interfaces e utilitários compartilhados
```

---

## 🚀 Mapeamento no `template.yaml` (AWS SAM)

No AWS SAM / CloudFormation, declaramos cada função separadamente com seu próprio `Handler`:

- `com.ledgeros.lambda.functions.HealthCheckFunction::handleRequest` ➡️ `GET /healthcheck`
- `com.ledgeros.lambda.functions.HealthCheck2Function::handleRequest` ➡️ `GET /healthcheck2`
- `com.ledgeros.lambda.functions.CreateTransactionFunction::handleRequest` ➡️ `POST /transactions`

---

## 💡 Vantagens deste Modelo

1. **Isolamento Total**: Cada função roda em seu próprio ambiente Lambda na AWS com limites de memória e timeout independentes.
2. **Estilo Firebase Functions**: Basta criar uma nova classe no pacote `functions/` e registrar no `template.yaml`.
3. **Reaproveitamento de Código**: Todas as funções compartilham os mesmos DTOs/Records e utilitários de infraestrutura.
