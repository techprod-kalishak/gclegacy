package io.kalishak.galacticraftlegacy.transfer.entity;

import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;

public interface MutableResourceHandler<R extends Resource> extends ResourceHandler<R>, IndexModifier<R> {
}
