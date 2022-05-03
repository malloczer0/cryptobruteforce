import org.bitcoinj.core.Context
import org.bitcoinj.params.MainNetParams
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    val (base, success) = args[0] to args[1]
    val bruteBlock = {
        Context.propagate(Context(MainNetParams.get()))
        while (true) {
            CryptoBrute(base, success).searchForAddress()
        }
    }
    val thread by lazy {{ thread(block = bruteBlock) }}
    thread()
    bruteBlock()
}