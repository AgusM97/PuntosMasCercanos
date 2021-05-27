@file:Suppress("SpellCheckingInspection")

import kotlin.math.pow
import kotlin.math.sqrt


fun main() {
    val asd = System.currentTimeMillis()
    val p1 = Punto(2,3)
    val p2 = Punto(12,30)
    val p3 = Punto(40,50)
    val p4 = Punto(5,1)
    val p5 = Punto(12,10)
    val p6 = Punto(3,4)

    val lista: List<Punto> = listOf(p1,p2,p3,p4,p5,p6)

    println("La distancia minima es:  ${distanciaMinimaA(lista)} y el tiempo es: ${System.currentTimeMillis()-asd}")
}

private fun distancia(p1: Punto, p2: Punto): Double =
    sqrt((p1.x - p2.x).toDouble().pow(2) + (p1.y - p2.y).toDouble().pow(2) )

private fun distanciaMinimaA(list: List<Punto>): ParDePuntos{
    lateinit var ret: ParDePuntos
    var min = 10.00.pow(9)
    for (p in list){
        for (k in list){
            val dis = distancia(p,k)
            if(p != k && dis < min) {
                min = dis
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
        val franjaOrdenada = list.filter { (it.x <= m + parMenor1.d) && (it.x >= m - parMenor1.d) }.sortedBy { it.y }
        val parMenor2 = recorrerFranja(franjaOrdenada, m)
        if (parMenor2 != null && parMenor1.d < parMenor2.d) parMenor2 else parMenor1
    }
}

private fun algoritmoBasico(list: List<Punto>): ParDePuntos =
    distanciaMinimaA(list)

private fun puntoMedioX(list: List<Punto>): Int{
    val min: Int = list.minByOrNull { it.x }!!.x
    val max: Int = list.maxByOrNull { it.x }!!.x
    return (min+max)/2
}

private fun recorrerFranja(lista: List<Punto>, m: Int): ParDePuntos? {
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
        val franjaOrdenada = list.filter { (it.x <= m + parMenor1.d) && (it.x >= m - parMenor1.d) }
        val parMenor2 = recorrerFranja(franjaOrdenada, m)
        if (parMenor2 != null && parMenor1.d < parMenor2.d) parMenor2 else parMenor1
    }
}