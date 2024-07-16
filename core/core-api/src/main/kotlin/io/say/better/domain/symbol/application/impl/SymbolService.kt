package io.say.better.domain.symbol.application.impl

import io.say.better.storage.mysql.domains.symbol.entity.Symbol
import io.say.better.storage.mysql.domains.symbol.repository.SymbolReadRepository
import io.say.better.storage.mysql.domains.symbol.repository.SymbolWriteRepository
import org.springframework.stereotype.Service

@Service
class SymbolService(
    private val symbolReadRepository: SymbolReadRepository,
    private val symbolWriteRepository: SymbolWriteRepository,
) {
    fun getSymbols(symbols: List<String>?): List<Symbol> = symbolReadRepository.findByTitleIn(symbols!!)

    fun getSymbols(name: String): List<Symbol> = symbolReadRepository.findByTitleStartingWith(name)

    fun getSymbol(symbolId: Long): Symbol = symbolReadRepository.findById(symbolId).get()
}
