/*
 * This Spock specification was auto generated by running 'gradle init --type groovy-library'
 */

import spock.lang.Specification
import io.ark.core.*
import com.google.common.io.BaseEncoding

class CryptoTest extends Specification {

    def "Passphrase 'this is a top secret passphrase' should generate address 'AGeYmgbg2LgGxRW2vNNJvQ88PknEJsYizC' on Mainnet"(){
      when:
        def address = Crypto.getAddress(Crypto.getKeys("this is a top secret passphrase"))
      then:
        address == 'AGeYmgbg2LgGxRW2vNNJvQ88PknEJsYizC'

    }

    def "Passphrase 'this is a top secret passphrase' should generate address 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib' on Devnet"(){
      when:
        Crypto.networkVersion = Network.Devnet.prefix
        def address = Crypto.getAddress(Crypto.getKeys("this is a top secret passphrase"))
      then:
        address == 'D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib'
    }

    def "Transaction should create and verify"() {
        when:
          def tx = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")
          println tx.toJson()
        then:
          Crypto.verify(tx)
    }

    def "Transaction should create and serialize/deserialize to JSON"() {
        when:
          def tx = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase", "this is a top secret second passphrase")
        then:
          tx == Transaction.fromJson(tx.toJson())
    }

    def "Transaction with second passphrase should create and verify"() {
        when:
          def tx = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase", "this is a top secret second passphrase")
          def secondPublicKeyHex=Crypto.getKeys("this is a top secret second passphrase").getPubKey()
          secondPublicKeyHex = BaseEncoding.base16().lowerCase().encode secondPublicKeyHex
        then:
          Crypto.verify(tx) && Crypto.secondVerify(tx, secondPublicKeyHex)
    }

    def "Transaction should create and verify should fail if amount is modified"() {
        when:
          def tx = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")
          tx.amount = 100000000000000
        then:
          !Crypto.verify(tx) && tx == Transaction.fromJson(tx.toJson())
    }

    def "Transaction should create and verify should fail if fee is modified"() {
        when:
          def tx = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")
          tx.fee = 11
        then:
          !Crypto.verify(tx) && tx == Transaction.fromJson(tx.toJson())
    }

    def "Transaction should create and verify should fail if recipientId is modified"() {
        when:
          def tx = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")
          tx.recipientId = "AavdJLxqBnWqaFXWm2xNirNArJNUmyUpup"
        then:
          !Crypto.verify(tx) && tx == Transaction.fromJson(tx.toJson())
    }

    def "Transaction vote should create and verify"() {
        when:
          def tx = Transaction.createVote(["+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"], "this is a top secret passphrase")
        then:
          Crypto.verify(tx) && tx == Transaction.fromJson(tx.toJson())
    }

    def "Transaction delegate should create and verify"() {
        when:
          def tx = Transaction.createDelegate("polopolo", "this is a top secret passphrase")
        then:
          Crypto.verify(tx) && tx == Transaction.fromJson(tx.toJson())
    }
}