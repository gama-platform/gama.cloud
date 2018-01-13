package com.jogamp.common.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class Buffers {

	public static final int SIZEOF_BYTE = 1;
	public static final int SIZEOF_SHORT = 2;
	public static final int SIZEOF_CHAR = 2;
	public static final int SIZEOF_INT = 4;
	public static final int SIZEOF_FLOAT = 4;
	public static final int SIZEOF_LONG = 8;
	public static final int SIZEOF_DOUBLE = 8;

    public static ByteBuffer nativeOrder(final ByteBuffer buf) {
        return buf.order(ByteOrder.nativeOrder());
}
	/**
	 * Allocates a new direct ByteBuffer with the specified number of elements.
	 * The returned buffer will have its byte order set to the host platform's
	 * native byte order.
	 */
	public static ByteBuffer newDirectByteBuffer(final int numElements) {
		return nativeOrder(ByteBuffer.allocateDirect(numElements));
	}

	public static ByteBuffer newDirectByteBuffer(final byte[] values, final int offset, final int length) {
		return (ByteBuffer) newDirectByteBuffer(length).put(values, offset, length).rewind();
	}

	public static ByteBuffer newDirectByteBuffer(final byte[] values, final int offset) {
		return newDirectByteBuffer(values, offset, values.length - offset);
	}

	public static ByteBuffer newDirectByteBuffer(final byte[] values) {
		return newDirectByteBuffer(values, 0);
	}

	/**
	 * Allocates a new direct DoubleBuffer with the specified number of
	 * elements. The returned buffer will have its byte order set to the host
	 * platform's native byte order.
	 */
	public static DoubleBuffer newDirectDoubleBuffer(final int numElements) {
		return newDirectByteBuffer(numElements * SIZEOF_DOUBLE).asDoubleBuffer();
	}

	public static DoubleBuffer newDirectDoubleBuffer(final double[] values, final int offset, final int length) {
		return (DoubleBuffer) newDirectDoubleBuffer(length).put(values, offset, length).rewind();
	}

	public static DoubleBuffer newDirectDoubleBuffer(final double[] values, final int offset) {
		return newDirectDoubleBuffer(values, offset, values.length - offset);
	}

	public static DoubleBuffer newDirectDoubleBuffer(final double[] values) {
		return newDirectDoubleBuffer(values, 0);
	}

	/**
	 * Allocates a new direct FloatBuffer with the specified number of elements.
	 * The returned buffer will have its byte order set to the host platform's
	 * native byte order.
	 */
	public static FloatBuffer newDirectFloatBuffer(final int numElements) {
		return newDirectByteBuffer(numElements * SIZEOF_FLOAT).asFloatBuffer();
	}

	public static FloatBuffer newDirectFloatBuffer(final float[] values, final int offset, final int length) {
		return (FloatBuffer) newDirectFloatBuffer(length).put(values, offset, length).rewind();
	}

	public static FloatBuffer newDirectFloatBuffer(final float[] values, final int offset) {
		return newDirectFloatBuffer(values, offset, values.length - offset);
	}

	public static FloatBuffer newDirectFloatBuffer(final float[] values) {
		return newDirectFloatBuffer(values, 0);
	}

	/**
	 * Allocates a new direct IntBuffer with the specified number of elements.
	 * The returned buffer will have its byte order set to the host platform's
	 * native byte order.
	 */
	public static IntBuffer newDirectIntBuffer(final int numElements) {
		return newDirectByteBuffer(numElements * SIZEOF_INT).asIntBuffer();
	}

	public static IntBuffer newDirectIntBuffer(final int[] values, final int offset, final int length) {
		return (IntBuffer) newDirectIntBuffer(length).put(values, offset, length).rewind();
	}

	public static IntBuffer newDirectIntBuffer(final int[] values, final int offset) {
		return newDirectIntBuffer(values, offset, values.length - offset);
	}

	public static IntBuffer newDirectIntBuffer(final int[] values) {
		return newDirectIntBuffer(values, 0);
	}

	/**
	 * Allocates a new direct LongBuffer with the specified number of elements.
	 * The returned buffer will have its byte order set to the host platform's
	 * native byte order.
	 */
	public static LongBuffer newDirectLongBuffer(final int numElements) {
		return newDirectByteBuffer(numElements * SIZEOF_LONG).asLongBuffer();
	}

	public static LongBuffer newDirectLongBuffer(final long[] values, final int offset, final int length) {
		return (LongBuffer) newDirectLongBuffer(length).put(values, offset, length).rewind();
	}

	public static LongBuffer newDirectLongBuffer(final long[] values, final int offset) {
		return newDirectLongBuffer(values, offset, values.length - offset);
	}

	public static LongBuffer newDirectLongBuffer(final long[] values) {
		return newDirectLongBuffer(values, 0);
	}

	/**
	 * Allocates a new direct ShortBuffer with the specified number of elements.
	 * The returned buffer will have its byte order set to the host platform's
	 * native byte order.
	 */
	public static ShortBuffer newDirectShortBuffer(final int numElements) {
		return newDirectByteBuffer(numElements * SIZEOF_SHORT).asShortBuffer();
	}

	public static ShortBuffer newDirectShortBuffer(final short[] values, final int offset, final int length) {
		return (ShortBuffer) newDirectShortBuffer(length).put(values, offset, length).rewind();
	}

	public static ShortBuffer newDirectShortBuffer(final short[] values, final int offset) {
		return newDirectShortBuffer(values, offset, values.length - offset);
	}

	public static ShortBuffer newDirectShortBuffer(final short[] values) {
		return newDirectShortBuffer(values, 0);
	}

	/**
	 * Allocates a new direct CharBuffer with the specified number of elements.
	 * The returned buffer will have its byte order set to the host platform's
	 * native byte order.
	 */
	public static CharBuffer newDirectCharBuffer(final int numElements) {
		return newDirectByteBuffer(numElements * SIZEOF_SHORT).asCharBuffer();
	}

	public static CharBuffer newDirectCharBuffer(final char[] values, final int offset, final int length) {
		return (CharBuffer) newDirectCharBuffer(length).put(values, offset, length).rewind();
	}

	public static CharBuffer newDirectCharBuffer(final char[] values, final int offset) {
		return newDirectCharBuffer(values, offset, values.length - offset);
	}

	public static CharBuffer newDirectCharBuffer(final char[] values) {
		return newDirectCharBuffer(values, 0);
	}

}
