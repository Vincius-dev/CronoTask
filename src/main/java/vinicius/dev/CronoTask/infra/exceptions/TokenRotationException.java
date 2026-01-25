package vinicius.dev.CronoTask.infra.exceptions;

public class TokenRotationException extends RuntimeException
{
    public TokenRotationException( String message )
    {
        super( message );
    }
}
