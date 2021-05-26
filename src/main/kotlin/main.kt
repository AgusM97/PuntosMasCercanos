import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun main(args: Array<String>) {
    var asd = System.currentTimeMillis()
    var p1 = Punto(2,3)
    var p2 = Punto(12,30)
    var p3 = Punto(40,50)
    var p4 = Punto(5,1)
    var p5 = Punto(12,10)
    var p6 = Punto(3,4)

    var lista: List<Punto> = listOf(p1,p2,p3,p4,p5,p6)

    println("La distancia minima es:  ${distanciaMinimaA(lista)} y el tiempo es: ${System.currentTimeMillis()-asd}")
}

private fun distancia(p1: Punto, p2: Punto): Double =
    sqrt((p1.x - p2.x).toDouble().pow(2) + (p1.y - p2.y).toDouble().pow(2) )

private fun distanciaMinimaA(list: List<Punto>): Double{
    var min = 10.00.pow(9)
    for (p in list){
        for (k in list){
            var dis = distancia(p,k)
            if(!p.equals(k) && dis < min)
                min = dis
        }
    }
    return min
}

private fun distanciaMinimaB(list: List<Punto>): Double{
    if (list.size <= 3)
        return algoritmoBasico(list)
    else{
        var m = puntoMedioX(list)
        var d1 = distanciaMinimaB(list.filter { it.x <= m })
        var d2 = distanciaMinimaB(list.filter { it.x > m })

        var d = min(d1,d2)

        var d3 = recorrido(
            list.filter { (it.x <= m + d) && (it.x >= m - d) }.sortedBy { it.y }, d, m
        )
        return min(d,d3)
    }
}

private fun algoritmoBasico(list: List<Punto>): Double = distanciaMinimaA(list)

private fun puntoMedioX(list: List<Punto>): Int{
    var min: Int = list.minByOrNull { it.x }!!.x
    var max: Int = list.maxByOrNull { it.x }!!.x
    return (min+max)/2
}

private fun recorrido(lista: List<Punto>, d: Double, m: Int):Double {

    var minimo: Double = d
    for (i in 1 .. lista.size){
        var j = i-1
        if(!(lista[i].x >= m && lista[j].x >= m) || !(lista[i].x <= m && lista[j].x <= m) )
            minimo = min(distancia(lista[i], lista[j]) , minimo)
    }
    return minimo
}

private fun distanciaMinimaC(lista: List<Punto>): Double{
    var list = lista.sortedBy { it.y }
    if (list.size <= 3)
        return algoritmoBasico(list)
    else{
        var m = puntoMedioX(list)
        var d1 = distanciaMinimaB(list.filter { it.x <= m })
        var d2 = distanciaMinimaB(list.filter { it.x > m })

        var d = min(d1,d2)

        var d3 = recorrido(
            list.filter { (it.x <= m + d) && (it.x >= m - d) }, d, m
        )
        return min(d,d3)
    }
}