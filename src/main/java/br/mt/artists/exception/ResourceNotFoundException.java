package br.mt.artists.exception;

// Exceções de domínio
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
