package io.kalishak.galacticraftlegacy.aunified.data.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.loaders.CompositeModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.ConditionalModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class CustomModelGenerator implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final String namespace;
    private final Map<Identifier, CustomLoaderBuilder> modelBuilders = new HashMap<>();

    public CustomModelGenerator(PackOutput output, String namespace) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
        this.namespace = namespace;
    }

    public abstract void generateModels();

    protected String prefixed(String name) {
        return "entity/" + name;
    }

    protected ObjModelBuilder obj(String name) {
        return loader(name, id -> new ObjModelBuilder().modelLocation(id.withPath(path -> "models/" + path + ".obj")));
    }

    protected CompositeModelBuilder composite(String childName, String modelId) {
        return loader(modelId, id -> new CompositeModelBuilder().child(childName, id));
    }

    protected ConditionalModelBuilder conditional(String modelId) {
        return loader(modelId, _ -> new ConditionalModelBuilder());
    }

    protected <L extends CustomLoaderBuilder> L loader(String modelId, Function<Identifier, L> builder) {
        Identifier id = Identifier.fromNamespaceAndPath(this.namespace, modelId);
        L loader = builder.apply(id);
        this.modelBuilders.put(id, loader);

        return loader;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        generateModels();

        return CompletableFuture.allOf(this.modelBuilders.entrySet().stream().map(e -> {
            Path path = this.pathProvider.json(e.getKey());
            JsonElement json = e.getValue().toJson(new JsonObject());

            return DataProvider.saveStable(cachedOutput, json, path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Data driven custom geometry for: " + this.namespace;
    }
}
