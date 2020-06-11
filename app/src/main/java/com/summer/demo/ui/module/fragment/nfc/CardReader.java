package com.summer.demo.ui.module.fragment.nfc;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;

public final class CardReader {
    private static final String TAG = "LoyaltyCardReader";
    public static String[][] TECHLISTS;
	public static IntentFilter[] FILTERS;
	public static int   tempdata;
	public static int   ref_body_store;
	public static int   ref_room_store;
	public static float   ref_body_offset;
	public static float   ref_room_offset;
	public static float real_temp = 20;
	public static float temp_dout = 20;
	public static int data_right = 1;
	private static int tmp_suc = 0;
	private static final String right_ack="5590";
	static {
		try {
			//the tech lists used to perform matching for dispatching of the ACTION_TECH_DISCOVERED intent
			TECHLISTS = new String[][] { { NfcA.class.getName() }};

			FILTERS = new IntentFilter[] { new IntentFilter(
					NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
		} catch (Exception e) {
		}
	}
	/////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
	
	@SuppressLint("NewApi") static public float tagDiscovered(Tag tag) {
		Log.e(TAG, "New tag discovered");
		String strResult="                                              ";
		String ee_body_store=" ";
		String ee_room_store=" ";
		String ee_body_offset=" ";
		String ee_room_offset=" ";
		NfcA NfcA=android.nfc.tech.NfcA.get(tag);
		if (NfcA != null) {
		   try{
			    // Connect to the remote NFC device
				Log.e("NFC----", "onNewIntent(getIntent())");
				tempdata=0;
				NfcA.connect();
				///////////////////////////////////////////////////////////////RATS
				byte[] result = null;
				byte[] RATS = new byte[2];
				RATS[0] =(byte) 0xE0;
				RATS[1] =0x28; 
				
				result = NfcA.transceive(RATS);
				String rats = " ";
				if(result != null) 
				{
					rats=ByteArrayToHexString(result);
					Log.e(TAG,"RATS is "+ rats);
					Log.e(TAG,"RATS is over");
				} 
				else 
				{
					data_right = 0;
					Log.e(TAG,"RATS is Null");
					Log.e(TAG,"RATS is over");
				}

				///////////////////////////////////////////////////////////////WR_EE
				String set_string_local= "                                           "; 
				set_string_local = NFCActivity.set_string;

				String str_set_head = set_string_local.substring(0,3);
				//int int_set_head = Str0xToInt(str_set_head);
				int set_suc = 0;
				Log.e(TAG,"str_set_head is "+str_set_head);
				if(str_set_head.equals("set") == true)
				{	
					set_suc = 1;
					Log.e(TAG,"set_head is right");}
				else if(str_set_head.equals("tmp") == true)
				{
					tmp_suc = 1;
					Log.e(TAG,"tmp_suc is right");}
				else
				{
					Log.e(TAG,"set_head is wrong");
				}
				
					
				String str_get_body_store = set_string_local.substring(3,7);
				String str_get_room_store = set_string_local.substring(7,11);
				String str_get_body_offset= set_string_local.substring(11,15);
				String str_get_room_offset= set_string_local.substring(15,19);
				String str_get_ref_tmp    = set_string_local.substring(3,7);  
			
				byte[] byte_get_body_store = stringToByte(str_get_body_store);
				byte[] byte_get_room_store = stringToByte(str_get_room_store);
				byte[] byte_get_body_offset= stringToByte(str_get_body_offset);
				byte[] byte_get_room_offset= stringToByte(str_get_room_offset); 
				byte[] byte_get_ref_tmp    = decimalStringToByte(str_get_ref_tmp);
			
			
			
				int i=0;
				result = null;
				byte[] cmd_wree0c = {0x55,0x11,0x0c,(byte) 0x5b,(byte) 0x00 };
				cmd_wree0c[3]=byte_get_body_store[1];
				cmd_wree0c[4]=byte_get_body_store[0];

				byte[] cmd_wree0d = {0x55,0x11,0x0d,(byte) 0x6b,(byte) 0x60 };
				cmd_wree0d[3]=byte_get_room_store[1];
				cmd_wree0d[4]=byte_get_room_store[0];
	
				byte[] cmd_wree0e = {0x55,0x11,0x0e,(byte) 0xff,(byte) 0xff };
				cmd_wree0e[3]=byte_get_body_offset[1];
				cmd_wree0e[4]=byte_get_body_offset[0];
		
				byte[] cmd_wree0f = {0x55,0x11,0x0f,(byte) 0xff,(byte) 0xff };
				cmd_wree0f[3]=byte_get_room_offset[1];
				cmd_wree0f[4]=byte_get_room_offset[0];

				byte[] cmd_ref_tmp = {0x55,0x11,0x0f,(byte) 0xff,(byte) 0xff };
				cmd_ref_tmp[2] = 0x0e;
				cmd_ref_tmp[3]=byte_get_ref_tmp[1];
				cmd_ref_tmp[4]=byte_get_ref_tmp[0];

			
				if( (NFCActivity.ee_set==1)&(set_suc==1) )
				{
					NFCActivity.ee_set=0;
					set_suc=0;
					Log.e(TAG,"WRITE TO EE for set");
					result = NfcA.transceive(cmd_wree0c);
					result = NfcA.transceive(cmd_wree0d);
					result = NfcA.transceive(cmd_wree0e);
					result = NfcA.transceive(cmd_wree0f);				
				}
				else if( (NFCActivity.ee_set==1)&(tmp_suc==1) )
				{
					NFCActivity.ee_set=0;
					tmp_suc=0;
					Log.e(TAG,"WRITE TO EE for tmp");
					result = NfcA.transceive(cmd_ref_tmp);
					///////////////////////////////////////////////////////
					result = NfcA.transceive(NFCActivity.cmd_sens);
					strResult=ByteArrayToHexString(result);
					Log.e(TAG,"!!!!!!!!!!!!!!!!! strResult is "+strResult);
					byte[] tmp_rd = stringToByte(strResult.substring(4, 8));
					if(strResult.substring(0, 4).equals(right_ack)==true)
					{
						Log.e(TAG,"rd_tmp is " + strResult.substring(4, 8) );
						cmd_wree0c[3]=tmp_rd[1];
						cmd_wree0c[4]=tmp_rd[0];
						result=NfcA.transceive(cmd_wree0c);
					}
				///////////////////////////////////////////////////////
				}
				else	Log.e(TAG,"dont not write EE");
				///////////////////////////////////////////////////////////////RD_EE
				result = null;
				byte[] cmd_rdee_store={0x55,0x10,0x0c,0x02};
				result = NfcA.transceive(cmd_rdee_store);
				Log.e(TAG,"EEVALUE"+result);
				strResult=ByteArrayToHexString(result);
				Log.e(TAG,"EEVALUE_STR is "+strResult);
				ee_body_store=strResult.substring(4,7);
				Log.e(TAG,"ee_body_store "+ ee_body_store);
				ref_body_store=Str0xToInt(ee_body_store);
				ee_room_store=strResult.substring(8,11);
				Log.e(TAG,"ee_room_store "+ ee_room_store);
				ref_room_store=Str0xToInt(ee_room_store);
				Log.e(TAG,"ref_room_store "+ ref_room_store);
				Log.e(TAG,"ref_body_store "+ ref_body_store);
			
				byte[] cmd_rdee_offset={0x55,0x10,0x0e,0x02};
				result = NfcA.transceive(cmd_rdee_offset);
				strResult=ByteArrayToHexString(result);
				Log.e(TAG,"EEVALUE_STR111 is "+strResult);
				ee_body_offset=strResult.substring(4,8);
				ee_room_offset=strResult.substring(8,12);
				ref_body_offset=( (float)(Str0xToInt(ee_body_offset)) )/10;
				ref_room_offset=( (float)(Str0xToInt(ee_room_offset)) )/10;
				Log.e(TAG,"ee_body_offset is "+ee_body_offset);
				Log.e(TAG,"REF BODY OFFSET "+ Str0xToInt(ee_body_offset));
				Log.e(TAG,"ee_room_offset is "+ee_room_offset);
				Log.e(TAG,"ref_body_offset is "+ref_body_offset);
				Log.e(TAG,"ref_room_offset is "+ref_room_offset);
				///////////////////////////////////////////////////////////////RD_TEMP
				Log.e(TAG,"======== read temp ============= ");
				int[] array_in  = new int[9];
				int[] array_out = new int[9];
				for(i=0;i<9;i++)
				{
					array_in[i] = 0;
					array_out[i] = 0;
				}

				int k=0;
				for(i=0;i<9;i++)
				{
					if(k==1 & i!=0)    i--;
					result = null;
					result = NfcA.transceive(NFCActivity.cmd_sens);
					if(result != null)
					{
						k=0;
						strResult=ByteArrayToHexString(result);
						if(strResult.substring(0, 4).equals(right_ack)==false) 
						{
							k=1;
							tempdata=0;
							Log.e(TAG,"transcieve result FALSE!! ");
						}
						else 
						{
							k=0;
							String hex = strResult.substring(4, 7);
							tempdata=Str0xToInt(hex);

							array_in[i]=tempdata;
							Log.e(TAG,"tempdata is "+ tempdata);
						}
					}
					else 
					{
						k=1;
						tempdata=0;
						Log.e(TAG,"transcieve result NULL!! ");
					}
				}
				//Log.e(TAG,"data_right is "+data_right);

			
				/////////////////////////////////////////////////////////////////cal
				array_out=sort(array_in);

/*				
				tempdata = 0;
				for(i=1;i<7;i++) tempdata = tempdata + array_out[i];
				tempdata = tempdata/6;
*/
				tempdata = array_out[4];
				
				real_temp= (float) (ref_body_offset - ( (tempdata - ref_body_store) * 0.026 ));


				Log.e(TAG,"real_temp is"+real_temp);
			
				if(NFCActivity.model==0)	temp_dout= (float)(Math.round(real_temp*10))/10;
				else 				temp_dout= (float)(Math.round(real_temp*100))/100;
		
				/////////////////////////////////////////////////////////////////
				NfcA.close();

				if( strResult=="null" )	strResult = "null";
				else 					return temp_dout;
			//return strResult;
			}catch(IOException e){
				Log.e(TAG, "Error communicating with card: " + e.toString());
			}
		}  
		Log.e(TAG, "Error communicating with card: ");
		temp_dout = 20;
		return temp_dout;
	}

	
	public static float load(Parcelable parcelable) {
		// ��Parcelableɸѡ������NFC��׼����
		final Tag tag = (Tag) parcelable;
		return tagDiscovered(tag);
	}
	
	public static int Str0xToInt(String string)
	{
		int sresult = 0;
        for ( char c : string.toCharArray() ) 
        	sresult = sresult * 16 + Character.digit( c, 16 );
        return sresult;
	}
	
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static byte[] intToByte(int i) {
        byte[] abyte0 = new byte[4];
        abyte0[0] = (byte) (0xff & i);
        abyte0[1] = (byte) ((0xff00 & i) >> 8);
        abyte0[2] = (byte) ((0xff0000 & i) >> 16);
        abyte0[3] = (byte) ((0xff000000 & i) >> 24);
        return abyte0;
    }
    
    public static int[] sort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
 
                if (array[i] > array[j]) {
                    int tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                }
            }
        }
        return array;
    }
    
    
    public static byte[] stringToByte(String string_in){
    	int int_value       = Str0xToInt(string_in);           			
		byte[] byte_value   = intToByte(int_value);
		return byte_value;
    }
    
    public static byte[] decimalStringToByte(String dstring){
    	int i;
    	int temp;
    	int sum = 0;
    	int temp1;
    	for(i=0;i<4;i++){
    		//Log.e(TAG,"/////////////////////////////////////////////////////");
    		temp = Str0xToInt(dstring.substring(3-i,4-i));
    		//Log.e(TAG,"substring(i) is "+dstring.substring(3-i,4-i));
    		temp1 = (int) Math.pow(10,i);
    		//Log.e(TAG,"temp1 is "+temp1);
    		sum = sum + temp*temp1;
    		//Log.e(TAG,"temp is_"+temp+"_i is_"+i);
    		//Log.e(TAG,"sum is_"+sum+"_sum is_"+sum);
    	}
    	Log.e(TAG,"sum is "+sum);
    	byte[] byte_value    = intToByte(sum);
    	return byte_value;
    }
}
