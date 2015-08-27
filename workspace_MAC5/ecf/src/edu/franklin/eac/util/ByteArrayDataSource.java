package edu.franklin.eac.util;

import java.io.*;
import javax.activation.DataSource;

class ByteArrayDataSource
    implements DataSource
{

    ByteArrayDataSource(String data, String type)
    {
        try
        {
            this.data = data.getBytes("iso-8859-1");
        }
        catch(UnsupportedEncodingException uex) { }
        this.type = type;
    }

    public InputStream getInputStream()
        throws IOException
    {
        if(data == null)
            throw new IOException("no data");
        else
            return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        throw new IOException("cannot do this");
    }

    public String getContentType()
    {
        return type;
    }

    public String getName()
    {
        return "dummy";
    }

    private byte data[];
    private String type;
}