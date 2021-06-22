const val LIMIT_MONTH_VK_RUB = 40_000
const val LIMIT_DAY_VK_RUB = 15_000
const val LIMIT_MONTH_MASTERCARD_RUB = 600_000
const val LIMIT_DAY_MASTERCARD_RUB = 150_000
const val LIMIT_COMMISSION_MASTERCARD_RUB = 75_000

const val COMMISSION_MASTERCARD = 0.006
const val ADDITIONAL_COMMISSION_MASTERCARD_KOP = 2000.00
const val COMMISSION_VISA_MIR = 0.0075
const val MIN_COMMISSION_VISA_MIR_KOP = 3500.00

fun main() {
    val typeCard = "Mastercard" //Введите значение
    val amountRub = 1000.45 //Введите значение в рублях
    val amountKop = amountRub * 100 //Значение считается самостоятельно
    val previousAmountRub = 500000 //Введите значение в рублях

    if (limitsTypeCard(typeCard, previousAmountRub, amountKop.toInt())) {
        val totalCommission = commissionTypeCardKop(typeCard, previousAmountRub, amountKop.toInt())
        val totalAmountRub = (amountKop + totalCommission.toDouble()) / 100

        val totalString = "%.2f".format(totalAmountRub)
        val rub = totalString.substringBefore(",")
        val kop = totalString.substringAfter(",")
        println("Итоговая сумма перевода $rub рублей $kop копеек")

    } else println("Ваши лимиты исчерпаны или платежная система не поддерживается")
}

fun limitsTypeCard(typeCard: String = "VK", previousAmountRub: Int = 0, amountKop: Int): Boolean {
    return when (typeCard) {
        "Mastercard", " Mir", "Visa" -> when {
            previousAmountRub + amountKop / 100 <= LIMIT_MONTH_MASTERCARD_RUB -> when {
                amountKop / 100 <= LIMIT_DAY_MASTERCARD_RUB -> true
                else -> false
            }
            else -> false
        }
        "VK" -> when {
            previousAmountRub + amountKop / 100 <= LIMIT_MONTH_VK_RUB -> when {
                amountKop / 100 <= LIMIT_DAY_VK_RUB -> true
                else -> false
            }
            else -> false
        }
        else -> false
    }
}

fun commissionTypeCardKop(typeCard: String = "VK", previousAmountRub: Int = 0, amountKop: Int): Number {
    return when (typeCard) {
        "Mastercard" -> {
            when {
                previousAmountRub + amountKop / 100 > LIMIT_COMMISSION_MASTERCARD_RUB -> amountKop * COMMISSION_MASTERCARD + ADDITIONAL_COMMISSION_MASTERCARD_KOP
                else -> 0
            }
        }
        "Visa", "Mir" -> {
            when {
                amountKop * COMMISSION_VISA_MIR < MIN_COMMISSION_VISA_MIR_KOP -> MIN_COMMISSION_VISA_MIR_KOP
                else -> COMMISSION_VISA_MIR
            }
        }
        else -> 0
    }
}