package com.simulacros.simulacro_1.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.simulacros.simulacro_1.api.dto.request.AssignmentReq;
import com.simulacros.simulacro_1.api.dto.response.AssignmentResp;
import com.simulacros.simulacro_1.domain.entities.Submission;
import com.simulacros.simulacro_1.domain.repositories.SubmissionRepository;
import com.simulacros.simulacro_1.infrastructure.abstract_services.IAssignmentService;
import com.simulacros.simulacro_1.util.enums.SortType;
import com.simulacros.simulacro_1.util.exceptions.BadRequestException;
import com.simulacros.simulacro_1.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubmissionService implements IAssignmentService {
    /* Inyección de dependencias */
    @Autowired
    private final SubmissionRepository respository;

    @Override
    public Page<AssignmentResp> getAll(int page, int size, SortType sortType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public AssignmentResp get(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public AssignmentResp create(AssignmentReq request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public AssignmentResp update(AssignmentReq request, Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
    private Submission find(Long id) {
        return this.respository.findById(id).orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Submission")));
    }
}
