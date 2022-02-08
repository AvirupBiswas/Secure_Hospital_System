package com.asu.project.hospital.Exception;

public class RoleException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public RoleException() {
        super("Role is invalid!");
    }
}
