package test.app

class Validation {

    private val accountTable: HashMap<String, MyAccount> = HashMap()
    private val oweToTable: HashMap<String, String> = HashMap()
    private val oweFromTable: HashMap<String, String> = HashMap()

    private var myAccount: MyAccount? = null
    private val messageUtils: MessageUtils = MessageUtils()

    var cacheUsername = ""

    private fun isCantTransfer(target: String): Boolean {
        if (!accountTable.containsKey(target)) {
            println("Account not found")
            return true
        }

        if (myAccount?.getName().equals(target)) {
            println("cant transfer same account")
            return true
        }

        if (myAccount?.getBalance()!! <= 0) {
            println("cant transfer balance less than 0");
            return true
        }
        return false
    }

    fun attempLogin(name: String) {
        if (cacheUsername.isNotEmpty()) {
            println("session $name still active please change other command")
            return
        }

        if (!accountTable.containsKey(name)) {
            val createdAccount = MyAccount(name)
            accountTable[name] = createdAccount
        }
        myAccount = accountTable[name]

        myAccount?.getName()?.let { messageUtils.helloMessages(it) }
        myAccount?.let { it.getBalance().let { it1 -> messageUtils.currentBalanceMessages(it1) } }

        if (myAccount?.getOweBalance()!! > 0) {
            val owedToAccount: MyAccount? = accountTable[oweToTable[name]]
            owedToAccount?.getName()?.let { messageUtils.oweMessages(myAccount?.getOweBalance()!!, it, false) }
        } else {
            if (oweFromTable.containsKey(name)) {
                val owedFromAccount: MyAccount? = accountTable[oweFromTable[name]]
                owedFromAccount?.getOweBalance()?.let { owedFromAccount.getName()?.let { it1 -> messageUtils.oweMessages(it, it1, true) } }
            }
        }

        cacheUsername = name
    }

    fun doDeposit(depositBalance: Int) {
        if (myAccount?.getOweBalance()!! > 0) {

            val targetName = oweToTable[myAccount?.getName()]
            val targetAccount: MyAccount? = accountTable[targetName]

            val movingBalance: Int? = myAccount?.increaseBalance(depositBalance)

            movingBalance?.let { targetAccount?.increaseBalance(it) }

            targetAccount?.getName()?.let { movingBalance?.let { it1 -> messageUtils.transferMessages(it1, it) } }
            messageUtils.currentBalanceMessages(myAccount?.getBalance() ?: 0)

            if (myAccount?.getOweBalance()!! > 0) {
                targetName?.let { messageUtils.oweMessages(myAccount?.getOweBalance() ?: 0, it, false) }
            } else {
                oweToTable.remove(myAccount?.getName())
                oweFromTable.remove(targetName)
            }
        } else {
            myAccount?.increaseBalance(depositBalance)
            messageUtils.currentBalanceMessages(myAccount?.getBalance() ?: 0)
        }
    }

    fun doWithDraw(withdrawBalance: Int) {
        val deductedBalance: Int = myAccount?.getBalance()?.minus(withdrawBalance) ?: 0

        if (deductedBalance >= 0) {
            myAccount?.decreaseBalance(withdrawBalance)
            myAccount?.getBalance()?.let { messageUtils.currentBalanceMessages(it) }
        } else {
            println("Insufficient money")
        }
    }

    fun doLogout() {
        myAccount?.getName()?.let { messageUtils.goodbyMessages(it) }
        cacheUsername = ""
        myAccount = null
    }

    fun doTransfer(target: String, transfer: Int) {
        if (isCantTransfer(target)) return

        if (oweFromTable.containsKey(myAccount?.getName())) {
            val owedFromAccount: MyAccount? = accountTable[oweFromTable[myAccount?.getName()]]
            val movingBalance: Int? = owedFromAccount?.increaseBalance(transfer)

            if (transfer > movingBalance!!) {
                myAccount?.decreaseBalance(transfer - movingBalance)
                owedFromAccount?.getName()?.let { messageUtils.transferMessages(transfer - movingBalance, it) }
            }

            myAccount?.getBalance()?.let { messageUtils.currentBalanceMessages(it) }
            if (owedFromAccount?.getOweBalance()!! > 0) {
                owedFromAccount?.getName()?.let { owedFromAccount.getOweBalance().let { it1 -> messageUtils.oweMessages(it1, it, true) } }
            } else {
                oweToTable.remove(target)
                oweFromTable.remove(myAccount?.getName())
            }

        } else {
            val targetAccount: MyAccount? = accountTable[target]
            val movingBalance: Int? = myAccount?.decreaseBalance(transfer)
            movingBalance?.let { targetAccount?.increaseBalance(it) }

            if (myAccount?.getOweBalance()!! > 0) {
                oweToTable[myAccount?.getName().toString()] = targetAccount?.getName().toString()
                oweFromTable[targetAccount?.getName().toString()] = myAccount?.getName().toString()
            }

            targetAccount?.getName()?.let { messageUtils.transferMessages(movingBalance!!, it) }
            messageUtils.currentBalanceMessages(myAccount?.getBalance() ?: 0)
            if (myAccount?.getOweBalance()!! > 0) {
                targetAccount?.getName()?.let { messageUtils.oweMessages(myAccount?.getOweBalance()!!, it, false) }
            }
        }
    }



}