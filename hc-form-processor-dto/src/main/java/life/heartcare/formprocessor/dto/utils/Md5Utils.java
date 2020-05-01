package life.heartcare.formprocessor.dto.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Md5Utils {

	public static String md5(String str)  {
	    MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			return DatatypeConverter.printHexBinary(digest).toLowerCase();		
		} catch (NoSuchAlgorithmException e) {
			log.error("md5 error", e);
		}
		return null;
	}
	
}
