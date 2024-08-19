package zabi.minecraft.extraalchemy.utils;

import java.lang.reflect.InvocationTargetException;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

public class CodecUtil {
	
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> Codec<E> enumCodec(Class<E> clazz) {
		
		E[] values;
		try {
			values = ((E[]) clazz.getMethod("values").invoke(null));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
		
		return new PrimitiveCodec<E>() {
	        @Override
	        public <T> DataResult<E> read(final DynamicOps<T> ops, final T input) {
	        	
	            return ops
	                .getNumberValue(input)
	                .map(Number::intValue)
	                .map(i -> values[i]);
	        }

	        @Override
	        public <T> T write(final DynamicOps<T> ops, final E value) {
	            return ops.createInt(value.ordinal());
	        }

	        @Override
	        public String toString() {
	            return "FixedEnum";
	        }
	    };
	}
	
}
