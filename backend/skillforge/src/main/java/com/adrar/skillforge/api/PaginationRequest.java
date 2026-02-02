package com.adrar.skillforge.api;

import com.adrar.skillforge.exception.BadRequestException;
import org.springframework.data.domain.Pageable;

public final class PaginationRequest {

    public static final int MAX_SIZE = 50;

    private PaginationRequest() {
    }

    public static Pageable enforce(Pageable pageable) {
        if (pageable == null) {
            throw new BadRequestException("Paramètres de pagination manquants.");
        }

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        if (page < 0) {
            throw new BadRequestException("Le paramètre page doit être >= 0.");
        }

        if (size < 1) {
            throw new BadRequestException("Le paramètre size doit être >= 1.");
        }

        if (size > MAX_SIZE) {
            throw new BadRequestException("Le paramètre size ne doit pas dépasser " + MAX_SIZE + ".");
        }

        return pageable;
    }
}
