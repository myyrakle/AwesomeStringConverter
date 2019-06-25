import java.util.*;

enum FromStatus
{
    NULL,
    STR, BIN, DEC, HEX
}

public class AwesomeStringConverter
{
    private String encoding = "UTF-8";
    private int bufferSize = 65536;
    private int bytesSize = 65536;

    private static final String ERROR = "잘못된 형식입니다.";
    private boolean debuggingMode = false;

    private FromStatus from = FromStatus.NULL;

    private String source = "";

    public boolean isDebuggingMode()
    {
        return debuggingMode;
    }
    public void setDebuggingMode()
    {
        debuggingMode = true;
    }
    public void unsetDebugginMode()
    {
        debuggingMode = false;
    }

    public String getEncoding()
    {
        return encoding;
    }
    public void setUtf8()
    {
        setEncoding("UTF-8");
    }
    public void setUtf16()
    {
        setEncoding("UTF-16");
    }
    public void setUtf32()
    {
        setEncoding("UTF-32");
    }
    public void setEucKr()
    {
        setEncoding("euc-kr");
    }
    public void setEncoding(String en)
    {
        encoding = en;
    }


    //인코딩된 문자열로 반환
    public String toStr()
    {
        try
        {
            switch(from)
            {
                case STR:
                    return source;

                case BIN:
                {
                    StringTokenizer tokenizer = new StringTokenizer(source);
                    byte[] bytes = new byte[tokenizer.countTokens()];
                    int i = 0;
                    while(tokenizer.hasMoreTokens())
                    {
                        bytes[i] = (byte)Integer.parseInt(tokenizer.nextToken(), 2);
                        i++;
                    }
                    return new String(bytes, encoding);
                }

                case DEC:
                {
                    StringTokenizer tokenizer = new StringTokenizer(source);
                    byte[] bytes = new byte[tokenizer.countTokens()];
                    int i = 0;
                    while(tokenizer.hasMoreTokens())
                    {
                        bytes[i] = (byte)Integer.parseInt(tokenizer.nextToken(), 10);
                        i++;
                    }
                    return new String(bytes, encoding);
                }

                case HEX:
                {
                    StringTokenizer tokenizer = new StringTokenizer(source);
                    byte[] bytes = new byte[tokenizer.countTokens()];
                    int i = 0;
                    while(tokenizer.hasMoreTokens())
                    {
                        bytes[i] = (byte)Integer.parseInt(tokenizer.nextToken(), 16);
                        i++;
                    }
                    return new String(bytes, encoding);
                }
            }

        }
        catch(Exception e)
        {return isDebuggingMode()? e.toString() : ERROR;}
        return ERROR;
    }


    //2진수 문자열로 반환
    String toBin()
    {
        try
        {
            byte[] bytes = source.getBytes(encoding);

            switch(from)
            {
                case STR:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);

                    for(byte e : bytes)
                        buffer.append(
                                String.format("%8s",Integer.toBinaryString(e&0xff))
                                        .replace(' ', '0')
                        ).append(' ');

                    return buffer.toString();
                }

                case BIN:
                    return source;

                case DEC:
                {
                    StringTokenizer tokenizer = new StringTokenizer(source);
                    StringBuilder buffer = new StringBuilder(bufferSize);

                    while(tokenizer.hasMoreTokens())
                    {
                        int e = Integer.parseInt(tokenizer.nextToken());
                        buffer.append(
                                String.format("%8s", Integer.toBinaryString(e))
                                        .replace(' ', '0')
                        ).append(' ');
                    }
                    return buffer.toString();
                }


                case HEX:
                {
                    StringTokenizer tokenizer = new StringTokenizer(source);
                    StringBuilder buffer = new StringBuilder(bufferSize);

                    while(tokenizer.hasMoreTokens())
                    {;
                        int e = Integer.parseInt("EC" +tokenizer.nextToken(), 16);

                        buffer.append(
                                String.format("%8s", Integer.toBinaryString(e&0xff))
                                        .replace(' ', '0')
                        ).append(' ');
                    }
                    return buffer.toString();
                }
            }
        }
        catch(Exception e)
        {return isDebuggingMode()? e.toString() : ERROR;}
        return ERROR;
    }


    //10진수 문자열로 반환
    String toDec()
    {
        try
        {
            switch(from)
            {
                case STR:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);

                    byte[] bytes = source.getBytes(encoding);
                    for(byte e : bytes)
                        buffer.append(e&0xff).append(' ');

                    return buffer.toString();
                }


                case BIN:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);
                    StringTokenizer tokenizer = new StringTokenizer(source);

                    while(tokenizer.hasMoreTokens())
                    {
                        int e = Integer.parseInt(tokenizer.nextToken(), 2);
                        buffer.append(String.format("%d",e))
                                .append(' ');
                    }
                    return buffer.toString();
                }


                case DEC:
                    return source;

                case HEX:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);
                    StringTokenizer tokenizer = new StringTokenizer(source);

                    while(tokenizer.hasMoreTokens())
                    {
                        int e = Integer.parseInt(tokenizer.nextToken(), 16);
                        buffer.append(String.format("%d",e))
                                .append(' ');
                    }
                    return buffer.toString();
                }
            }
        }
        catch(Exception e)
        {return isDebuggingMode()? e.toString() : ERROR;}
        return ERROR;
    }


    //16진수 문자열로 변경
    String toHex()
    {
        try
        {
            switch(from)
            {
                case STR:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);
                    byte[] bytes = source.getBytes(encoding);

                    for(byte e : bytes)
                        buffer.append(String.format("%02X", e))
                                .append(' ');

                    return buffer.toString();
                }

                case BIN:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);
                    StringTokenizer tokenizer = new StringTokenizer(source);

                    while(tokenizer.hasMoreTokens())
                    {
                        int e = Integer.parseInt(tokenizer.nextToken(), 2);
                        buffer.append(String.format("%02X",e))
                                .append(' ');
                    }
                    return buffer.toString();
                }

                case DEC:
                {
                    StringBuilder buffer = new StringBuilder(bufferSize);
                    StringTokenizer tokenizer = new StringTokenizer(source);

                    while(tokenizer.hasMoreTokens())
                    {
                        int e = Integer.parseInt(tokenizer.nextToken(), 10);
                        buffer.append(String.format("%02X",e))
                                .append(' ');
                    }
                    return buffer.toString();
                }

                case HEX:
                    return source;
            }
        }
        catch(Exception e)
        {return isDebuggingMode()? e.toString() : ERROR;}
        return ERROR;
    }

    public AwesomeStringConverter fromString(String s)
    {
        from = FromStatus.STR;
        source = s;

        return this;
    }
    public AwesomeStringConverter fromBin(String s)
    {
        from = FromStatus.BIN;
        source = s;

        return this;
    }
    public AwesomeStringConverter fromDec(String s)
    {
        from = FromStatus.DEC;
        source = s;

        return this;
    }
    public AwesomeStringConverter fromHex(String s)
    {
        from = FromStatus.HEX;
        source = s;

        return this;
    }
}
