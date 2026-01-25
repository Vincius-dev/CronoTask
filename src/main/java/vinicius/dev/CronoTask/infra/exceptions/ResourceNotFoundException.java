package vinicius.dev.CronoTask.infra.exceptions;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException( String message )
    {
        super( message );
    }
}
