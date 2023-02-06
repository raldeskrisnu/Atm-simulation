package test.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MyAccountTest {

    var myAccount: MyAccount? = null
    val name: String = "raldes"
    val balance = 10
    val oweBalance = 0

    @BeforeEach
    fun setUp() {
        myAccount = MyAccount(name)
    }

    @Test
    fun init_test() {
        myAccount?.increaseBalance(balance)
        assertEquals(name, myAccount?.getName())
        assertEquals(balance, myAccount?.getBalance())
        assertEquals(oweBalance, myAccount?.getOweBalance())
    }

    @Test
    fun increasedBalance_test() {
        myAccount?.increaseBalance(balance)

        assertEquals(balance, myAccount?.getBalance())
        assertEquals(oweBalance, myAccount?.getOweBalance())
    }

    @Test
    fun decreasedBalance_test() {
        val decrese = 0
        myAccount?.increaseBalance(balance)
        myAccount?.decreaseBalance(decrese)
        assertEquals(balance - decrese, myAccount?.getBalance())
    }

    @Test
    fun transferBalanceMoreThanOwe_test() {
        val decreasedBalance = 20
        val increasedBalance = 10

        myAccount?.increaseBalance(balance)
        myAccount?.decreaseBalance(decreasedBalance)
        val transferBalance: Int? = myAccount?.increaseBalance(increasedBalance)

        assertEquals(decreasedBalance - balance, transferBalance)
    }
}