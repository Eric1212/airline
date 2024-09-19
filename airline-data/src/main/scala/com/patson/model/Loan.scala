package com.patson.model

case class Loan(airlineId : Int, principal : Double, annualRate : BigDecimal, creationCycle : Int, lastPaymentCycle : Int, term : Int, var id : Int = 0) extends IdObject {
  val WEEKS_PER_YEAR = 52
  //Payment = P x (r / n) x (1 + r / n)^n(t)] / ((1 + r / n)^n(t) - 1)
  val weeklyRate = annualRate / WEEKS_PER_YEAR
  val weeklyPayment : Long = Math.ceil(principal * weeklyRate.toDouble * Math.pow(1 + weeklyRate.toDouble, term) / (Math.pow(1 + weeklyRate.toDouble, term) - 1)).toLong
  val interest = weeklyPayment * term - principal.toDouble
  val total = principal + interest.toDouble

  val remainingTerm = (currentCycle : Int) => creationCycle + term - currentCycle
  val remainingPayment : (Int => Long)= (currentCycle : Int) => (total - (term - remainingTerm(currentCycle)) * weeklyPayment).toLong
//  val remainingInterest = (currentCycle : Int) => interestWeeklyPayment(currentCycle) * remainingTerm
  val remainingPrincipal : (Int => Long) = (currentCycle : Int) => {
    val paidMonth = currentCycle - creationCycle
    (principal * Math.pow(1 + weeklyRate.toDouble, paidMonth) - weeklyPayment * (Math.pow(1 + weeklyRate.toDouble, paidMonth) - 1) / weeklyRate).toLong
  }

  val weeklyInterest : (Int => Long) = (currentCycle : Int) => {
    if (currentCycle <= creationCycle) {
      0
    } else {
      (remainingPrincipal(currentCycle - 1) * weeklyRate).toLong
    }
  }

  val weeklyPrincipal : (Int => Long) = (currentCycle : Int) => {
    if (currentCycle <= creationCycle) {
      0
    } else {
      weeklyPayment - weeklyInterest(currentCycle)
    }
  }


  val earlyRepaymentFee : (Int => Long) = (currentCycle : Int) => ((remainingPayment(currentCycle) - remainingPrincipal(currentCycle)) * 0.5).toLong //half of the remaining interest
  val earlyRepayment : (Int => Long) = (currentCycle : Int) => remainingPrincipal(currentCycle) + earlyRepaymentFee(currentCycle)



}


