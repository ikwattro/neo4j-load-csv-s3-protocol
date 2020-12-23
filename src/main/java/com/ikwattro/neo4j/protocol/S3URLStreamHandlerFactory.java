package com.ikwattro.neo4j.protocol;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class S3URLStreamHandlerFactory implements URLStreamHandlerFactory {

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equals("s3")) {
            return new S3ProtocolHandler();
        }

        return null;
    }
}
