@file:Suppress("SpellCheckingInspection")

import kotlin.math.pow
import kotlin.math.sqrt


fun main() {
    val asd = System.currentTimeMillis()
    val p1 = Punto(2.0,3.0)
    val p2 = Punto(12.0,30.0)
    val p3 = Punto(40.0,50.0)
    val p4 = Punto(5.0,1.0)
    val p5 = Punto(12.0,10.0)
    val p6 = Punto(3.0,4.0)

    val lista: List<Punto> = listOf(p1,p2,p3,p4,p5,p6)

    println("La distancia minima es:  ${distanciaMinimaCShell(lista)} y el tiempo es: ${System.currentTimeMillis()-asd}")
}

private fun distancia(p1: Punto, p2: Punto): Double =
    sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2) )

private fun distanciaMinimaA(list: List<Punto>): ParDePuntos{
    var max = 10.00.pow(9) + 1
    val pMin = Punto(0.0,0.0)
    val pMax = Punto(max, max)
    var ret = ParDePuntos(pMin, pMax, distancia(pMin, pMax))
    for (p in list){
        for (k in list){
            val dis = distancia(p,k)
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
        val franjaOrdenada = list.filter { it.x in (m - parMenor1.d).. (m + parMenor1.d) }.sortedBy { it.y }
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