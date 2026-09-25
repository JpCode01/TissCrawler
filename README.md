# TISS Crawler

Web Crawler desenvolvido em **Groovy** para automatizar a coleta de informações e documentos públicos disponibilizados pela **Agência Nacional de Saúde Suplementar (ANS)** relacionados ao padrão **TISS — Troca de Informações na Saúde Suplementar**.

O projeto foi desenvolvido como parte do **Acelera ZG**, utilizando recursos e conceitos estudados durante a formação, principalmente **Groovy, HTTPBuilder, Jsoup, Regex, orientação a objetos e manipulação de arquivos**.

## Objetivo

O objetivo do projeto é desenvolver um bot capaz de acessar o site público da ANS, navegar pelas páginas relacionadas ao padrão TISS, realizar o parsing do HTML e coletar automaticamente os dados e arquivos solicitados.

O crawler realiza três tarefas principais:

1. Localiza e baixa o **Componente de Comunicação** da versão do TISS encontrada no site.
2. Consulta o **Histórico das versões dos Componentes do Padrão TISS** e coleta informações a partir da competência **Jan/2016**.
3. Localiza e baixa a **Tabela de erros no envio para a ANS**.

---

## Funcionalidades

### 1. Componente de Comunicação

O crawler inicia o acesso pelo site da ANS e realiza a navegação:

```text
ANS
 ↓
Espaço do Prestador de Serviços de Saúde
 ↓
TISS - Padrão para Troca de Informação de Saúde Suplementar
 ↓
Versão do TISS
 ↓
Componente de Comunicação
```

A versão é identificada através do texto encontrado na página utilizando **Regex**, evitando deixar um mês e ano específico fixados diretamente no código.

Após localizar o componente, o arquivo é baixado e armazenado em:

```text
Downloads/Aquivos_padrao_TISS/
```

O crawler também verifica se o arquivo já existe antes de realizar um novo download.

---

### 2. Histórico das versões do TISS

O crawler acessa a página de histórico das versões dos componentes e realiza o parsing da tabela.

São coletadas as seguintes informações:

* **Competência**
* **Publicação**
* **Início de Vigência**

A coleta considera as competências a partir de:

```text
Jan/2016
```

As informações são armazenadas em objetos `HistoricoTiss` e exibidas no console.

Exemplo:

```text
Jan/2016 | 15/1/2016 | 15/1/2016
...
```

A comparação das competências é realizada utilizando `YearMonth`.

---

### 3. Tabela de erros no envio para a ANS

O crawler acessa a página **Tabelas relacionadas**, localiza o link da:

```text
Tabela de erros no envio para a ANS
```

e realiza o download do arquivo `.xlsx`.

O arquivo também é armazenado no diretório:

```text
Downloads/Aquivos_padrao_TISS/
```

---

## Tecnologias utilizadas

### Groovy

Linguagem principal utilizada no desenvolvimento do crawler.

Foi utilizada para:

* controle do fluxo da aplicação;
* orientação a objetos;
* manipulação de arquivos;
* processamento das informações;
* integração entre as classes.

### HTTPBuilder NG

Utilizado para realizar as requisições HTTP ao site da ANS e realizar os downloads dos arquivos.

Dependência:

```groovy
implementation 'io.github.http-builder-ng:http-builder-ng-core:1.0.4'
```

### Jsoup

Utilizado para realizar o parsing dos documentos HTML retornados pelo site da ANS.

Com o Jsoup são localizados:

* links;
* tabelas;
* linhas;
* colunas;
* textos dos elementos HTML.

Dependência:

```groovy
implementation 'org.jsoup:jsoup:1.23.2'
```

### Regex

Utilizado para identificar a competência da versão do TISS presente no texto do link.

Exemplo do padrão utilizado:

```regex
Clique aqui para acessar a versão ([A-Za-z]+\/\d{4})
```

Com isso, uma informação como:

```text
Clique aqui para acessar a versão Julho/2026
```

pode ter sua competência identificada como:

```text
Julho/2026
```

### Java Time API

A classe `YearMonth` é utilizada para representar e comparar competências no formato mês/ano.

Exemplo:

```groovy
YearMonth data = YearMonth.of(ano, mes)
YearMonth inicio = YearMonth.of(2016, 1)
```

Isso permite verificar se uma competência é igual ou posterior a Janeiro de 2016.

### Manipulação de arquivos

A classe `File` é utilizada para:

* criar o diretório de downloads;
* verificar se arquivos já existem;
* salvar os arquivos baixados.

Os arquivos são armazenados em:

```text
Downloads/Aquivos_padrao_TISS/
```

