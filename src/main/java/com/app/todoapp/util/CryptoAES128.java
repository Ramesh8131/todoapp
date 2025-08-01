package com.app.todoapp.util;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Service
public class CryptoAES128 {

	private static final String KEY="Bar12345Bar56435";
	
//	private static final String KEY = "#sma#key#Ra_3weqZ3owgPT9Y6Bcx";
    private static final String PBE_WITH_MD_5_AND_DES_MODE = "PBEWithMD5AndDES";
    private static Cipher ecipher;
    private static Cipher dcipher;
    private static CryptoAES128 instance = new CryptoAES128();

    
    private CryptoAES128() {
        // 8-bytes Salt
        final byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03};
        // Iteration count
        int iterationCount = 19;
        try {
            final KeySpec keySpec = new PBEKeySpec(KEY.toCharArray(), salt, iterationCount);
            final SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD_5_AND_DES_MODE).generateSecret(keySpec);
            ecipher = Cipher.getInstance(PBE_WITH_MD_5_AND_DES_MODE);
            dcipher = Cipher.getInstance(PBE_WITH_MD_5_AND_DES_MODE);
            // Prepare the parameters to the cipthers
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException e) {
//            log.error("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
//            log.error("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
//            log.error("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
//            log.error("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
//            log.error("EXCEPTION: InvalidKeyException");
        }
    }
    
    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            final byte[] utf8 = str.getBytes("UTF8");
            // Encrypt
            final byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
            String token1 = javax.xml.bind.DatatypeConverter.printBase64Binary(enc);
            String token=instance.stringToHex(token1);
            return token;
        } catch (Exception e) {
//            log.error(e.getMessage());
        }
        return null;
    }
        
    public String decrypt(String token) {
        try {
        	String str=instance.hexToString(token);
             // Decode base64 to get bytes
            final byte[] dec = javax.xml.bind.DatatypeConverter.parseBase64Binary(str);
            // Decrypt
            final byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (Exception e) {
//            log.error(e.getMessage());
        }
        return null;
    }
	
	public   String stringToHex(String token) {
		char[] chars = token.toCharArray();
	    StringBuilder hex = new StringBuilder();
	    for (char ch : chars) {
	        hex.append(Integer.toHexString((int) ch));
	    }

	    return hex.toString();
//		return token;
	}
	
	public   String hexToString(String token) {
		 StringBuilder output = new StringBuilder("");
		    
		    for (int i = 0; i < token.length(); i += 2) {
		        String str = token.substring(i, i + 2);
		        output.append((char) Integer.parseInt(str, 16));
		    }
		    
		    return output.toString();
	}
}
