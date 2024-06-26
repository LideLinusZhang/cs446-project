package edu.card.clarity.repositories

import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardEntity
import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardInfoEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardInfoEntity
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnEntity
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.PurchaseReturn
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.PointBackCreditCard

@JvmName("pointBackCreditCardInfoEntityToDomainModel")
fun List<PointBackCreditCardInfoEntity>.toDomainModel() = map(
    PointBackCreditCardInfoEntity::toDomainModel
)

fun PointBackCreditCardInfoEntity.toDomainModel() = CreditCardInfo(
    name = name,
    statementDate = statementDate,
    paymentDueDate = paymentDueDate
)

@JvmName("cashBackCreditCardInfoEntityToDomainModel")
fun List<CashBackCreditCardInfoEntity>.toDomainModel() = map(
    CashBackCreditCardInfoEntity::toDomainModel
)

fun CashBackCreditCardInfoEntity.toDomainModel() = CreditCardInfo(
    name = name,
    statementDate = statementDate,
    paymentDueDate = paymentDueDate
)

@JvmName("pointBackCreditCardEntityToDomainModel")
fun List<PointBackCreditCardEntity>.toDomainModel() = map(
    PointBackCreditCardEntity::toDomainModel
)

fun PointBackCreditCardEntity.toDomainModel() = PointBackCreditCard(
    id = creditCardInfo.id,
    info = creditCardInfo.toDomainModel(),
    pointSystem = pointSystem.toDomainModel(),
    purchaseReturns = purchaseReturns.toDomainModel()
)

@JvmName("cashBackCreditCardEntityToDomainModel")
fun List<CashBackCreditCardEntity>.toDomainModel() = map(
    CashBackCreditCardEntity::toDomainModel
)

fun CashBackCreditCardEntity.toDomainModel() = CashBackCreditCard(
    id = creditCardInfo.id,
    info = creditCardInfo.toDomainModel(),
    purchaseReturns = purchaseReturns.toDomainModel()
)

@JvmName("pointSystemEntityToDomainModel")
fun List<PointSystemEntity>.toDomainModel() = map(PointSystemEntity::toDomainModel)

fun PointSystemEntity.toDomainModel() = PointSystem(
    id = id,
    name = name,
    pointToCashConversionRate = pointToCashConversionRate
)

fun MultiplierPurchaseReturnEntity.toDomainModel() = PurchaseReturn(
    applicablePurchaseType = purchaseType,
    returnFactor = multiplier
)

@JvmName("multiplierPurchaseReturnEntityToDomainModel")
fun List<MultiplierPurchaseReturnEntity>.toDomainModel() =
    map(MultiplierPurchaseReturnEntity::toDomainModel)

fun PercentagePurchaseReturnEntity.toDomainModel() = PurchaseReturn(
    applicablePurchaseType = purchaseType,
    returnFactor = percentage
)

@JvmName("percentagePurchaseReturnEntityToDomainModel")
fun List<PercentagePurchaseReturnEntity>.toDomainModel() =
    map(PercentagePurchaseReturnEntity::toDomainModel)