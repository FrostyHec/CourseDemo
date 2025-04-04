package org.frosty.common.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    public static void modifyStaticFinalField(Class<?> clazz, String fieldName, Object newValue) throws Exception {
        // 获取字段
//        Field field = clazz.getDeclaredField(fieldName);
//
//        // 取消字段的final修饰
//        Field modifiersField = Field.class.getDeclaredField("modifiers");
//        modifiersField.setAccessible(true);
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//
//        // 设置字段可访问
//        field.setAccessible(true);
//
//        // 修改字段的值
//        field.set(null, newValue);
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        // 获取字段
        Field field = clazz.getDeclaredField(fieldName);
        // 计算字段的偏移量
        long fieldOffset = unsafe.staticFieldOffset(field);
        // 修改字段的值
        unsafe.putInt(clazz, fieldOffset, (int) newValue);
    }
}
