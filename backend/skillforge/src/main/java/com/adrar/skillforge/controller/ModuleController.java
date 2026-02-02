package com.adrar.skillforge.controller;

import com.adrar.skillforge.dto.module.CreateModuleRequest;
import com.adrar.skillforge.dto.module.ModuleResponse;
import com.adrar.skillforge.dto.module.PatchModuleRequest;
import com.adrar.skillforge.entity.Module;
import com.adrar.skillforge.service.module.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping("/courses/{coursePublicId}/modules")
    public ResponseEntity<List<ModuleResponse>> findAllByCourse(@PathVariable UUID coursePublicId) {
        List<ModuleResponse> modules = moduleService.findAllByCourse(coursePublicId)
            .stream()
            .map(ModuleResponse::from)
            .toList();

        return ResponseEntity.ok(modules);
    }

    @PostMapping("/courses/{coursePublicId}/modules")
    public ResponseEntity<ModuleResponse> create(
        @PathVariable UUID coursePublicId,
        @Valid @RequestBody CreateModuleRequest request
    ) {
        Module created = moduleService.create(coursePublicId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ModuleResponse.from(created));
    }

    @PatchMapping("/modules/{modulePublicId}")
    public ResponseEntity<ModuleResponse> patch(
        @PathVariable UUID modulePublicId,
        @Valid @RequestBody PatchModuleRequest request
    ) {
        Module updated = moduleService.patch(modulePublicId, request);
        return ResponseEntity.ok(ModuleResponse.from(updated));
    }

    @DeleteMapping("/modules/{modulePublicId}")
    public ResponseEntity<Void> delete(@PathVariable UUID modulePublicId) {
        moduleService.delete(modulePublicId);
        return ResponseEntity.noContent().build();
    }
}
