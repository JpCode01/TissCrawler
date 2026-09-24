package com.jpcode

import groovyx.net.http.HttpBuilder

class TissCrawler {
    void acessar() {
       
        def response = HttpBuilder.configure {
            request.uri = "https://www.gov.br/ans/pt-br"
        }.get()

        println(response)
    }
}
