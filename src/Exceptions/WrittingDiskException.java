package Exceptions;

import java.io.IOException;

public class WrittingDiskException extends IOException {
    public WrittingDiskException(String message){
        super(message);
        System.out.println(message);
    }
}
