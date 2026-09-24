package com.jpcode

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissCrawler {
    void acessar() {

        Document document = HttpBuilder.configure {
            request.uri = "https://www.gov.br/ans/pt-br"
        }.get()

        Element linkEncontrado = document.select('a').find() { link ->
            link.text().contains("Espaço do Prestador de Serviços de Saúde")
        }

        if(linkEncontrado) {
            String urlPrestador = linkEncontrado.absUrl('href')

            Document documentPrestador = HttpBuilder.configure {
                request.uri = urlPrestador
            }.get()

            println("Página do prestador acessado!")
            println(document.title())
        } else {
            println("Link não encontrado")
        }

    }
}
