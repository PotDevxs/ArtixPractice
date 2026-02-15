# ArtixPractice - Practice Core Plugin

Plugin Spigot/Bukkit para servidores de prática (Practice) de Minecraft.

## Configuração Maven

Este projeto está configurado para ser compilado com Maven.

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+ instalado

### Correção Necessária

**IMPORTANTE**: Antes de compilar, você precisa corrigir manualmente a tag `<n>` na linha 12 do `pom.xml` para `<name>`:

```xml
<!-- Antes (incorreto) -->
<n>Bolt</n>

<!-- Depois (correto) -->
<name>Bolt</name>
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

### Notas

- O plugin requer Spigot 1.20.1 ou superior
- Todas as dependências são incluídas no JAR final (shaded)
- O projeto usa Java 17 como versão de compilação
