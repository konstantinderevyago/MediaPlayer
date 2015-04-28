package by.itrex.jetfirer.mediaplayer.enums;

/**
 * Created by Konstantin on 25.04.2015.
 */
public enum Repeat {

    REPEAT_OFF, REPEAT_ALL, REPEAT_SINGLE;

    public static Repeat toRepeat(String repeatString) {
        try {
            return valueOf(repeatString);
        } catch (Exception ex) {
            return REPEAT_OFF;
        }
    }

}
