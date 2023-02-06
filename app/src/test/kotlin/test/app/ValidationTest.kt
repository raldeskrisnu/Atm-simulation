package test.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ValidationTest {

    private var validation: Validation? = null
    private val outputStreamCaptor: ByteArrayOutputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        validation = Validation()
        System.setOut(PrintStream(outputStreamCaptor))
        validation?.attempLogin("raldes")
    }

    @Test
    fun attempLogin_test() {
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("Hello, welcome raldes!", outPutStreamCaptorSplit[0].trim { it <= ' ' })
        assertEquals("Your balance $0", outPutStreamCaptorSplit[1].trim { it <= ' ' })
    }

    @Test
    fun doDeposit_test() {
        validation?.doDeposit(50)
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("Your balance $50", outPutStreamCaptorSplit[2].trim())
    }

    @Test
    fun doWithDraw_test() {
        validation?.doDeposit(50)
        validation?.doWithDraw(10)
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("Your balance $40", outPutStreamCaptorSplit[3].trim());
    }

    @Test
    fun doLogout_test() {
        validation?.doLogout()
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("goodby raldes", outPutStreamCaptorSplit[2].trim())
    }

    @Test
    fun doTransferWhenMoreThanZeroBalance_test() {
        validation?.doLogout()
        validation?.attempLogin("abc")
        validation?.doTransfer("raldes", 10)
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("cant transfer balance less than 0", outPutStreamCaptorSplit[5].trim())
    }

    @Test
    fun doWithDrawWithInsufficientMoney_test() {
        validation?.doDeposit(50)
        validation?.doWithDraw(51)
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("Insufficient money", outPutStreamCaptorSplit[3].trim());
    }

    @Test
    fun doTransferWhenLessThanBalance_test() {
        validation?.doDeposit(50)
        validation?.doLogout()
        validation?.attempLogin("abc")
        validation?.doDeposit(40)
        validation?.doTransfer("raldes", 30)
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("Transferred $30 to raldes", outPutStreamCaptorSplit[7].trim());
        assertEquals("Your balance $10", outPutStreamCaptorSplit[8].trim());
    }

    @Test
    fun doLoginWhenSessionStillActive_test() {
        validation?.attempLogin("abc")
        val outPutStreamCaptorSplit = outputStreamCaptor.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        assertEquals("session abc still active please change other command",outPutStreamCaptorSplit[2])
    }
}