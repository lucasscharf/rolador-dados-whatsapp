# Rolador de Dados para WhatsApp

Bot de WhatsApp que rola dados de RPG. Ao receber a mensagem `roll Xd6` (com X entre 1 e 99), responde no mesmo chat com os valores sorteados e o total:

```
Você:  roll 3d6
Bot:   🎲 3d6: 4, 2, 6
```

Se a mensagem começa com `roll` mas é inválida (ex.: `roll 200d6`), o bot responde com a sintaxe correta. Qualquer outra mensagem é ignorada.

A integração com o WhatsApp é feita pela [Evolution API](https://github.com/evolution-foundation/evolution-api) (v2): o bot recebe mensagens via webhook (`MESSAGES_UPSERT`) e responde pelo endpoint `POST /message/sendText/{instance}`.

## Stack

- Java 21, Spring Boot 4.1 (WebMVC + RestClient), Maven (wrapper incluso)
- Evolution API v2 + Postgres + Redis via Docker Compose

## Arquitetura

```
WhatsApp -> Evolution API -- webhook MESSAGES_UPSERT --> POST /webhook (bot)
                                                              |
WhatsApp <- Evolution API <-- POST /message/sendText/{i} <-- resposta
```

Pacotes:

- `dice`: `DiceCommandParser` (interpreta `roll Xd6`), `DiceRoller` (sorteia valores 1 a 6), `MessageFormatter` (monta as respostas)
- `webhook`: `WebhookController` (recebe o webhook, filtra eventos, `fromMe`, mensagens sem texto) e `MessageHandlerImpl` (orquestra o fluxo)
- `evolution`: `EvolutionApiClientImpl` (envia textos pela Evolution API)

## Configuração

Variáveis de ambiente (com os defaults de `application.yml`):

| Variável             | Default                 | Descrição                          |
| -------------------- | ----------------------- | ---------------------------------- |
| `EVOLUTION_BASE_URL` | `http://localhost:8081` | URL base da Evolution API          |
| `EVOLUTION_API_KEY`  | `change-me`             | API key global da Evolution API    |
| `EVOLUTION_INSTANCE` | `bot`                   | Nome da instância usada para envio |

## Rodando os testes

```bash
./mvnw test
```

## Subindo tudo com Docker Compose

```bash
export EVOLUTION_API_KEY="uma-chave-secreta"
docker compose up -d --build
```

Sobe o bot (porta 8080), a Evolution API (porta 8081), Postgres e Redis.

### 1. Criar a instância do WhatsApp

```bash
curl -X POST http://localhost:8081/instance/create \
  -H "apikey: $EVOLUTION_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{"instanceName": "bot", "qrcode": true, "integration": "WHATSAPP-BAILEYS"}'
```

### 2. Conectar via QR code

Abra o manager em `http://localhost:8081/manager` (login com a API key) e escaneie o QR code da instância `bot` com o WhatsApp do número que será o bot. Alternativa por API: `GET /instance/connect/bot` retorna o QR em base64.

### 3. Configurar o webhook da instância

```bash
curl -X POST http://localhost:8081/webhook/set/bot \
  -H "apikey: $EVOLUTION_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "webhook": {
      "enabled": true,
      "url": "http://bot:8080/webhook",
      "events": ["MESSAGES_UPSERT"],
      "byEvents": false,
      "base64": false
    }
  }'
```

O hostname `bot` resolve dentro da rede do Compose. O controller também aceita `POST /webhook/messages-upsert`, caso `byEvents` esteja habilitado.

### 4. Testar

Mande `roll 3d6` para o número conectado e aguarde a resposta.

## Rodando só o bot (sem Docker)

Com uma Evolution API já disponível em algum lugar:

```bash
EVOLUTION_BASE_URL=https://minha-evolution.example.com \
EVOLUTION_API_KEY=minha-chave \
EVOLUTION_INSTANCE=bot \
./mvnw spring-boot:run
```

Para simular o webhook localmente:

```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "event": "messages.upsert",
    "instance": "bot",
    "data": {
      "key": {"remoteJid": "5511999999999@s.whatsapp.net", "fromMe": false, "id": "ABC123"},
      "pushName": "João",
      "message": {"conversation": "roll 3d6"},
      "messageType": "conversation"
    }
  }'
```
