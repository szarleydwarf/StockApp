package dbase;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import utillity.Helper;
import utillity.Logger;

public class EncryptionClass {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;
   
	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;

    public EncryptionClass(String p_loggerFolderPath){
    	myEncryptionKey = "ThisIsSpartaThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		loggerFolderPath = p_loggerFolderPath;
		log = new Logger(loggerFolderPath);
		helper = new Helper();
		date = helper.getFormatedDate();
		try {
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
	        ks = new DESedeKeySpec(arrayBytes);
	        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
	        cipher = Cipher.getInstance(myEncryptionScheme);
	        key = skf.generateSecret(ks);} catch (UnsupportedEncodingException e) {

	        log.logError(date+" "+this.getClass().getName()+"\tE "+e.getMessage());
		} catch (InvalidKeyException e1) {
			log.logError(date+" "+this.getClass().getName()+"\tE1 "+e1.getMessage());
			} catch (NoSuchAlgorithmException e2) {
				log.logError(date+" "+this.getClass().getName()+"\tE2 "+e2.getMessage());

		} catch (Exception e3) {
			log.logError(date+" "+this.getClass().getName()+"\tE3 "+e3.getMessage());
			}
    	
    }
    
    public SecretKey getSKey() throws Exception{
		myEncryptionKey = "ThisIsSpartaThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        return skf.generateSecret(ks);
    }
	public void saltPassword(String pass) throws Exception {
		String salt = "tezrn6H#s7|89jd-M@voxEBI|@£*cW$I";
		salt += pass;
        
		myEncryptionKey = "ThisIsSpartaThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
        
		String encoded = encrypt(salt, key);
//		System.out.println(encoded);
		String decoded = encrypt(salt, key);
//		System.out.println(decoded);
	}
    
    public String encrypt(String plainText, SecretKey secretKey)
            throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}
