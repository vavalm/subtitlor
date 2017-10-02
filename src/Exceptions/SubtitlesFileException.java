package Exceptions;

import javax.annotation.processing.FilerException;

public class SubtitlesFileException extends FilerException {
    public SubtitlesFileException(String message) {
        super(message);
        System.out.println(message);
    }
}
