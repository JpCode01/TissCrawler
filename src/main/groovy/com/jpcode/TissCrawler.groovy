package com.jpcode

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissCrawler {

    private final TissParser parser = new TissParser()

    void iniciar() {

        println System.getProperty("user.dir")

        Document document = HttpBuilder.configure {
            request.uri = "https://www.gov.br/ans/pt-br"
        }.get()

        Element linkPrestador = parser.encontrarLinkPrestador(document)

        if (linkPrestador) {

            String urlPrestador = linkPrestador.absUrl('href')

            Document documentPrestador = HttpBuilder.configure {
                request.uri = urlPrestador
            }.get()

            Element linkTiss = parser.encontrarLinkTiss(documentPrestador)

            if (linkTiss) {

                String urlTiss = linkTiss.absUrl('href')

                Document documentTiss = HttpBuilder.configure {
                    request.uri = urlTiss
                }.get()

                Element linkHistorico = parser.encontrarLinkHistorico(documentTiss)

                if (linkHistorico) {
                    String urlHistorico = linkHistorico.absUrl('href')

                    Document documentHistorico = HttpBuilder.configure {
                        request.uri = urlHistorico
                    }.get()

                    println("Página do historico acessada")
                    println documentHistorico.title()
                } else {
                    println "Link do histórico NÃO encontrado"
                }

                Element linkVersao = parser.encontrarLinkVersao(documentTiss)

                String urlVersao = linkVersao.absUrl('href')

                Document documentVersao = HttpBuilder.configure {
                    request.uri = urlVersao
                }.get()

                Element linkComunicacao = parser.encontrarLinkComunicacao(documentVersao)

                String urlComunicacao = linkComunicacao.absUrl('href')

                File diretorio = new File(
                        "C:/Users/Mysterio/Desktop/Estudo/Estudo Java/TissCrawler/Downloads/Arquivos_padrao_TISS"
                )

                diretorio.mkdirs()

                File arquivoZip = new File(
                        diretorio,
                        'PadroTISSComunicao_202511.zip'
                )

                if (arquivoZip.exists()) {

                    println("Arquivo já existe. Download não necessário.")

                } else {

                    byte[] arquivo = HttpBuilder.configure {
                        request.uri = urlComunicacao
                    }.get(byte[]) {
                        response.parser('application/zip') { config, resposta ->
                            resposta.inputStream.bytes
                        }
                        }

                    arquivoZip.bytes = arquivo

                    println("Componente de Comunicação baixado!")
                }

            } else {
                println("Link da versão não encontrado")
            }

        } else {
            println("Link TISS não encontrado")
        }
    }
}