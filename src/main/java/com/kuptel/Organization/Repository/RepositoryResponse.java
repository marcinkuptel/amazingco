package com.kuptel.Organization.Repository;

public enum RepositoryResponse {
    OK (0),
    UPDATE_FAILED(1);

    private final int status;

    RepositoryResponse(int status){
        this.status = status;
    }
}
