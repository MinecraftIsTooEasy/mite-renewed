package com.github.jeffyjamzhd.renewed.api.server;

import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import net.minecraft.ILogAgent;
import net.minecraft.ResourceLocation;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RenewedYAML {
    private final Yaml yaml = new Yaml();
    private final ILogAgent logger;
    private final File file;

    public RenewedYAML(File file, ILogAgent logger) {
        this.file = file;
        this.logger = logger;

        if (!file.exists()) {
            generateDefault();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void generateDefault() {
        Config config = new Config();
        HashMap<String, ParameterNamespace> namespaces = new HashMap<>();

        for (DifficultyParameter<?> parameter : DifficultyProvider.orderedList) {
            ResourceLocation id = parameter.id;
            ParameterNamespace parameterNamespace;

            if (!namespaces.containsKey(id.getResourceDomain())) {
                parameterNamespace = new ParameterNamespace();
                parameterNamespace.namespace = id.getResourceDomain();
                namespaces.put(id.getResourceDomain(), parameterNamespace);
            } else {
                parameterNamespace = namespaces.get(id.getResourceDomain());
            }

            Parameter<T> paramSerialized = new Parameter<>();
            paramSerialized.key = parameter.id.getResourcePath();
            paramSerialized.value = (T) DifficultyProvider.defaults.get(parameter);
            parameterNamespace.parameters.add(paramSerialized);
        }

        config.namespaces = new ArrayList<>(namespaces.values());

        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(config, writer);
            logger.logInfo("[MiTE-Renewed] Generating default MiTE-Renewed server config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadYaml() {
    }

    private void saveYaml() {
    }

    private static class Config {
        public List<ParameterNamespace> namespaces;
    }

    private static class ParameterNamespace {
        public String namespace;
        public List<Parameter<?>> parameters = new ArrayList<>();
    }

    private static class Parameter<T> {
        public String key;
        public T value;
    }
}
