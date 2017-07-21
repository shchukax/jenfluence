package de.sprengnetter.jenkins.plugins.jenfluence.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Outputstream that logs the passing bytes.
 */

public class LoggingOutputStream extends FilterOutputStream {

    private final StringBuilder sb = new StringBuilder();
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    /**
     * Constructor which takes the {@link OutputStream} which should get logged.
     *
     * @param out The {@link OutputStream} which should get logged.
     */
    public LoggingOutputStream(final OutputStream out) {
        super(out);
    }

    /**
     * Returns a {@link StringBuilder} which holds the outputstream-"log".
     *
     * @param charset The desired charset.
     * @return The {@link StringBuilder} holding the outputstream log.
     */
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
