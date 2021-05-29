@file:Suppress("SpellCheckingInspection")

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val max = 1000000000.00

fun main() {

    val lista = benchmarck(19)
    val tiempoInicio = System.currentTimeMillis()

    println("La distancia minima es:  ${distanciaMinimaA(lista)} y el tiempo es: ${System.currentTimeMillis()-tiempoInicio}")
}

private fun benchmarck(potencia: Int):List<Punto>{
    val n = 2.00
    val cantPuntos = n.pow(potencia).toInt()
    var list: MutableList<Punto> = mutableListOf()
    for (i in 1..cantPuntos)
        list.add( Punto( Random.nextDouble(max), Random.nextDouble(max) ) )

    return list
}

private fun distancia(p1: Punto, p2: Punto): Double =
    sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2) )

private fun distanciaMinimaA(list: List<Punto>): ParDePuntos{
    var max = 10.00.pow(9) + 1
    val pMin = Punto(0.0,0.0)
    val pMax = Punto(max, max)
    var ret = ParDePuntos(pMin, pMax, distancia(pMin, pMax))
    var dis: Double
    for (p in list){
        for (k in list){
            dis = distancia(p,k)
            if(p != k && dis < ret.d) {
                ret = ParDePuntos(p, k, dis)
            }
        }
    }
    return ret
}

private fun distanciaMinimaB(list: List<Punto>): ParDePuntos{
    return if (list.size <= 3)
        algoritmoBasico(list)
    else{
        val m = puntoMedioX(list)
        val par1 = distanciaMinimaB(list.filter { it.x <= m })
        val par2 = distanciaMinimaB(list.filter { it.x > m })
        val parMenor1 = if(par1.d < par2.d) par1 else par2
        val franjaOrdenada = list.filter {
            it.x in (m - parMenor1.d) .. (m + parMenor1.d)
        }.sortedBy { it.y }
        val parMenor2 = recorrerFranja(franjaOrdenada, m)
        if (parMenor2 != null && parMenor1.d < parMenor2.d) parMenor2 else parMenor1
    }
}

private fun algoritmoBasico(list: List<Punto>): ParDePuntos =
    distanciaMinimaA(list)

private fun puntoMedioX(list: List<Punto>): Double{
    val min: Double = list.minByOrNull { it.x }!!.x
    val max: Double = list.maxByOrNull { it.x }!!.x
    return (min+max)/2
}

private fun recorrerFranja(lista: List<Punto>, m: Double): ParDePuntos? {
    var ret: ParDePuntos? = null
    var minimo: Double = 10.00.pow(9)
    for (i in 1 until lista.size){
        val j = i-1
        val distintasMitades = !(lista[i].x >= m && lista[j].x >= m) || !(lista[i].x <= m && lista[j].x <= m)
        if(distintasMitades) {
            val d = distancia(lista[i], lista[j])
            if (d < minimo){
                minimo = d
                ret = ParDePuntos(lista[i], lista[j], d)
            }
        }
    }
    return ret
}


private fun distanciaMinimaCShell(lista: List<Punto>): ParDePuntos =
    distanciaMinimaC(lista.sortedBy { it.y })

private fun distanciaMinimaC(list: List<Punto>): ParDePuntos {
    return if (list.size <= 3)
        algoritmoBasico(list)
    else{
        val m = puntoMedioX(list)
        val par1 = distanciaMinimaC(list.filter { it.x <= m })
        val par2 = distanciaMinimaC(list.filter { it.x > m })
        val parMenor1 = if(par1.d < par2.d) par1 else par2
        val franjaOrdenada = list.filter {  it.x in (m - parMenor1.d).. (m + parMenor1.d)  }
        val parMenor2 = recorrerFranja(franjaOrdenada, m)
        if (parMenor2 != null && parMenor1.d < parMenor2.d) parMenor2 else parMenor1
    }
}