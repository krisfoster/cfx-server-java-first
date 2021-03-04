package com.cxf.server.graal;

import org.apache.cxf.common.spi.GeneratedClassClassLoaderCapture;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DumpingClassLoaderCapturer implements GeneratedClassClassLoaderCapture {

    public DumpingClassLoaderCapturer() {
        //
    }

    private final Map<String, byte[]> classes = new ConcurrentHashMap<>();

    public void dumpTo(File file) throws IOException {
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException("The dump location does not exist or is not a directory: " + file);
        }

        for (Map.Entry<String, byte[]> entry: classes.entrySet()) {
            final Path path = file.toPath().resolve(StringUtils.periodToSlashes(entry.getKey()) + ".class");
            Files.createDirectories(path.getParent());

            try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
                out.write(entry.getValue());
            }
        }
    }

    @Override
    public void capture(String className, byte[] bytes) {
        classes.putIfAbsent(className, bytes);
    }
}