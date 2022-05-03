import me.tongfei.progressbar.ProgressBar
import org.bitcoinj.core.Address
import org.bitcoinj.core.ECKey
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.bitcoinj.wallet.Wallet
import java.io.File
import java.security.SecureRandom

class CryptoBrute(
    private val basePath: String,
    private val successPath: String
) {

    private val wallets = run {
        val progressbar = ProgressBar(
            "${Thread.currentThread().name} Wallet",
            KEYGEN_SIZE.toLong()
        )

        (1..KEYGEN_SIZE)
            .map {
                generateWallet(
                    SecureRandom()
                        .generateSeed(30)
                        //.also { println(Base58.encode(it)) }
                        .let(ECKey::fromPrivate)
                        //.also { println(it) }
                ).also { progressbar.step() }
            }
    }

    constructor(): this(
        BASE_FILE,
        SUCCESS_OUT_FILE
    )

    private fun generateWallet(
        key: ECKey,
        netParams: MainNetParams = MainNetParams.get()
    ): Wallet = Wallet.createBasic(netParams).apply {
        importKey(key)
        addWatchedAddress(
            Address.fromKey(
                netParams,
                key,
                Script.ScriptType.P2PKH
            )
        )
    }

    fun searchForAddress() = run {
        File(basePath)
            .inputStream()
            .bufferedReader()
            .useLines {  lines -> lines
                .map { it.split(' ', '\t').first() }
                .filter { it.first() == '1' }
                .forEach { address ->
                    wallets.forEach { wallet -> wallet.checkAddress(
                        wallet.watchedAddresses.first(),
                        address
                    ) }
                }
            }
    }

    private fun Wallet.checkAddress(
        address: Address,
        other: String
    ) {
        val (pub, priv) = importedKeys
            .first()
            .run { publicKeyAsHex to privKey }

        val msg = "\n$address $pub $priv\n"

        if (address.toString() == other) {
            File(successPath)
                .appendText("SUCCESS:$msg")
            println(msg)
            return
        }
    }
}