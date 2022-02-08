package com.asu.project.hospital.Exception;

public class EmailUsedException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public EmailUsedException() {
        super("Email is already used!");
    }
}
