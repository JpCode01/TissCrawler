package com.jpcode

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissParser {

    private final TimeParser timeParser = new TimeParser()

    Element encontrarLinkPrestador(Document document) {
        return document.select('a').find { Element link ->
            link.text().contains("Espaço do Prestador de Serviços de Saúde")
        }
    }

    Element encontrarLinkTiss(Document document) {
        return document.select('a').find { Element link ->
            link.text().contains("TISS - Padrão para Troca de Informação de Saúde Suplementar")
        }
    }

    Element encontrarLinkVersao(Document document) {
        return document.select('a').find { Element link ->
            link.text().contains("Clique aqui para acessar a versão Julho/2026")
        }
    }

    Element encontrarLinkComunicacao(Document document) {
        Element tabelaDocumentos = document.select('table').find { Element tabela ->
            tabela.text().contains("Componente de Comunicação")
        }

        return tabelaDocumentos.select('a').find { Element link ->
            link.text().contains("Componente de Comunicação")
        }
    }

    Element encontrarLinkHistorico(Document document) {
        return document.select('a').find { Element link ->
            link.text().contains("Clique aqui para acessar todas as versões dos Componentes")
        }
    }

    Element encontrarTabelaHistorico(Document document) {
        return document.select('table').find { Element tabela ->
            tabela.text().contains("Competência") &&
                    tabela.text().contains("Publicação") &&
                    tabela.text().contains("Início de Vigência")
        }
    }

    List<HistoricoTiss> extrairHistorico(Document document) {

        Element tabelaHistorico = encontrarTabelaHistorico(document)

        List<HistoricoTiss> historicos = new ArrayList<>()

        if (!tabelaHistorico) {
            return historicos
        }

        List<Element> linhas = tabelaHistorico.select('tr').toList()

        for (Element linha : linhas) {

            List<Element> colunas = linha.select('td').toList()

            if (colunas.size() < 3) {
                continue
            }

            String competencia = colunas[0].text()
            String publicacao = colunas[1].text()
            String inicioVigencia = colunas[2].text()

            if (timeParser.aPartirDeJaneiro2016(competencia)) {

                HistoricoTiss historico = new HistoricoTiss()

                historico.competencia = competencia
                historico.publicacao = publicacao
                historico.inicioVigencia = inicioVigencia

                historicos.add(historico)
            }
        }

        return historicos
    }
}