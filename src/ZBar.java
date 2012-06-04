/*------------------------------------------------------------------------
 *  Copyright 2012 (c) Kiko Qiu <kikoqiu@163.com>
 *
 *  This file is part of the QRReader.
 *
 *  The QRReader is free software; you can redistribute it
 *  and/or modify it under the terms of the GNU Lesser Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  The QRReader is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with the QRReader; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 *  Boston, MA  02110-1301  USA
 *  http://code.google.com/p/java-qr-code-reader-with-zbar/
 *------------------------------------------------------------------------*/

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.NullPointer;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

public class ZBar {

	public static class Symbol {
		public int type;
		public String data;
		public int[] xs;
		public int[] ys;
	}

	static JNative zbar_image_scanner_create;
	static JNative zbar_image_scanner_set_config;
	static JNative zbar_image_create;
	static JNative zbar_image_set_format;
	static JNative zbar_image_set_size;
	static JNative zbar_image_set_data;
	static JNative zbar_image_free_data;
	static JNative zbar_scan_image;
	static JNative zbar_image_first_symbol;
	static JNative zbar_symbol_next;
	static JNative zbar_symbol_get_type;
	static JNative zbar_get_symbol_name;
	static JNative zbar_symbol_get_data;
	static JNative zbar_image_destroy;
	
	static JNative zbar_symbol_get_loc_size;
	static JNative zbar_symbol_get_loc_x;
	static JNative zbar_symbol_get_loc_y;
	
	
	
	static String dll = "libzbar-0.dll";
	static int scanner;
	
	static{
		try {
			zbar_image_scanner_create = new JNative(dll,
					"zbar_image_scanner_create");	
			zbar_image_scanner_create.setRetVal(Type.INT);
			
			zbar_image_scanner_set_config = new JNative(dll,
					"zbar_image_scanner_set_config");
			zbar_image_scanner_set_config.setRetVal(Type.INT);
			
			zbar_image_create = new JNative(dll, "zbar_image_create");
			zbar_image_create.setRetVal(Type.INT);
			
			zbar_image_set_format = new JNative(dll, "zbar_image_set_format");
			zbar_image_set_format.setRetVal(Type.INT);
			
			zbar_image_set_size = new JNative(dll, "zbar_image_set_size");
			zbar_image_set_size.setRetVal(Type.INT);
			
			zbar_image_set_data = new JNative(dll, "zbar_image_set_data");
			zbar_image_set_data.setRetVal(Type.INT);
			
			zbar_image_free_data= new JNative(dll, "zbar_image_free_data");	
			zbar_image_free_data.setRetVal(Type.INT);
			
			zbar_scan_image = new JNative(dll, "zbar_scan_image");
			zbar_scan_image.setRetVal(Type.INT);
			
			zbar_image_first_symbol = new JNative(dll, "zbar_image_first_symbol");
			zbar_image_first_symbol.setRetVal(Type.INT);
			
			zbar_symbol_next = new JNative(dll, "zbar_symbol_next");
			zbar_symbol_next.setRetVal(Type.INT);			
			
			zbar_symbol_get_type = new JNative(dll, "zbar_symbol_get_type");
			zbar_symbol_get_type.setRetVal(Type.INT);
			
			zbar_get_symbol_name = new JNative(dll, "zbar_get_symbol_name");
			zbar_get_symbol_name.setRetVal(Type.INT);
			
			zbar_symbol_get_data = new JNative(dll, "zbar_symbol_get_data");
			zbar_symbol_get_data.setRetVal(Type.STRING);
			
			zbar_image_destroy = new JNative(dll, "zbar_image_destroy");
			zbar_image_destroy.setRetVal(Type.INT);
			
			zbar_symbol_get_loc_size = new JNative(dll, "zbar_symbol_get_loc_size");
			zbar_symbol_get_loc_size.setRetVal(Type.INT);
			zbar_symbol_get_loc_x = new JNative(dll, "zbar_symbol_get_loc_x");
			zbar_symbol_get_loc_x.setRetVal(Type.INT);
			zbar_symbol_get_loc_y = new JNative(dll, "zbar_symbol_get_loc_y");
			zbar_symbol_get_loc_y.setRetVal(Type.INT);
			
			zbar_image_scanner_create.invoke();
			scanner=zbar_image_scanner_create.getRetValAsInt();
			
			zbar_image_scanner_set_config.setParameter(0, scanner);
			zbar_image_scanner_set_config.setParameter(1,0);
			zbar_image_scanner_set_config.setParameter(2, 0);
			zbar_image_scanner_set_config.setParameter(3, 1);
			zbar_image_scanner_set_config.invoke();
			
			
			
			
			
		} catch (NativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
	

  public static Pointer creatPointer(byte[] data) throws NativeException{ 
	   Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(data.length)); 
       pointer.setMemory(data);      
       return pointer; 
    } 
  public static byte[] getY800(BufferedImage originalPic){
	  int imageWidth = originalPic.getWidth();  
      int imageHeight = originalPic.getHeight();  

      BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,  
              BufferedImage.TYPE_BYTE_GRAY);  

      ColorConvertOp cco = new ColorConvertOp(ColorSpace  
              .getInstance(ColorSpace.CS_GRAY), null);  
      cco.filter(originalPic, newPic);  
	  byte[]   data   =   ((DataBufferByte)   newPic.getData().getDataBuffer()).getData();
	  return data;
  }
  
