package test.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream


class AppTest {

    private val outputStreamCaptor: ByteArrayOutputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @Test
    fun main_test() {
        val inputLogin = "login Alice\n"
        val inputDeposit = "deposit 100\n"
        val inputInvalidDepositBalance = "deposit a\n"
        val inputWithdraw = "withdraw 50\n"
        val inputInvalidWithdrawBalance = "withdraw a\n"
        val inputTransfer = "transfer Bob 50\n"
        val inputInvalidTransferBalance = "transfer Bob a\n"
        val inputLogout = "logout\n"
        val inputInvalid = "invalid test\n"
        val inputExit = "exit\n"

        var inputStr = inputLogin + inputDeposit + inputInvalidDepositBalance
        inputStr += inputWithdraw + inputInvalidWithdrawBalance
        inputStr += inputTransfer + inputInvalidTransferBalance
        inputStr += inputLogout + inputInvalid + inputExit

        val input: InputStream = ByteArrayInputStream(inputStr.toByteArray())
        System.setIn(input)
        main()

        val split = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        assertEquals("Hello, welcome Alice!", split[0].trim { it <= ' ' })
        assertEquals("Your balance $0", split[1].trim { it <= ' ' })
        assertEquals("Your balance $100", split[2].trim { it <= ' ' })
        assertEquals("Input is not a number!", split[3].trim { it <= ' ' })
    }
}