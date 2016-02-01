package ist.a.alonsoba;

public class Packer {
	public static byte[] pack(int number) {
		byte b0 = (byte)((number >> 24) & 0xFF); // i0
		byte b1 = (byte)((number >> 16) & 0xFF); // i1
		byte b2 = (byte)((number >> 8) & 0xFF); // i2
		byte b3 = (byte)((number) & 0xFF); // i3
		
		return new byte[]{b3, b2, b1, b0};
	}
	
	public static int unpack(byte[] bytes) {
		int i0 = ((bytes[3] & 0xFF) << 24); // b0 
		int i1 = ((bytes[2] & 0xFF) << 16); // b1
		int i2 = ((bytes[1] & 0xFF) << 8); // b2
		int i3 = ((bytes[0] & 0xFF)); // b3
		
		return i0 | i1 | i2 | i3;
	}
}
