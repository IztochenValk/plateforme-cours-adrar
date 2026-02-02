package com.adrar.skillforge.service.module;

import com.adrar.skillforge.dto.module.CreateModuleRequest;
import com.adrar.skillforge.dto.module.PatchModuleRequest;
import com.adrar.skillforge.entity.Module;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    Module findByPublicId(UUID publicId);

    List<Module> findAllByCourse(UUID coursePublicId);

    Module create(UUID coursePublicId, CreateModuleRequest request);

    Module patch(UUID modulePublicId, PatchModuleRequest request);

    void delete(UUID modulePublicId);
}
