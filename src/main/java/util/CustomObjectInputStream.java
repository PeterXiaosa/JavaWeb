package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;

public class CustomObjectInputStream extends ObjectInputStream {
    protected CustomObjectInputStream() throws IOException, SecurityException {
        super();
    }

    public CustomObjectInputStream(InputStream arg0) throws IOException {
        super(arg0);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException{
        String name = desc.getName();
        try {
            if (name.startsWith("com.peter.guardianangel.bean")){
                name = name.replace("com.peter.guardianangel.", "");
            }
            return Class.forName(name);
        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return super.resolveClass(desc);
    }
}
