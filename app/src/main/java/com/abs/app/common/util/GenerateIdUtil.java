package com.abs.app.common.util;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 86d4024 (feat(register_feat_api): add GenerateIdUtil)
import java.util.UUID;

public class GenerateIdUtil {
    public static String GenerateId(){
        return UUID.randomUUID().toString();
    }
<<<<<<< HEAD

    public static String GenerateId(String salt, int limit) {
        return salt + "_" + UUID.randomUUID().toString().substring(0, limit);
    }
=======
public class GenerateIdUtil {
>>>>>>> 1491b3a (feat(register_feat_api): implement register api and handler)
=======
>>>>>>> 86d4024 (feat(register_feat_api): add GenerateIdUtil)
}
