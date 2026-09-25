package com.jpcode

import java.time.YearMonth

class TimeParser {

    private static final Map<String, Integer> MESES = [
            "jan": 1,
            "fev": 2,
            "mar": 3,
            "abr": 4,
            "mai": 5,
            "jun": 6,
            "jul": 7,
            "ago": 8,
            "set": 9,
            "out": 10,
            "nov": 11,
            "dez": 12
    ]

    boolean aPartirDeJaneiro2016(String competencia) {

        String[] partes = competencia.toLowerCase().split('/')

        int mes = MESES[partes[0]]
        int ano = Integer.parseInt(partes[1])

        YearMonth data = YearMonth.of(ano, mes)
        YearMonth inicio = YearMonth.of(2016, 1)

        return !data.isBefore(inicio)
    }
}
