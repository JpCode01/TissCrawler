package com.jpcode

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class TissCrawler {

    private final TissParser parser = new TissParser()

    void iniciar() {

        File diretorio = new File(
                "C:/Users/Mysterio/Desktop/Estudo/Estudo Java/TissCrawler/Downloads/Arquivos_padrao_TISS"
        )

        if (!diretorio.exists()) {
            diretorio.mkdirs()
        }



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

                    List<HistoricoTiss> historicos = parser.extrairHistorico(documentHistorico)

                    for (HistoricoTiss historico : historicos) {
                        println "${historico.competencia} | ${historico.publicacao} | ${historico.inicioVigencia}"
                    }
                        
                } else {
                    println "Link do histórico NÃO encontrado"
                }

                String urlTabelasRelacionadas =
                        "https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/padrao-tiss-tabelas-relacionadas"

                Document documentTabelasRelacionadas = HttpBuilder.configure {
                    request.uri = urlTabelasRelacionadas
                }.get()

                Element linkTabelaErros = parser.encontrarLinkTabelaErros(documentTabelasRelacionadas)

                if (linkTabelaErros) {

                    String urlTabelaErros =
                            linkTabelaErros.absUrl('href')

                    File arquivoErros = new File(
                            diretorio,
                            'Tabela_erros_envio_ANS.xlsx'
                    )

                    if (arquivoErros.exists()) {
                        println("Tabela de erros já existe. Download não necessário.")
                    } else {

                        byte[] arquivo = HttpBuilder.configure {
                            request.uri = urlTabelaErros
                        }.get(byte[]) {
                            response.parser(
                                    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                            ) {
                                config, resposta ->
                                    resposta.inputStream.bytes
                            }
                        }

                        arquivoErros.bytes = arquivo

                        println("Tabela de erros no envio para a ANS baixada!")
                    }

                } else {

                    println "Link da tabela de erros NÃO encontrado"

                }
                
                Element linkVersao = parser.encontrarLinkVersao(documentTiss)

                String urlVersao = linkVersao.absUrl('href')

                Document documentVersao = HttpBuilder.configure {
                    request.uri = urlVersao
                }.get()

                Element linkComunicacao = parser.encontrarLinkComunicacao(documentVersao)

                String urlComunicacao = linkComunicacao.absUrl('href')

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