package com.simulacros.simulacro_1.infrastructure.abstract_services;

import org.springframework.data.domain.Page;

import com.simulacros.simulacro_1.util.enums.SortType;

public interface CrudService<RQ, RS, ID> {
    public Page<RS> getAll(int page, int size, SortType sortType);
    
    public RS get(ID id);

    public RS create(RQ request);
    
    public RS update(RQ request, ID id);

    public void delete(ID id);
}
