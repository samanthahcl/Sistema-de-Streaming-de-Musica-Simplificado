# 🎵 Sistema de Streaming de Música
### Spotify Simplificado — Padrões de Projeto GoF em Java

> Projeto educacional que simula o backend de uma plataforma de streaming,
> demonstrando a aplicação prática dos padrões de projeto **Factory Method**,
> **Strategy** e **Observer** do catálogo GoF (Gang of Four).

---

## Sumário

1. [Visão Geral](#visão-geral)
2. [Estrutura do Projeto](#estrutura-do-projeto)
3. [Padrões de Projeto](#padrões-de-projeto)
    - [Factory Method](#1-factory-method)
    - [Strategy](#2-strategy)
    - [Observer](#3-observer)
4. [Diagrama de Classes](#diagrama-de-classes)
5. [Descrição dos Arquivos](#descrição-dos-arquivos)
6. [Como Executar](#como-executar)
7. [Saída Esperada](#saída-esperada)
8. [Conceitos Aplicados](#conceitos-aplicados)
9. [Possíveis Extensões](#possíveis-extensões)

---

## Visão Geral

O sistema simula as operações centrais de uma plataforma de streaming:

| Funcionalidade | Tecnologia / Padrão |
|---|---|
| Criação de músicas e podcasts | **Factory Method** |
| Reprodução sequencial e aleatória | **Strategy** |
| Notificação de faixas em reprodução | **Observer** |
| Gerenciamento de playlist | `Player` |
| Histórico com timestamp | `HistoryService` |

**Requisitos:** Java 21+ (usa Records e Text Blocks)

---

## Estrutura do Projeto

```
spotify/
└── src/
    └── main/
        └── java/
            └── com/
                └── spotify/
                    ├── model/
                    │   ├── Content.java          ← Classe abstrata base (Product)
                    │   ├── Music.java            ← Produto concreto: música
                    │   └── Podcast.java          ← Produto concreto: podcast
                    │
                    ├── factory/
                    │   ├── ContentCreator.java   ← Creator abstrato (Factory Method)
                    │   ├── MusicCreator.java     ← ConcreteCreator para Music
                    │   └── PodcastCreator.java   ← ConcreteCreator para Podcast
                    │
                    ├── strategy/
                    │   ├── PlaybackMode.java     ← Interface Strategy
                    │   ├── SequentialMode.java   ← Estratégia sequencial
                    │   └── RandomMode.java       ← Estratégia aleatória
                    │
                    ├── observer/
                    │   ├── TrackObserver.java    ← Interface Observer
                    │   ├── Player.java           ← Subject + Context
                    │   ├── UIObserver.java       ← Observador: interface gráfica
                    │   └── HistoryService.java   ← Observador: histórico
                    │
                    └── main/
                        └── Main.java             ← Simulação completa
```

---

## Padrões de Projeto

### 1. Factory Method

**Pacote:** `com.spotify.factory`

#### O que é?
O padrão **Factory Method** define uma classe criadora abstrata que declara um método de fábrica (`factoryMethod()`), deixando as subclasses concretas decidirem qual classe instanciar. Isso separa completamente a lógica de *uso* do objeto da lógica de *criação*.

#### Por que foi usado?
Sem o padrão, o código cliente precisaria conhecer os construtores de `Music` e `Podcast` e replicar a lógica de validação em vários lugares. Com ele:

- A **validação comum** (título, autor, duração) fica centralizada no `ContentCreator` abstrato.
- A **validação específica** de cada tipo (ex.: número de episódio do Podcast) fica isolada no creator concreto.
- Adicionar um novo tipo (ex.: `Audiobook`) exige apenas criar a classe modelo e um novo `AudiobookCreator` — sem alterar nenhum código existente.
- O cliente opera sempre com o tipo abstrato `ContentCreator`, nunca com `Music` ou `Podcast` diretamente.

#### Como foi implementado?

```java
// Criação via Creator — o cliente nunca usa "new Music(...)" diretamente
ContentCreator creator = new MusicCreator("Bohemian Rhapsody", "Queen", 354, "Rock", "A Night at the Opera");
Content music = creator.createContent(); // chama factoryMethod() internamente

ContentCreator podcastCreator = new PodcastCreator("IA em 2025", "Lex Fridman", 4500, "Lex Podcast", 412, "Descrição");
Content podcast = podcastCreator.createContent();
```

**Hierarquia GoF:**

```
ContentCreator (Creator abstrato)
├── MusicCreator   → factoryMethod() → new Music(...)
└── PodcastCreator → factoryMethod() → new Podcast(...)

Content (Product abstrato)
├── Music    ← criado por MusicCreator.factoryMethod()
└── Podcast  ← criado por PodcastCreator.factoryMethod()
```

#### Fluxo de criação

```
Main
 └── new MusicCreator(params)          ← instancia o Creator concreto
      └── .createContent()             ← método template no Creator abstrato
           ├── factoryMethod()         ← implementado por MusicCreator → new Music(...)
           └── validateContent(music)  ← validação centralizada no Creator abstrato
```

#### Benefício do Princípio Aberto/Fechado (OCP)
> "Aberto para extensão, fechado para modificação."
> Para adicionar `Audiobook`, basta criar `Audiobook extends Content` e `AudiobookCreator extends ContentCreator`.

---

### 2. Strategy

**Pacote:** `com.spotify.strategy`

#### O que é?
O padrão **Strategy** define uma família de algoritmos, encapsula cada um deles e os torna intercambiáveis. O algoritmo pode variar independentemente dos clientes que o utilizam.

#### Por que foi usado?
O comportamento do botão "Avançar" muda conforme o modo de reprodução. Sem Strategy, o `Player` teria uma estrutura condicional como:

```java
// ❌ Sem Strategy — frágil e difícil de expandir
if (mode.equals("SEQUENTIAL")) {
    currentIndex = (currentIndex + 1) % playlist.size();
} else if (mode.equals("RANDOM")) {
    currentIndex = random.nextInt(playlist.size());
}
// e se adicionar "REPEAT_ONE"? Modifica o Player inteiro.
```

Com Strategy:

```java
// ✅ Com Strategy — delega ao objeto correto, sem if/else no Player
currentIndex = playbackMode.next(playlist, currentIndex);
```

#### Como foi implementado?

| Classe | Papel |
|---|---|
| `PlaybackMode` | Interface Strategy — define o contrato `next()` |
| `SequentialMode` | Estratégia concreta — avança em ordem circular |
| `RandomMode` | Estratégia concreta — sorteia índice diferente do atual |
| `Player` | Contexto — mantém referência e delega a chamada |

#### Troca em tempo de execução

```java
Player player = new Player();               // SequentialMode é o padrão

player.play();                              // usa SequentialMode
player.next();                              // ainda sequencial

player.setPlaybackMode(new RandomMode());   // troca sem modificar o Player!
player.next();                              // agora usa RandomMode
```

---

### 3. Observer

**Pacote:** `com.spotify.observer`

#### O que é?
O padrão **Observer** define uma dependência um-para-muitos entre objetos, de modo que quando um objeto (Subject) muda de estado, todos os seus dependentes (Observers) são notificados e atualizados automaticamente.

#### Por que foi usado?
Sem Observer, o `Player` precisaria chamar diretamente cada serviço:

```java
// ❌ Sem Observer — alto acoplamento
public void playCurrentTrack() {
    ui.update(current);          // Player conhece UIObserver
    history.register(current);   // Player conhece HistoryService
    // E se adicionar RecommendationService? Modifica o Player!
}
```

Com Observer:

```java
// ✅ Com Observer — o Player não sabe quem está escutando
private void notifyObservers(Content content) {
    for (TrackObserver observer : new ArrayList<>(observers)) {
        observer.onTrackStarted(content);  // publica o evento
    }
}
```

#### Como foi implementado?

| Classe | Papel |
|---|---|
| `TrackObserver` | Interface Observer — define `onTrackStarted(Content)` |
| `Player` | Subject — mantém lista de observers e notifica |
| `UIObserver` | Observer concreto — exibe card visual no console |
| `HistoryService` | Observer concreto — registra faixa com timestamp |

#### Gerenciamento dinâmico de observers

```java
player.addObserver(ui);       // registra (subscribe)
player.addObserver(history);  // registra (subscribe)

player.next();                // ambos são notificados

player.removeObserver(ui);    // remove dinamicamente (unsubscribe)
player.next();                // apenas HistoryService é notificado
```

---

## Diagrama de Classes

```
┌─────────────────────────────────────────────────────────────────────┐
│                          FACTORY METHOD                             │
│                                                                     │
│  ┌──────────────────────┐        ┌───────────────┐                  │
│  │   <<abstract>>       │        │  <<abstract>> │                  │
│  │   ContentCreator     │        │    Content    │                  │
│  │ + factoryMethod()*   │        │ + title       │                  │
│  │ + createContent()    │        │ + author      │                  │
│  └──────────┬───────────┘        │ + duration    │                  │
│             │ extends            └──────┬────────┘                  │
│    ┌────────┴─────────┐                │ extends                   │
│    │                  │         ┌──────┴──────┐  ┌──────────────┐  │
│  ┌─▼────────────┐  ┌──▼──────────────┐ Music  │  │   Podcast    │  │
│  │ MusicCreator │  │ PodcastCreator  │- genre │  │ - showName   │  │
│  │factoryMethod │  │ factoryMethod() │- album │  │ - episode    │  │
│  │→ new Music() │  │→ new Podcast()  └────────┘  │ - description│  │
│  └──────────────┘  └─────────────────            └──────────────┘  │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                             STRATEGY                                │
│                                                                     │
│  ┌─────────────────┐    ┌──────────────────┐                        │
│  │  <<interface>>  │◀───│  SequentialMode  │                        │
│  │  PlaybackMode   │    │  + next()        │                        │
│  │  + next()       │    └──────────────────┘                        │
│  │  + getModeName()│    ┌──────────────────┐                        │
│  └────────┬────────┘◀───│   RandomMode     │                        │
│           │             │  + next()        │                        │
│           │             └──────────────────┘                        │
│  ┌────────▼────────┐                                                │
│  │     Player      │  (Context — delega a PlaybackMode)             │
│  └─────────────────┘                                                │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                             OBSERVER                                │
│                                                                     │
│  ┌───────────────────────┐  notifica  ┌────────────────────────┐   │
│  │        Player         │───────────▶│    <<interface>>       │   │
│  │      (Subject)        │            │    TrackObserver        │   │
│  │ - observers: List     │            │ + onTrackStarted()     │   │
│  │ + addObserver()       │            └────────────┬───────────┘   │
│  │ + removeObserver()    │                         │               │
│  │ + notifyObservers()   │            ┌────────────┴────────────┐  │
│  └───────────────────────┘            │                         │  │
│                               ┌───────▼──────┐  ┌──────────────▼┐ │
│                               │  UIObserver  │  │HistoryService │ │
│                               │ (exibe card) │  │(registra log) │ │
│                               └──────────────┘  └───────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Descrição dos Arquivos

### `model/Content.java`
Classe abstrata que define o contrato comum para todos os tipos de conteúdo (**Product** abstrato do Factory Method). Possui os atributos `title`, `author` e `durationSeconds`, além do método utilitário `getFormattedDuration()` (retorna duração em `MM:SS`) e o método abstrato `getContentType()` que as subclasses devem implementar.

### `model/Music.java`
Estende `Content` adicionando `genre` (gênero musical) e `album`. Implementa `getContentType()` retornando `"Música"`. Instanciada exclusivamente por `MusicCreator`.

### `model/Podcast.java`
Estende `Content` adicionando `showName`, `episodeNumber` e `description`. Implementa `getContentType()` retornando `"Podcast"`. Instanciada exclusivamente por `PodcastCreator`.

### `factory/ContentCreator.java`
**Creator abstrato** do padrão Factory Method. Declara `factoryMethod()` como método abstrato e oferece `createContent()` como método template, que invoca `factoryMethod()` e aplica a validação comum (título, autor, duração) centralizada no método privado `validateContent()`.

### `factory/MusicCreator.java`
**ConcreteCreator** para músicas. Implementa `factoryMethod()` retornando `new Music(...)` com os parâmetros recebidos no construtor.

### `factory/PodcastCreator.java`
**ConcreteCreator** para podcasts. Implementa `factoryMethod()` com validação específica (número de episódio deve ser positivo) antes de retornar `new Podcast(...)`.

### `strategy/PlaybackMode.java`
Interface do **Strategy** com dois métodos: `next(List<Content>, int)` que calcula o próximo índice, e `getModeName()` que retorna o nome legível da estratégia.

### `strategy/SequentialMode.java`
Estratégia concreta que avança em ordem linear e circular (`(currentIndex + 1) % size`).

### `strategy/RandomMode.java`
Estratégia concreta que sorteia um índice aleatório, garantindo que seja diferente do atual. Aceita um `seed` para testes determinísticos.

### `observer/TrackObserver.java`
Interface do **Observer** com o método `onTrackStarted(Content content)`, chamado pelo `Player` sempre que uma faixa inicia a reprodução.

### `observer/Player.java`
**Subject** do Observer e **Context** do Strategy. Gerencia a playlist (`List<Content>`), a lista de observers e o modo de reprodução atual. O método `next()` delega ao `PlaybackMode` (Strategy) e em seguida chama `notifyObservers()` (Observer). A lista de observers é copiada antes de iterar para permitir remoção segura durante a notificação.

### `observer/UIObserver.java`
Observer concreto que simula a interface gráfica. Ao ser notificado, exibe um card formatado com título, artista, duração e tipo do conteúdo. Calcula a largura visual correta das linhas levando em conta que emojis ocupam 2 colunas no terminal.

### `observer/HistoryService.java`
Observer concreto que mantém um `List<HistoryEntry>` com timestamp de cada reprodução. Utiliza `record` do Java 21 para o tipo imutável `HistoryEntry`. Oferece `printHistory()` para listar o histórico completo e `getHistory()` para acesso somente-leitura.

### `main/Main.java`
Classe de simulação que demonstra todos os padrões em sequência:
1. Criação de conteúdos via `MusicCreator` / `PodcastCreator` (Factory Method)
2. Registro de observers no `Player` (Observer)
3. Reprodução em modo sequencial (Strategy — `SequentialMode`)
4. Troca de estratégia para modo aleatório em runtime (Strategy — `RandomMode`)
5. Remoção dinâmica de observer (Observer — `removeObserver`)
6. Exibição do histórico final (`HistoryService`)
7. Demonstração de validação da fábrica (erros esperados capturados por `try/catch`)

---

## Como Executar

### Compilação manual

```bash
# 1. Clone ou extraia o projeto
cd spotify/

# 2. Compile todos os fontes
find src -name "*.java" | xargs javac --release 21 -d out

# 3. Execute a simulação
java -cp out com.spotify.main.Main
```

### IntelliJ IDEA

1. Abra a pasta `spotify/` como projeto existente
2. Confirme que o SDK está configurado para **Java 21+** em `File > Project Structure`
3. Execute `Main.java` com o botão ▶

### Com Maven (opcional)

Adicione um `pom.xml` padrão e execute:

```bash
mvn compile exec:java -Dexec.mainClass="com.spotify.main.Main"
```

---

## Saída Esperada

```
╔══════════════════════════════════════════════════════════╗
║         🎵   SISTEMA DE STREAMING DE MÚSICA   🎵         ║
║               Padrões GoF — Java Simulation              ║
╠══════════════════════════════════════════════════════════╣
║   Padrões:  Factory Method  │  Strategy  │  Observer    ║
╚══════════════════════════════════════════════════════════╝

┌──────────────────────────────────────────────────────┐
│  1. Factory Method — Criando Conteúdos via Creators   │
└──────────────────────────────────────────────────────┘

  Conteúdos criados com sucesso:
  → [Música] "Bohemian Rhapsody" — Queen (05:54) | Álbum: A Night at the Opera | Gênero: Rock
  → [Podcast] "Inteligência Artificial em 2025" — Lex Fridman (75:00) | Show: Lex Fridman Podcast | Ep. 412 | Avanços recentes em LLMs e robótica

[ ... demais conteúdos e playlist ... ]

  ▶  [1/5] via modo Sequencial 🔁

  ╔════════════════════════════════════════════════╗
  ║ 🖥  Spotify Simplificado — Tocando agora       ║
  ╠════════════════════════════════════════════════╣
  ║ 🎵  Bohemian Rhapsody                          ║
  ║ 👤  Queen                                      ║
  ║ ⏱  05:54                                       ║
  ║ 🏷  Música                                     ║
  ╚════════════════════════════════════════════════╝
  📋 [HistoryService] Registrado: "Bohemian Rhapsody" às 19:16:56

[ ... demais reproduções e troca de modo ... ]

📖 Histórico de Reprodução (6 faixa(s)):
  [28/05/2026 19:16:56]  Bohemian Rhapsody               Queen               (Música)
  [28/05/2026 19:16:56]  Inteligência Artificial em 2025  Lex Fridman         (Podcast)
  ...

┌──────────────────────────────────────────────────────┐
│  7. Factory Method — Demonstrando Validação           │
└──────────────────────────────────────────────────────┘
  ❌ Erro esperado (título vazio): Título não pode ser vazio.
  ❌ Erro esperado (episódio negativo): Número do episódio deve ser positivo. Recebido: -1
  ❌ Erro esperado (duração zero): Duração deve ser maior que zero.
```

> Os erros da seção 7 são **intencionais** — demonstram que a validação da fábrica funciona corretamente. O processo termina com `exit code 0`.

---

## Conceitos Aplicados

| Princípio | Onde aparece |
|---|---|
| **OCP** — Aberto/Fechado | Novos tipos de conteúdo ou modos de reprodução não exigem alterar código existente |
| **SRP** — Responsabilidade Única | Cada classe tem uma única responsabilidade (criar, reproduzir, notificar, registrar) |
| **DIP** — Inversão de Dependência | `Player` depende de `PlaybackMode` (interface), não de `SequentialMode` ou `RandomMode`; `Main` depende de `ContentCreator` (abstrato), não de `Music` ou `Podcast` |
| **Encapsulamento** | Lógica de criação nos creators; lógica de navegação nas strategies |
| **Polimorfismo** | `Content` permite tratar músicas e podcasts de forma uniforme |

---

## Possíveis Extensões

```
Novos tipos de conteúdo:
  └── AudiobookCreator extends ContentCreator  ← novo creator + nova classe modelo
      Audiobook extends Content                ← zero mudanças no código existente

Novos modos de reprodução:
  └── RepeatOneMode implements PlaybackMode    ← nova classe, zero mudanças no Player

Novos observadores:
  └── RecommendationService implements TrackObserver  ← basta registrar no Player

Persistência:
  └── HistoryService.saveToFile()  ← apenas lógica adicional no observer existente
```

---

*Projeto desenvolvido com fins educacionais para demonstração dos padrões GoF em Java.*
