package com.teb.kilimanjaro.utils;

import java.io.File;

/**
 * Created by yangbin on 16/9/5.
 */
public class FileUtils {
    /**
     * 文件是否存在
     *
     * @return
     */
    public static boolean fileIsExists (File file) {
        try {
            if (!file.exists ()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
