package com.github.jeffyjamzhd.renewed.api.server;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.ILogAgent;
import net.minecraft.ResourceLocation;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RenewedYAML {
    private static final String HEADER_INFO;
    private final LangParser languageParser;
    private final Yaml yaml;
    private final ILogAgent logger;
    private final File file;

    public RenewedYAML(File file, ILogAgent logger) {
        this.file = file;
        this.logger = logger;
        this.languageParser = new LangParser();
        this.languageParser.loadFromFile("/assets/miterenewed/lang/en_US.lang");

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setProcessComments(true);

        ConfigRepresenter representer = new ConfigRepresenter(options);
        representer.addClassTag(Config.class, Tag.MAP);

        this.yaml = new Yaml(representer, options);
    }

    @SuppressWarnings("unchecked")
    public <T> Difficulty loadFromYaml() {
        if (!file.exists()) {
            generateDefault();
            return RenewedDifficulties.EXTREME.cloneAsCustom();
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            Map<String, Object> root = yaml.load(inputStream);

            if (root == null || !root.containsKey("namespaces")) {
                logger.logWarning("[MiTE-Renewed] Config is empty or malformed.");
                return RenewedDifficulties.EXTREME.cloneAsCustom();
            }

            Difficulty custom = RenewedDifficulties.EXTREME.cloneAsCustom();
            List<Map<String, Object>> namespaces = (List<Map<String, Object>>) root.get("namespaces");

            for (Map<String, Object> nsNode : namespaces) {
                String domain = (String) nsNode.get("namespace");
                Map<String, Object> parameters = (Map<String, Object>) nsNode.get("parameters");

                if (parameters == null) continue;

                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    String path = entry.getKey();
                    T parsedValue = (T) entry.getValue();

                    // Reconstruct the ResourceLocation
                    ResourceLocation id = new ResourceLocation(domain, path);
                    DifficultyParameter<T> parameter = (DifficultyParameter<T>) DifficultyProvider.getParameter(id);

                    if (parameter != null) {
                        attemptToParseValue(custom, parameter, parsedValue);
                    } else {
                        logger.logWarning("[MiTE-Renewed] Unknown config parameter found: " + id);
                    }
                }
            }

            logger.logInfo("[MiTE-Renewed] Successfully loaded server config.");

        } catch (Exception e) {
            logger.logSevereException("[MiTE-Renewed] Failed to parse config!", e);
        }
        return RenewedDifficulties.EXTREME.cloneAsCustom();
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
            T value = (T) DifficultyProvider.defaults.get(parameter);
            paramSerialized.key = parameter.id.getResourcePath();
            paramSerialized.value = value;
            paramSerialized.comment = languageParser.translate(parameter.getDescriptionKey(null));
            parameterNamespace.parameters.add(paramSerialized);
        }

        config.namespaces = new ArrayList<>(namespaces.values());

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(HEADER_INFO.formatted(MiTERenewed.getVersionString()));
            yaml.dump(config, writer);
            logger.logInfo("[MiTE-Renewed] Generating default MiTE-Renewed server config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    private <T> void attemptToParseValue(Difficulty difficulty, DifficultyParameter<T> parameter, Object value) {
        try {
            T castedValue = null;

            if (value instanceof Number) {
                Number num = (Number) value;
                Class<T> typeClass = (Class<T>) DifficultyProvider.defaults.get(parameter).getClass(); // Assuming you can get the expected class

                if (typeClass == Integer.class) castedValue = (T) Integer.valueOf(num.intValue());
                else if (typeClass == Double.class) castedValue = (T) Double.valueOf(num.doubleValue());
                else if (typeClass == Float.class) castedValue = (T) Float.valueOf(num.floatValue());
                else if (typeClass == Long.class) castedValue = (T) Long.valueOf(num.longValue());
            }
            else {
                castedValue = (T) value;
            }

            castedValue = parameter.sanitizeValue(difficulty, castedValue);
            difficulty.setParamValue(parameter, castedValue);

        } catch (ClassCastException e) {
            logger.logWarning("[MiTE-Renewed] Invalid value type for parameter " + parameter.id + ". Using default.");
        }
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
        public String comment;
        public T value;
    }

    private static class ConfigRepresenter extends Representer {
        public ConfigRepresenter(DumperOptions options) {
            super(options);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean,
                                                      Property property,
                                                      Object propertyValue,
                                                      Tag customTag) {
            if (javaBean instanceof ParameterNamespace && "parameters".equals(property.getName())) {
                List<Parameter<?>> params = (List<Parameter<?>>) propertyValue;
                List<NodeTuple> tuples = new ArrayList<>();

                for (Parameter<?> param : params) {
                    Node keyNode = representData(param.key);
                    Node valueNode = representData(param.value);
                    List<CommentLine> comments = new ArrayList<>();

                    if (!tuples.isEmpty()) {
                        comments.add(new CommentLine(null, null, "", CommentType.BLANK_LINE));
                    }

                    if (param.comment != null && !param.comment.isEmpty()) {
                        comments.add(new CommentLine(null, null, " " + param.comment, CommentType.BLOCK));
                    }

                    keyNode.setBlockComments(comments);
                    tuples.add(new NodeTuple(keyNode, valueNode));
                }

                CommentLine newLine = new CommentLine(null, null, "", CommentType.BLANK_LINE);
                MappingNode mapNode = new MappingNode(Tag.MAP, tuples, DumperOptions.FlowStyle.BLOCK);
                mapNode.setEndComments(Collections.singletonList(newLine));

                NodeTuple defaultTuple = super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
                return new NodeTuple(defaultTuple.getKeyNode(), mapNode);
            }
            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
    }

    private static class LangParser {
        private final Properties properties = new Properties();

        public void loadFromFile(String filePath) {
            try (InputStream is = getClass().getResourceAsStream(filePath)) {
                if (is != null) {
                    try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                        properties.load(reader);
                    }
                } else {
                    System.out.println("Could not find lang file at: " + filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String translate(String key) {
            return properties.getProperty(key, "No description available.");
        }
    }

    static {
        HEADER_INFO = """
                # %s Server Configuration File
                # For more information on configuring the server, you can view the
                # online documentation at https://github.com/MinecraftIsTooEasy/mite-renewed/wiki
                
                """;
    }
}
