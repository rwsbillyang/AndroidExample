package cn.niukid.myexampleapplication.note;

/**
 * copy from https://github.com/greenrobot/greenDAO/tree/master/examples/DaoExample/src/main/java/org/greenrobot/greendao/example
 */

import org.greenrobot.greendao.converter.PropertyConverter;

public class NoteTypeConverter implements PropertyConverter<NoteType, String> {
    @Override
    public NoteType convertToEntityProperty(String databaseValue) {
        return NoteType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteType entityProperty) {
        return entityProperty.name();
    }
}