	public static List<Symbol> scan(BufferedImage bi) {
		try {
			List<Symbol> ret=new ArrayList<Symbol>();
			
			zbar_image_create.invoke();
			int image=zbar_image_create.getRetValAsInt();
			if(image==0){
				return ret;
			}
			
			zbar_image_set_format.setParameter(0, image);
			zbar_image_set_format.setParameter(1, 0x30303859);
			zbar_image_set_format.invoke();			
			
			zbar_image_set_size.setParameter(0, image);
			zbar_image_set_size.setParameter(1, bi.getWidth());
			zbar_image_set_size.setParameter(2,bi.getHeight());
			zbar_image_set_size.invoke();
			
			Pointer mem=creatPointer(getY800(bi));
			zbar_image_set_data.setParameter(0, image);
			zbar_image_set_data.setParameter(1,mem);
			zbar_image_set_data.setParameter(2, bi.getWidth()*bi.getHeight());
			zbar_image_set_data.setParameter(3, NullPointer.NULL);
			zbar_image_set_data.invoke();
			
			zbar_scan_image.setParameter(0, scanner);
			zbar_scan_image.setParameter(1, image);
			zbar_scan_image.invoke();
			if(zbar_scan_image.getRetValAsInt()>0){				
				zbar_image_first_symbol.setParameter(0, image);
				zbar_image_first_symbol.invoke();
				int symbol=zbar_image_first_symbol.getRetValAsInt();
				while(symbol!=0){
					Symbol s=new Symbol();
					zbar_symbol_get_type.setParameter(0, symbol);
					zbar_symbol_get_type.invoke();
					s.type=zbar_symbol_get_type.getRetValAsInt();
					
					zbar_symbol_get_data.setParameter(0, symbol);
					zbar_symbol_get_data.invoke();				
					s.data=zbar_symbol_get_data.getRetVal();		
					
					
					
					zbar_symbol_get_loc_size.setParameter(0, symbol);
					zbar_symbol_get_loc_size.invoke();
					int c=zbar_symbol_get_loc_size.getRetValAsInt();
					s.xs=new int[c];
					s.ys=new int[c];
					for(int i=0;i<c;++i){
						zbar_symbol_get_loc_x.setParameter(0, symbol);
						zbar_symbol_get_loc_x.setParameter(1, i);
						zbar_symbol_get_loc_x.invoke();
						s.xs[i]=zbar_symbol_get_loc_x.getRetValAsInt();
						
						zbar_symbol_get_loc_y.setParameter(0, symbol);
						zbar_symbol_get_loc_y.setParameter(1, i);
						zbar_symbol_get_loc_y.invoke();
						s.ys[i]=zbar_symbol_get_loc_y.getRetValAsInt();
					}
					
					ret.add(s);
					
					zbar_symbol_next.setParameter(0, symbol);
					zbar_symbol_next.invoke();
					symbol=zbar_symbol_next.getRetValAsInt();
					
				
				}
			}
			zbar_image_destroy.setParameter(0, image);
			zbar_symbol_next.invoke();			
			mem.dispose();
			
			return ret;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
//	public static void main(String[] argv){
//		 
//		try {
//			BufferedImage   srcImage   =   ImageIO.read(new   File( "orig.png"));
//			for(Symbol s:scan(srcImage)){
//				System.out.print(new String(s.data.getBytes(),"utf-8"));
//			}
//			for(Symbol s:scan(srcImage)){
//				System.out.print(new String(s.data.getBytes(),"utf-8"));
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
}
