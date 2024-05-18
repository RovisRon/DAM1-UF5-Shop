package exception;

public class LimitLoginException extends Exception{

    public LimitLoginException(){
        super();
    }

    public LimitLoginException(String message){
        super(message);
    }

    public LimitLoginException(String message, Throwable cause){
        super(message, cause);
    }

    public LimitLoginException(Throwable cause){
        super(cause);
    }
}
