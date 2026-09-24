package com.jpcode

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissParser {

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
}