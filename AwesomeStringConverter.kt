package com.myyrakle.awesomeconverter

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

    @kotlin.ExperimentalUnsignedTypes
    fun toStr(): String=
        when(from)
        {
            FromStatus.STR ->source

            FromStatus.BIN, FromStatus.DEC, FromStatus.HEX ->
            {
                val radix = when(from)
                {
                    FromStatus.BIN -> 2
                    FromStatus.DEC -> 10
                    FromStatus.HEX -> 16
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var bytes = Array<Byte>(tokenizer.countTokens(), {0})
                var i = 0
                while(tokenizer.hasMoreTokens())
                {
                    bytes[i] = try {tokenizer.nextToken().toUByte(radix).toByte()} catch(e:Exception) {0.toByte()}
                    i++
                }

                bytes.toByteArray().toString(encoding)
            }

            FromStatus.NULL -> "null"
        }


    @kotlin.ExperimentalUnsignedTypes
    fun toBin(): String=
        when(from)
        {
            FromStatus.BIN -> source

            FromStatus.STR ->
            {
                var builder = StringBuilder(bufferSize)
                val bytes = source.toByteArray(encoding)

                for(e in bytes)
                {
                    val formatted =
                        try
                        {
                            String.format("%8s", Integer.toBinaryString(e.toInt().and(0xff))).
                                replace(' ', '0')
                        }
                        catch(e:Exception){"???"}

                    builder.append(formatted).append(' ')
                }

                builder.toString()
            }

            FromStatus.DEC, FromStatus.HEX ->
            {
                val radix = when(from)
                {
                    FromStatus.DEC -> 10
                    FromStatus.HEX -> 16
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var builder = StringBuilder()
                while (tokenizer.hasMoreTokens())
                {
                    val formatted = try
                    {
                        val byte: Int = tokenizer.nextToken().toInt(radix)

                        if(byte > UByte.MAX_VALUE.toInt())
                            String.format("%s", Integer.toBinaryString(byte))
                        else
                            String.format("%8s", Integer.toBinaryString(byte)).replace(' ', '0')
                    }
                    catch(e:Exception)
                    {"???"}

                    builder.append(formatted).append(' ')
                }
                builder.toString()
            }

            FromStatus.NULL -> "null"
        }

    @kotlin.ExperimentalUnsignedTypes
    fun toDec(): String=
        when(from)
        {
            FromStatus.DEC -> source

            FromStatus.STR ->
            {
                var builder = StringBuilder(bufferSize)
                val bytes = source.toByteArray(encoding)

                for(e in bytes)
                {
                    builder.
                        append(e.toUByte().toString()).
                        append(' ')
                }

                builder.toString()
            }

            FromStatus.BIN, FromStatus.HEX ->
            {
                val radix = when(from)
                {
                    FromStatus.BIN -> 2
                    FromStatus.HEX -> 16
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var builder = StringBuilder(bufferSize)
                while (tokenizer.hasMoreTokens())
                {
                    val formatted = try
                    {
                        tokenizer.nextToken().toLong(radix).toString()
                    }
                    catch(e:Exception)
                    {"???"}

                    builder.append(formatted).append(' ')
                }
                builder.toString()
            }

            FromStatus.NULL -> "null"
        }


    fun toHex(): String=
        when(from)
        {
            FromStatus.HEX -> source

            FromStatus.STR ->
            {
                var builder = StringBuilder(bufferSize)
                val bytes = source.toByteArray(encoding)

                for(e in bytes)
                {
                    val formatted = try { String.format("%X", e) }
                    catch(e:Exception) {"???"}

                    builder.
                        append(formatted).
                        append(' ')
                }

                builder.toString()
            }

            FromStatus.BIN, FromStatus.DEC ->
            {
                val radix = when(from)
                {
                    FromStatus.BIN -> 2
                    FromStatus.DEC -> 10
                    else -> 0
                }

                var tokenizer = StringTokenizer(source)
                var builder = StringBuilder(bufferSize)
                while (tokenizer.hasMoreTokens())
                {
                    val formatted = try{
                        String.format("%02X", tokenizer.nextToken().toLong(radix))
                    }
                    catch(e:Exception) { "???" }
                    builder.append( formatted ).append(' ')
                }
                builder.toString()
            }

            FromStatus.NULL -> "null"
        }
}