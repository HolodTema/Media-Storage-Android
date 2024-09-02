package com.terabyte.mediastorage

import javax.inject.Inject

fun main() {
    val bank = IronBank()
    val allies = Allies(bank)

    val starks = Starks(allies, bank)
    val boltons = Boltons(allies, bank)
    val war = War(starks, boltons)
    war.prepare()
    war.report()
}
interface House {
    fun prepareForWar()
    fun reportForWar()
}

class Starks @Inject constructor(allies: Allies, bank: IronBank): House {
    override fun prepareForWar() {
        println(this.javaClass.simpleName + " prepared for war.")
    }

    override fun reportForWar() {
        println(this.javaClass.simpleName + " are reporting war!")
    }
}


class Boltons @Inject constructor(allies: Allies, bank: IronBank): House {
    override fun prepareForWar() {
        println(this.javaClass.simpleName + " prepared for war.")
    }

    override fun reportForWar() {
        println(this.javaClass.simpleName + " are reporting war!")
    }
}


class War(private val starks: Starks, private val boltons: Boltons) {

    fun prepare() {
        starks.prepareForWar()
        boltons.prepareForWar()
    }

    fun report() {
        starks.reportForWar()
        boltons.reportForWar()
    }
}

class IronBank
class Allies(bank: IronBank)