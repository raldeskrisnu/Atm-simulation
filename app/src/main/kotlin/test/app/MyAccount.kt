package test.app

class MyAccount(name: String) {

    private var name: String? = null
    private var balance = 0
    private var owe = 0

    init {
        this.name = name
        balance = 0
        owe = 0
    }

    fun getName(): String? {
        return name
    }

    fun increaseBalance(increasedBalance: Int): Int {
        if (owe > 0) {
            if (owe < increasedBalance) {
                balance = increasedBalance - owe
                owe = 0
                return increasedBalance - balance
            } else {
                owe -= increasedBalance
            }
        } else {
            balance += increasedBalance
        }
        return increasedBalance
    }

    fun decreaseBalance(decreasedBalance: Int): Int {
        if (balance < decreasedBalance) {
            owe = decreasedBalance - balance
            balance = 0
            return decreasedBalance - owe
        }
        balance -= decreasedBalance
        return decreasedBalance
    }

    fun getBalance(): Int {
        return balance
    }

    fun getOweBalance(): Int {
        return owe
    }
}