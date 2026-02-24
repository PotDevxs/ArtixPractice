# ArtixPractice - Practice Core Plugin

Plugin Spigot/Bukkit para servidores de prática (Practice) de Minecraft.

### Licença e inicialização

- A licença é validada ao iniciar (config: `License:` em `config.yml`). Key vazia, placeholder ou inválida na API resulta em **"Key Invalida"** e o plugin é **desativado**.
- Key de desenvolvimento `ARTIX-DEVELOPERS-USER-MAX` é aceita para testes.

### Comandos

- Comandos são registrados **por código** (CommandMap); não há seção `commands:` no `plugin.yml`.
- Principais: `/practice`, `/party`, `/accept`, `/shop`, `/cosmetics`, `/queue`, `/silent`, `/config`, `/animatedtitle`, `/hologram`, `/botkb`, `/togglev`, `/toggleduels`, `/postmatch`, `/artixlicense`. Ver `plugin.yml` para permissões.

### Config e reload

- `/config reload` recarrega database, scoreboard, menu, hotbar, messages, levels e **atualiza** scoreboard e hotbar de todos os jogadores online.
- `scoreboard.yml` e `hotbar.yml` são copiados do JAR na primeira execução (em `plugins/ArtixPractice/`).

## Configuração Maven

Este projeto está configurado para ser compilado com Maven.

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+ instalado

### Correção Necessária

**IMPORTANTE**: Antes de compilar, você precisa corrigir manualmente a tag `<n>` na linha 12 do `pom.xml` para `<name>`:

```xml
<!-- Antes (incorreto) -->
<n>Artix</n>

<!-- Depois (correto) -->
<name>ArtixPractice</name>
```

### Compilação

Para compilar o projeto, execute:

```bash
mvn clean package
```

O JAR compilado estará em `target/ArtixPractice-22.0.jar`.

### Estrutura do Projeto

O projeto usa a estrutura de diretórios não-padrão:
- Código fonte: `main/java/`
- Recursos: `main/resources/`

O `pom.xml` está configurado para usar essa estrutura.

### Dependências Incluídas

O projeto inclui as seguintes dependências (shaded no JAR final):

- **Spigot API** (provided)
- **MongoDB Driver** 4.11.1
- **MySQL Connector** 8.2.0
- **SQLite JDBC** 3.44.1.0
- **PacketEvents** 2.3.0
- **Gson** 2.10.1
- **FastUtil** 8.5.12
- **Kyori Adventure API** 4.14.0
- **Log4j** 2.20.1
- **SnakeYAML** 2.2
- **JetBrains Annotations** 24.0.1

Todas as dependências são relocadas para o pacote `dev.artixdev.libs.*` para evitar conflitos.

### Repositórios Maven

O projeto usa os seguintes repositórios:
- Spigot Repository
- Sonatype Snapshots
- CodeMC Repository
- JitPack
- PaperMC Repository
- Maven Central

### Comandos Úteis

```bash
# Compilar o projeto
mvn clean package

# Compilar sem executar testes
mvn clean package -DskipTests

# Limpar arquivos compilados
mvn clean

# Validar o pom.xml
mvn validate

# Ver dependências do projeto
mvn dependency:tree
```

### Problemas conhecidos: HolographicDisplays

Se aparecer no console:

```text
[HolographicDisplays] Unexpected error while modifying the channel pipeline.
java.util.NoSuchElementException: packet_handler
```

Isso é um **bug do HolographicDisplays** (não do ArtixPractice): o plugin tenta injetar um listener no pipeline Netty e não encontra o handler `packet_handler` (comum no 1.8 ou com ProtocolLib).

**O que fazer:**

1. **Ordem de carregamento** – Garantir que o HolographicDisplays carregue **antes** do ProtocolLib (no `plugin.yml` do HolographicDisplays: `loadbefore: [ProtocolLib]`), se você usar ProtocolLib.
2. **Atualizar** – Usar a versão mais recente do HolographicDisplays (o projeto foi arquivado, mas builds antigos podem ter o problema).
3. **Testar sem ProtocolLib** – Rodar sem ProtocolLib para ver se o erro some (confirma conflito de pipeline).
4. **Alternativa** – Se o erro continuar, usar outro plugin de hologramas compatível com 1.8 ou desativar o HolographicDisplays.

O ArtixPractice usa o HolographicDisplays como **softdepend** (opcional); o servidor funciona mesmo com esse aviso.

### Notas

- O plugin requer Spigot 1.8.8 ou Spigot 1.20.1 ou superior
- Todas as dependências são incluídas no JAR final (shaded)
- O projeto usa Java 17 como versão de compilação
