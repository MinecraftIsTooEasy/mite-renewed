package com.github.jeffyjamzhd.renewed.util;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.minecraft.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class NBTSizeCalculator {

    private static Field tagMapField;

    static {
        try {
            // "tagMap" is the MCP name.
            // If running in a pure obfuscated production env without Forge, use its notch name: "a"
            tagMapField = NBTTagCompound.class.getDeclaredField("tagMap");
            tagMapField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the byte size of an NBTTagCompound without using streams.
     */
    @SuppressWarnings("unchecked")
    public static long getCompoundByteSize(NBTTagCompound compound) {
        if (compound == null) return 0;

        long size = 0;
        try {
            Map<String, NBTBase> map = (Map<String, NBTBase>) tagMapField.get(compound);

            for (Map.Entry<String, NBTBase> entry : map.entrySet()) {
                String key = entry.getKey();
                NBTBase tag = entry.getValue();
                size += 1 + 2 + key.getBytes("UTF-8").length;
                size += getTagPayloadSize(tag);
            }

            // Every NBTCompound ends with a TAG_End (1 byte)
            size += 1;

        } catch (Exception e) {
            MiTERenewed.LOGGER.throwing(e);
        }
        return size;
    }

    /**
     * Recursively calculates the payload size of any given NBT tag.
     */
    private static long getTagPayloadSize(NBTBase tag) throws Exception {
        // In 1.6.4, getId() returns the byte ID of the tag type
        switch (tag.getId()) {
            case 1:  return 1; // TAG_Byte
            case 2:  return 2; // TAG_Short
            case 3:  return 4; // TAG_Int
            case 4:  return 8; // TAG_Long
            case 5:  return 4; // TAG_Float
            case 6:  return 8; // TAG_Double

            case 7:  // TAG_Byte_Array
                // 4 bytes for integer length + 1 byte per element
                return 4 + ((NBTTagByteArray) tag).byteArray.length;

            case 8:  // TAG_String
                // 2 bytes for UTF-8 string length + actual string bytes
                String str = ((NBTTagString) tag).data;
                return 2 + (str == null ? 0 : str.getBytes("UTF-8").length);

            case 9:  // TAG_List
                NBTTagList list = (NBTTagList) tag;
                long listSize = 1 + 4; // 1 byte for type ID + 4 bytes for list size (int)

                // Reflection to get the internal Java List holding the tags in 1.6.4
                Field tagListField = NBTTagList.class.getDeclaredField("tagList");
                tagListField.setAccessible(true);
                Collection<NBTBase> internalList = (Collection<NBTBase>) tagListField.get(list);

                for (NBTBase subTag : internalList) {
                    listSize += getTagPayloadSize(subTag);
                }
                return listSize;

            case 10: // TAG_Compound
                // Recursively calculate sub-compounds.
                // Sub-compounds don't have named keys inside their own payload size calculation;
                // their keys are handled by the parent's loop.
                long compoundSize = 0;
                Map<String, NBTBase> subMap = (Map<String, NBTBase>) tagMapField.get(tag);
                for (Map.Entry<String, NBTBase> entry : subMap.entrySet()) {
                    compoundSize += 1 + 2 + entry.getKey().getBytes("UTF-8").length; // ID + Key len + Key
                    compoundSize += getTagPayloadSize(entry.getValue());
                }
                return compoundSize + 1; // +1 for TAG_End

            case 11: // TAG_Int_Array
                // 4 bytes for integer length + 4 bytes per integer
                return 4 + (((NBTTagIntArray) tag).intArray.length * 4);

            default:
                return 0;
        }
    }
}