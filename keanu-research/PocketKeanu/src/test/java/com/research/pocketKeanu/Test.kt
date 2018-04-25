package com.research.pocketKeanu

import com.research.pocketKeanu.abstractTypes.DoubleLike
import com.research.pocketKeanu.distributions.ContinuousProbabilisticFunction
import com.research.pocketKeanu.distributions.ProbabilisticProgram
import com.research.pocketKeanu.inference.findMAP
import com.research.pocketKeanu.randomFactory.DifferentiableRandom
import com.research.pocketKeanu.randomFactory.DoubleLikeRandomFactory

class Test : ContinuousProbabilisticFunction {

    @org.junit.Test
    fun test() {
        val rand = DifferentiableRandom()
        val probProgram = ProbabilisticProgram(this, listOf())
        val (t1, t2, temp) = probProgram.outputs
        val observedProg = probProgram.observe(mapOf(
                t1 to 21.0,
                t2 to 20.0 ))
        observedProg.findMAP(1000)
        println(temp.value)
    }


    override fun <T : DoubleLike<T>> apply(doubleParams: List<T>, rand: DoubleLikeRandomFactory<T>): List<T> {
        val temp = rand.nextGaussian(20.0,2.0)
        val t1 = temp + rand.nextGaussian()
        val t2 = temp + rand.nextGaussian()

        return listOf(t1, t2, temp)
    }
}