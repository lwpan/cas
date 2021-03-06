package org.apereo.cas;

import org.apereo.cas.util.BinaryCipherExecutor;

/**
 * This is {@link DefaultTicketCipherExecutor} that handles the encryption
 * and signing of tickets during replication.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class DefaultTicketCipherExecutor extends BinaryCipherExecutor {
    public DefaultTicketCipherExecutor(
            final String encryptionSecretKey,
            final String signingSecretKey,
            final String secretKeyAlg,
            final int signingKeySize,
            final int encryptionKeySize) {
        super(encryptionSecretKey, signingSecretKey, signingKeySize, encryptionKeySize);
        setSecretKeyAlgorithm(secretKeyAlg);
    }
}