---

## Estrutura do projeto

```text
TissCrawler/
│
├── Downloads/
│   └── Arquivos_padrao_TISS/
│       ├── PadroTISSComunicao_202511.zip
│       └── Tabela_erros_envio_ANS.xlsx
│
├── src/
│   └── main/
│       └── groovy/
│           └── com/
│               └── jpcode/
│                   ├── Main.groovy
│                   ├── TissCrawler.groovy
│                   ├── TissParser.groovy
│                   ├── TimeParser.groovy
│                   └── HistoricoTiss.groovy
│
├── build.gradle
└── settings.gradle
```

---

## Organização das classes

### `Main`

Classe responsável pelo ponto de entrada da aplicação.

```text
Main
 ↓
TissCrawler.iniciar()
```

### `TissCrawler`

Responsável pelo fluxo principal do Web Crawler.

Realiza:

* requisições HTTP;
* navegação entre as páginas;
* utilização do parser;
* downloads;
* criação do diretório;
* verificação da existência dos arquivos.

### `TissParser`

Responsável pelo parsing dos documentos HTML utilizando Jsoup.

Entre suas responsabilidades estão localizar:

* Espaço do Prestador;
* página do TISS;
* versão do TISS;
* Componente de Comunicação;
* página de histórico;
* tabela de histórico;
* Tabela de erros.

Também realiza a extração dos dados do histórico.

### `TimeParser`

Responsável pelo tratamento das competências.

Possui o mapeamento dos meses em português:

```text
jan → 1
fev → 2
mar → 3
abr → 4
mai → 5
jun → 6
jul → 7
ago → 8
set → 9
out → 10
nov → 11
dez → 12
```

Utiliza `YearMonth` para verificar se a competência é a partir de Janeiro de 2016.

### `HistoricoTiss`

Classe responsável por representar os dados extraídos da tabela histórica:

```text
competencia
publicacao
inicioVigencia
```

---

## Fluxo da aplicação

```text
                    ┌─────────────────┐
                    │      Main       │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  TissCrawler    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │     ANS.gov     │
                    └────────┬────────┘
                             │
             ┌───────────────┼────────────────┐
             │               │                │
             ▼               ▼                ▼
        Versão TISS      Histórico      Tabelas
             │               │                │
             ▼               ▼                ▼
       Comunicação      Jan/2016+       Tabela de
             │          competência        erros
             ▼               │                ▼
          .zip               │              .xlsx
                             ▼
                     Dados no console
```

---

## Como executar

### Pré-requisitos

* Java 17 ou superior
* Gradle
* Git

### Clonar o projeto

```bash
git clone https://github.com/JpCode01/TissCrawler.git
```

Entre no diretório:

```bash
cd TissCrawler
```

### Executar

Com Gradle:

```bash
./gradlew run
```

No Windows:

```bash
gradlew.bat run
```

O crawler realizará automaticamente as requisições, coleta de dados e downloads.

---

## Arquivos gerados

Após a execução, os arquivos baixados estarão em:

```text
Downloads/
└── Arquivos_padrao_TISS/
```

Contendo:

```text
PadroTISSComunicao_202511.zip
Tabela_erros_envio_ANS.xlsx
```

Caso os arquivos já existam, o crawler não realiza o download novamente.

---

## Dependências

O projeto utiliza as seguintes dependências principais:

```groovy
implementation 'org.codehaus.groovy:groovy-all:3.0.25'
implementation 'io.github.http-builder-ng:http-builder-ng-core:1.0.4'
implementation 'org.jsoup:jsoup:1.23.2'
```

Testes:

```groovy
testImplementation platform('org.junit:junit-bom:6.0.0')
testImplementation 'org.junit.jupiter:junit-jupiter'
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
```

---

## Conceitos utilizados

Durante o desenvolvimento foram utilizados conceitos como:

* Web Crawler / Web Scraper
* Requisições HTTP
* Parsing de HTML
* Seletores CSS com Jsoup
* Expressões regulares (Regex)
* Orientação a objetos
* Encapsulamento de responsabilidades
* Coleções (`List` e `Map`)
* Manipulação de arquivos
* Download de arquivos binários
* Tratamento de datas com `YearMonth`
* Validação de existência de arquivos
* Organização de código em classes
* Gradle e gerenciamento de dependências

---

## Projeto

Desenvolvido por **João Pedro** como parte do programa **Acelera ZG**.

O projeto utiliza exclusivamente documentos públicos disponibilizados pela Agência Nacional de Saúde Suplementar (ANS).

---

## Licença

Projeto desenvolvido para fins educacionais.
