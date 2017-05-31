package cxm.example.kurento.util;


public class XArrays {
	static public void byteArray2ShortArray(byte[] data, short []transferBuffer) {
		if (data.length != transferBuffer.length * 2)
			throw new IllegalArgumentException("Array length");
		/*
		ByteBuffer.wrap(data).order(
				ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(transferBuffer);
		*/
		for (int i = 0; i < transferBuffer.length; i++)
	        transferBuffer[i] = (short)((data[i * 2 + 1] << 8) | (data[i * 2] & 0xff));
	}
	
	/*
	static public short[] byteArray2ShortArray(byte[] data, int start, int num) {
		// array copy
		byte []dataCopy = new byte[num];
		System.arraycopy(data, start, dataCopy, 0, num);
		
		return byteArray2ShortArray(dataCopy);
	}
	*/
	
	public static void shortArray2ByteArray(short shorts[], byte transferBuffer[]) {
		if (shorts.length != transferBuffer.length / 2)
			throw new IllegalArgumentException("Array length");

		/*
		ByteBuffer.wrap(transferBuffer).order(
				ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shorts);
		*/
	    for (int n = 0; n < shorts.length; n++) {
	        byte lsb = (byte)(shorts[n] >> 0);
	        byte msb = (byte)(shorts[n] >> 8);
	        /*
	        if (bigendian) {
	            bytes[2*n]   = msb;
	            bytes[2*n+1] = lsb;
	        } else {
	        */
            transferBuffer[2 * n] = lsb;
            transferBuffer[2 * n + 1] = msb;
	        // }
	    }
	}
	
	/*
	public static byte[] shortArray2ByteArray(short shorts[], int start, int num) {
		short []dataCopy = new short[num];
		System.arraycopy(shorts, start, dataCopy, 0, num);
		
		return shortArray2ByteArray(dataCopy);
	}
	*/
}
