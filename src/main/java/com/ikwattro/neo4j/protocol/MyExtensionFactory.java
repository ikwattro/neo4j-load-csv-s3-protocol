package com.ikwattro.neo4j.protocol;

import org.neo4j.annotations.service.ServiceProvider;
import org.neo4j.graphdb.security.URLAccessRule;
import org.neo4j.kernel.extension.ExtensionFactory;
import org.neo4j.kernel.extension.ExtensionType;
import org.neo4j.kernel.extension.context.ExtensionContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;

import java.net.URL;


@ServiceProvider
public class MyExtensionFactory extends ExtensionFactory<MyExtensionFactory.Dependencies> {

    private boolean alreadyRegistered = false;

    public MyExtensionFactory() {
        super(ExtensionType.DATABASE, "LoadCSVExtensionFactory");
    }

    @Override
    public Lifecycle newInstance(ExtensionContext context, Dependencies dependencies) {
        if (!alreadyRegistered) {
            try {
                URL.setURLStreamHandlerFactory(new S3URLStreamHandlerFactory());
                alreadyRegistered = true;
            } catch (Exception e) {
                // Do nothing, it was already registered
            }
        }

        URLAccessRule urlAccessRule = dependencies.urlAccessRule();

        return LifecycleAdapter.onInit(() -> {
            URLAccessRule s3Rule = (config, url) -> {
                if ("s3".equals(url.getProtocol())) {
                    return url;
                }
                return urlAccessRule.validate(config, url);
            };
            context.dependencySatisfier().satisfyDependency(s3Rule);
        });
    }

    interface Dependencies {
        URLAccessRule urlAccessRule();
    }
}
