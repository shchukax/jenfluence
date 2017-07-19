package de.sprengnetter.jenkins.plugins.jenfluence.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */

public class LoggingOutputStream extends FilterOutputStream {

    private final StringBuilder sb = new StringBuilder();
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public LoggingOutputStream(final OutputStream out) {
        super(out);
    }

    public StringBuilder getStringBuilder(final Charset charset) {
        final byte[] entity = baos.toByteArray();
        sb.append(new String(entity, charset));
        return sb;
    }

    @Override
    public void write(final int i) throws IOException {
        baos.write(i);
        out.write(i);
    }
}
