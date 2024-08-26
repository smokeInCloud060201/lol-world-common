package vn.com.lol.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectUtil {
    public static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return bos.toByteArray();
    }


    public static <T> T deserializeObject(byte[] bytes, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            Object obj = ois.readObject();
            return clazz.cast(obj);
        }
    }
}
