import java.nio.charset.Charset
import java.util.*
import kotlin.text.StringBuilder

enum class FromStatus
{
    NULL,
    STR, BIN, DEC, HEX
}

open class AwesomeStringConverter()
{
    var encoding: Charset = charset("UTF-8")
    var debuggingMode: Boolean = false

    private var from = FromStatus.NULL
    private var source = ""

    private var bufferSize = 65536
    private var byteSize = 65536

    //인코딩을 설정합니다.
    fun setUtf8()
    {
        encoding = charset("UTF-8")
    }
    fun setUtf16()
    {
        encoding = charset("UTF-16")
    }
    fun setUtf32()
    {
        encoding = charset("UTF-32")
    }
    fun setEucKr()
    {
        encoding = charset("euc-kr")
    }
    fun setEncoding(encodingName:String )
    {
        encoding = charset(encodingName)
    }

    //변환될 값과 타입을 받습니다.
    fun fromString(s: String): AwesomeStringConverter
    {
        from = FromStatus.STR
        source = s

        return this
    }
    fun fromBin(s: String): AwesomeStringConverter
    {
        from = FromStatus.BIN
        source = s

        return this
    }
    fun fromDec(s: String): AwesomeStringConverter
    {
        from = FromStatus.DEC
        source = s

        return this
    }
    fun fromHex(s: String): AwesomeStringConverter
    {
        from = FromStatus.HEX
        source = s

        return this
    }

    fun toStr(): String
    {
        return when(from)
        {
            FromStatus.STR->source

            FromStatus.BIN, FromStatus.DEC, FromStatus.HEX->
            {
                val radix = when(from)
                {
                    FromStatus.BIN-> 2
                    FromStatus.DEC-> 10
                    FromStatus.HEX-> 16
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var bytes = Array<Byte>(tokenizer.countTokens(), {0})
                var i:Int = 0
                while(tokenizer.hasMoreTokens())
                {
                    bytes[i] = tokenizer.nextToken().toByte(radix)
                    i++
                }

                bytes.toByteArray().toString(encoding)
            }

            FromStatus.NULL-> "null"
        }
    }

    fun toBin(): String
    {
        return when(from)
        {
            FromStatus.BIN-> source

            FromStatus.STR->
            {
                var builder = StringBuilder(bufferSize)
                val bytes = source.toByteArray(encoding)

                for(e in bytes)
                {
                    builder.
                        append( String.format("%8s", Integer.toBinaryString(e.toInt())).replace(' ', '0') ).
                        append(' ')
                }

                builder.toString()
            }

            FromStatus.DEC, FromStatus.HEX->
            {
                val radix = when(from)
                {
                    FromStatus.DEC-> 10
                    FromStatus.HEX-> 16
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var builder = StringBuilder()
                while (tokenizer.hasMoreTokens())
                {
                    val byte:Int = tokenizer.nextToken().toByte(radix).toInt()
                    builder.append(String.format("%8s", Integer.toBinaryString(byte)).replace(' ', '0')).append(' ')
                }
                builder.toString()
            }

            FromStatus.NULL-> "null"
        }
    }

    fun toDec(): String
    {
        return when(from)
        {
            FromStatus.DEC-> source

            FromStatus.STR->
            {
                var builder = StringBuilder(bufferSize)
                val bytes = source.toByteArray(encoding)

                for(e in bytes)
                {
                    builder.
                        append(e.toString()).
                        append(' ')
                }

                builder.toString()
            }

            FromStatus.BIN, FromStatus.HEX->
            {
                val radix = when(from)
                {
                    FromStatus.BIN-> 2
                    FromStatus.HEX-> 16
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var builder = StringBuilder(bufferSize)
                while (tokenizer.hasMoreTokens())
                {
                    builder.append( tokenizer.nextToken().toByte(radix).toString() ).append(' ')
                }
                builder.toString()
            }

            FromStatus.NULL-> "null"
        }
    }

    fun toHex(): String
    {
        return when(from)
        {
            FromStatus.HEX-> source

            FromStatus.STR->
            {
                var builder = StringBuilder(bufferSize)
                val bytes = source.toByteArray(encoding)

                for(e in bytes)
                {
                    builder.
                        append(String.format("%X", e)).
                        append(' ')
                }

                builder.toString()
            }

            FromStatus.BIN, FromStatus.DEC->
            {
                val radix = when(from)
                {
                    FromStatus.BIN-> 2
                    FromStatus.DEC-> 10
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var builder = StringBuilder(bufferSize)
                while (tokenizer.hasMoreTokens())
                {
                    builder.append( String.format("%02X", tokenizer.nextToken().toByte(radix)) ).append(' ')
                }
                builder.toString()
            }

            FromStatus.NULL-> "null"
        }
    }
}